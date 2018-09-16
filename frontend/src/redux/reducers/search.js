import {
  FETCH_SEARCH_REQUEST,
  FETCH_SEARCH_SUCCESS,
  FETCH_SEARCH_FAILURE
} from "../actions";

const initialState = {
  items: null,
  meta: null,
  timeFetched: null,
  isFetching: false
};

const search = (state = initialState, action) => {
  const { type, payload, time } = action;
  if (type === FETCH_SEARCH_REQUEST) {
    return {
      ...state,
      isFetching: true
    };
  } else if (type === FETCH_SEARCH_SUCCESS) {
    const { data, timeFetched } = payload;
    return {
      ...state,
      ...data,
      timeFetched,
      isFetching: false
    };
  } else if (type === FETCH_SEARCH_FAILURE) {
    const { timeFetched, error } = payload;
    return {
      ...state,
      error,
      timeFetched,
      isFetching: false
    };
  }
  return state;
};

export default search;
