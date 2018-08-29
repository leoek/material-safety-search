import { createStore, applyMiddleware, compose } from "redux";
import { persistStore, persistCombineReducers } from "redux-persist";
import storage from "redux-persist/lib/storage";
import { createBlacklistFilter } from "redux-persist-transform-filter";
import createSagaMiddleware from "redux-saga";

import createHistory from "history/createBrowserHistory";
import { routerReducer, routerMiddleware } from "react-router-redux";

import reducers from "./reducers";
import rootSaga from "./sagas";

//https://github.com/zalmoxisus/redux-devtools-extension
const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

/**
 * documentation on redux persist filters
 * https://github.com/edy/redux-persist-transform-filter
 */
const loginFilter = createBlacklistFilter("login", ["error"]);
const persistConfig = {
  key: "root",
  storage,
  whitelist: ["login"],
  transforms: [loginFilter]
};

// creates the store
export default () => {
  // Create a history of your choosing (we're using a browser history in this case)
  const history = createHistory();

  //create the rootReducer
  const rootReducer = persistCombineReducers(persistConfig, {
    ...reducers,
    router: routerReducer
  });

  // create the saga middleware
  const sagaMiddleware = createSagaMiddleware();

  // create the touter middleware, aggregate the middlewares
  const middlewares = [sagaMiddleware, routerMiddleware(history)];
  const enhancers = [];
  enhancers.push(applyMiddleware(...middlewares));

  const store = createStore(
    rootReducer,
    undefined,
    composeEnhancers(...enhancers)
  );

  //run sagas
  sagaMiddleware.run(rootSaga);

  //start persisting the store
  const persistor = persistStore(store, null, () => store.getState());

  return { persistor, store, history };
};
