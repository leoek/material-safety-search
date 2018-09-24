import { combineReducers } from "redux";
import { reducer as formReducer } from "redux-form";

import search from "./search";
import searchInput from "./searchInput";
import suggest from "./suggest";
import ui from "./ui";

const reducers = {
  form: formReducer,
  search,
  searchInput,
  ui,
  suggest
};

export const getRootReducer = combineReducers(reducers);

export default reducers;
