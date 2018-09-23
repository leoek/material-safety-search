export const REDUX_REHYDRATION_COMPLETED = "MSS/REDUX_REHYDRATION_COMPLETED";
export const reduxRehydrationCompleted = () => ({
  type: REDUX_REHYDRATION_COMPLETED
});

export const FETCH_SEARCH_REQUEST = "MSS/FETCH_SEARCH_REQUEST";
export const fetchSearchRequest = payload => ({
  type: FETCH_SEARCH_REQUEST,
  payload
});

export const FETCH_SEARCH_SUCCESS = "MSS/FETCH_SEARCH_SUCCESS";
export const fetchSearchSuccess = data => ({
  type: FETCH_SEARCH_SUCCESS,
  payload: {
    data,
    timeFetched: new Date()
  }
});

export const FETCH_SEARCH_FAILURE = "MSS/FETCH_SEARCH_FAILURE";
export const fetchSearchFailure = error => ({
  type: FETCH_SEARCH_FAILURE,
  payload: {
    error,
    timeFetched: new Date()
  }
});

export const SHOW_DATASHEET_SECTION = "MSS/SHOW_DATASHEET_SECTION";
export const showDatasheetSection = (datasheet, section) => ({
  type: SHOW_DATASHEET_SECTION,
  payload: { datasheet, section }
});

export const CLOSE_DATASHEET_SECTION = "MSS/CLOSE_DATASHEET_SECTION";
export const closeDatasheetSection = () => ({
  type: CLOSE_DATASHEET_SECTION
});

export const SHOW_DATASHEET = "MSS/SHOW_DATASHEET";
export const showDatasheet = datasheet => ({
  type: SHOW_DATASHEET,
  payload: { datasheet }
});

export const CLOSE_DATASHEET = "MSS/CLOSE_DATASHEET";
export const closeDatasheet = () => ({
  type: CLOSE_DATASHEET
});

export const SELECT_FACET = "MSS/SELECT_FACET";
export const selectFacet = facet => ({
  type: SELECT_FACET,
  payload: {
    facet
  }
});

export const UPDATE_SEARCH_INPUT = "MSS/UPDATE_SEARCH_INPUT";
export const updateSearchInput = update => ({
  type: UPDATE_SEARCH_INPUT,
  payload: {
    update
  }
});
