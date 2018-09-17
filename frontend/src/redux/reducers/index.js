import { combineReducers } from "redux";
import { reducer as formReducer } from "redux-form";

import search from "./search";

const reducers = {
  form: formReducer,
  search
};

export const getRootReducer = combineReducers(reducers);

export default reducers;
