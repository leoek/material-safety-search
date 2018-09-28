import { UPDATE_SEARCH_INPUT, RESET_SEARCH_INPUT } from "../actions";

const initialState = {};

const searchInput = (state = initialState, action) => {
  const { type, payload } = action;
  if (type === UPDATE_SEARCH_INPUT) {
    const { update } = payload || {};
    return {
      ...state,
      ...update
    };
  } else if (type === RESET_SEARCH_INPUT) {
    return initialState;
  }
  return state;
};

export default searchInput;
