import { actionTypes as reduxFormActionTypes } from "redux-form";

import config from "../../config";

export const REDUX_FORM_SUBMIT = reduxFormActionTypes.SUBMIT;
export const REDUX_FORM_SUBMIT_SUCCEEDED =
  reduxFormActionTypes.SET_SUBMIT_SUCCEEDED;

export const REDUX_REHYDRATION_COMPLETED = "MSS/REDUX_REHYDRATION_COMPLETED";
export const reduxRehydrationCompleted = () => ({
  type: REDUX_REHYDRATION_COMPLETED
});

/**
 * API Actions /search and /advancedSearch
 */
export const FETCH_SEARCH_REQUEST = "MSS/FETCH_SEARCH_REQUEST";
export const fetchSearchRequest = (payload, advancedSearch) => ({
  type: FETCH_SEARCH_REQUEST,
  payload,
  advancedSearch
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

/**
 * API Actions /suggest
 */
export const FETCH_SUGGEST_REQUEST = "MSS/FETCH_SUGGEST_REQUEST";
export const fetchSuggestRequest = ({
  s,
  field,
  count = config.DEFAULTS.suggestionCount
}) => ({
  type: FETCH_SUGGEST_REQUEST,
  payload: {
    s,
    field,
    count
  }
});

export const FETCH_SUGGEST_SUCCESS = "MSS/FETCH_SUGGEST_SUCCESS";
export const fetchSuggestSuccess = ({ field, data }) => ({
  type: FETCH_SUGGEST_SUCCESS,
  payload: {
    field,
    data,
    timeFetched: new Date()
  }
});

export const FETCH_SUGGEST_FAILURE = "MSS/FETCH_SUGGEST_FAILURE";
export const fetchSuggestFailure = ({ field, error }) => ({
  type: FETCH_SUGGEST_FAILURE,
  payload: {
    field,
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

export const DESELECT_FACET = "MSS/DESELECT_FACET";
export const deselectFacet = facet => ({
  type: DESELECT_FACET,
  payload: {
    facet
  }
});

export const DESELECT_FACETS = "MSS/DESELECT_FACETS";
export const deselectFacets = facet => ({
  type: DESELECT_FACETS
});

export const UPDATE_SEARCH_INPUT = "MSS/UPDATE_SEARCH_INPUT";
export const updateSearchInput = update => ({
  type: UPDATE_SEARCH_INPUT,
  payload: {
    update
  }
});

export const TOGGLE_ADVANCED_SEARCH = "MSS/TOGGLE_ADVANCED_SEARCH";
export const toggleAdvancedSearch = value => ({
  type: TOGGLE_ADVANCED_SEARCH,
  payload: {
    value
  }
});
