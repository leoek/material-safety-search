import { combineReducers } from "redux";
import { reducer as formReducer } from "redux-form";

const reducers = {
  form: formReducer
};

export const getRootReducer = combineReducers(reducers);

export default reducers;
