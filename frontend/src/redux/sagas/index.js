import { all, put, takeLatest, takeEvery, select } from "redux-saga/effects";
import { REHYDRATE } from "redux-persist";
import {
  reduxRehydrationCompleted,
  FETCH_SEARCH_REQUEST,
  fetchSearchSuccess,
  fetchSearchFailure,
  UPDATE_SEARCH_INPUT,
  updateSearchInput,
  fetchSearchRequest,
  SELECT_FACET,
  DESELECT_FACET,
  REDUX_FORM_SUBMIT,
  REDUX_FORM_SUBMIT_SUCCEEDED,
  deselectFacets,
  DESELECT_FACETS,
  FETCH_SUGGEST_REQUEST,
  fetchSuggestSuccess,
  fetchSuggestFailure
} from "../actions";
import { getSearchInput, getAdvancedSearch } from "../selectors";
import { post, get } from "../../lib/api";
import { config } from "../../config";

export function* reduxRehydrateSaga(action) {
  yield put(reduxRehydrationCompleted());
}

/**
 * redux-saga expects that a yielded promise will be resolved,
 * therefore we catch any parsing errors here, create our custom
 * error object and resolve the promise anyways.
 * We cannot let the app crash because of a malformed response.
 */
const handleResponseJsonError = (errorMessage, statusCode) => {
  return new Promise(resolve => {
    const error = {
      error: config.ERROR.UNPARSABLE_RESPONSE
    };
    resolve(error);
  });
};

export function* fetchSuggestSaga(action) {
  const { payload = {} } = action;
  const { field, s, count } = payload;
  const parameters = {
    field,
    count,
    s
  };
  const response = yield get({
    endpoint: "suggest",
    parameters
  });
  const reponseData = yield response.json().catch(handleResponseJsonError);
  if (response.ok) {
    yield put(fetchSuggestSuccess(reponseData));
  } else {
    yield put(fetchSuggestFailure(reponseData));
  }
}

export function* fetchSearchSaga(action) {
  const { payload = {}, advancedSearch } = action;
  const { query, page = 0, size = config.DEFAULTS.pageSize, ...rest } = payload;
  const parameters = {
    page,
    size
  };
  const data = {
    searchTerm: query,
    ...rest
  };
  const response = yield post({
    endpoint: advancedSearch ? "advancedSearch" : "search",
    parameters,
    data
  });
  const reponseData = yield response.json().catch(handleResponseJsonError);
  if (response.ok) {
    yield put(fetchSearchSuccess(reponseData));
  } else {
    yield put(fetchSearchFailure(reponseData));
  }
}

export function* updateSearchInputSaga(action) {
  const { payload } = action;
  const { update } = payload || {};
  const oldSearchInput = yield select(getSearchInput);
  const advancedSearch = yield select(getAdvancedSearch);
  const searchInput = {
    ...oldSearchInput,
    ...update
  };
  yield put(fetchSearchRequest(searchInput, advancedSearch));
}

export function* handleSelectFacetSaga(action) {
  const { payload, type } = action;
  const { facet } = payload || {};
  const update = {
    [facet.type]: type === SELECT_FACET ? facet.facetNumber : null
  };
  yield put(updateSearchInput(update));
}

export function* handleDeselectFacetsSaga(action) {
  yield put(
    updateSearchInput({
      fsgFacet: null,
      fscFacet: null
    })
  );
}

export function* handleSubmitSaga(action) {
  const { meta, error } = action;
  const { form } = meta || {};
  if (form === "search" && !error) {
    yield put(deselectFacets());
  }
}

export default function* root() {
  yield all([
    takeLatest(REHYDRATE, reduxRehydrateSaga),
    takeEvery(FETCH_SEARCH_REQUEST, fetchSearchSaga),
    takeEvery(FETCH_SUGGEST_REQUEST, fetchSuggestSaga),
    takeEvery(UPDATE_SEARCH_INPUT, updateSearchInputSaga),
    takeEvery([SELECT_FACET, DESELECT_FACET], handleSelectFacetSaga),
    takeEvery(DESELECT_FACETS, handleDeselectFacetsSaga),
    takeLatest(
      [REDUX_FORM_SUBMIT, REDUX_FORM_SUBMIT_SUCCEEDED],
      handleSubmitSaga
    )
  ]);
}
