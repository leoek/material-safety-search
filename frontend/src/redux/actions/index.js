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

export const SHOW_DATASHEET = "MSS/SHOW_DATASHEET";
export const showDatasheet = datasheet => ({
  type: SHOW_DATASHEET,
  payload: { datasheet }
});
