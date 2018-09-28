import { combineReducers } from "redux";
import { reducer as formReducer } from "redux-form";

import search from "./search";
import searchInput from "./searchInput";
import suggest from "./suggest";
import ui from "./ui";
import log from "./log";

const reducers = {
  form: formReducer,
  search,
  searchInput,
  ui,
  suggest,
  log
};

export const getRootReducer = combineReducers(reducers);

export default reducers;
