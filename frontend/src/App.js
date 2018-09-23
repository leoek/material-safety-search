import React, { Component } from "react";
import { Provider } from "react-redux";
import { PersistGate } from "redux-persist/integration/react";
import { I18nextProvider } from "react-i18next";

/**
 * Look here for more information on ConnectedRouter
 * (can be easily confused with the old react-router-redux)...
 * https://github.com/ReactTraining/react-router/tree/master/packages/react-router-redux
 */
import { ConnectedRouter } from "react-router-redux";
import { Route } from "react-router";

import { changeLanguage, i18n } from "./i18n";
import configureStore from "./redux/createStore";

import { MuiThemeProvider, createMuiTheme } from "@material-ui/core/styles";

import MainScreen from "./components/MainScreen";

const { store, history, persistor } = configureStore();
const theme = createMuiTheme({
  palette: {
    background: {
      main: "#ededed"
    }
  }
});

class App extends Component {
  constructor(props) {
    super(props);
    //Use a fixed language in development for now
    if (process.env.NODE_ENV === "development") {
      changeLanguage("en");
      //console.log(i18n);
    }
  }

  render() {
    return (
      <PersistGate loading={null} persistor={persistor}>
        <Provider store={store}>
          <I18nextProvider i18n={i18n}>
            <MuiThemeProvider theme={theme}>
              <ConnectedRouter history={history}>
                <div>
                  <Route exact path="/" component={MainScreen} />
                </div>
              </ConnectedRouter>
            </MuiThemeProvider>
          </I18nextProvider>
        </Provider>
      </PersistGate>
    );
  }
}

export default App;
