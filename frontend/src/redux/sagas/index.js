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
  SELECT_FACET
} from "../actions";
import { getSearchInput } from "../selectors";
import { post } from "../../lib/api";
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

export function* fetchSearchSaga(action) {
  const { payload = {} } = action;
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
    endpoint: "search",
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
  const searchInput = {
    ...oldSearchInput,
    ...update
  };
  yield put(fetchSearchRequest(searchInput));
}

export function* handleSelectFacetSaga(action) {
  const { payload } = action;
  const { facet } = payload || {};
  const update = {
    [facet.type]: facet.facetNumber
  };
  yield put(updateSearchInput(update));
}

export default function* root() {
  yield all([
    takeLatest(REHYDRATE, reduxRehydrateSaga),
    takeEvery(FETCH_SEARCH_REQUEST, fetchSearchSaga),
    takeEvery(UPDATE_SEARCH_INPUT, updateSearchInputSaga),
    takeEvery(SELECT_FACET, handleSelectFacetSaga)
  ]);
}
