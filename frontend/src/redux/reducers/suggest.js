import {
  FETCH_SUGGEST_REQUEST,
  FETCH_SUGGEST_SUCCESS,
  FETCH_SUGGEST_FAILURE
} from "../actions";

const initialState = {
  productId: {
    suggestions: null,
    timeFetched: null,
    isFetching: false
  }
};

const search = (state = initialState, action) => {
  const { type, payload } = action;
  if (type === FETCH_SUGGEST_REQUEST) {
    const { field } = payload || {};
    return {
      ...state,
      [field]: {
        ...(state[field] || {}),
        isFetching: true
      }
    };
  } else if (type === FETCH_SUGGEST_SUCCESS) {
    const { data, timeFetched } = payload;
    return {
      ...state,
      [field]: {
        ...(state[field] || {}),
        ...data,
        timeFetched,
        isFetching: false
      }
    };
  } else if (type === FETCH_SUGGEST_FAILURE) {
    const { timeFetched, error } = payload;
    return {
      ...state,
      [field]: {
        ...(state[field] || {}),
        error,
        timeFetched,
        isFetching: false
      }
    };
  }
  return state;
};

export default search;
