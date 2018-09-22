import { all, put, takeLatest, takeEvery } from "redux-saga/effects";
import { REHYDRATE } from "redux-persist";
import {
  reduxRehydrationCompleted,
  FETCH_SEARCH_REQUEST,
  fetchSearchSuccess,
  fetchSearchFailure
} from "../actions";
import { get, post } from "../../lib/api";
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
  const { query, page = 0, size = config.DEFAULTS.pageSize } = payload;
  const parameters = {
    page,
    size
  };
  const data = {
    searchTerm: query
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

export default function* root() {
  yield all([
    takeLatest(REHYDRATE, reduxRehydrateSaga),
    takeEvery(FETCH_SEARCH_REQUEST, fetchSearchSaga)
  ]);
}
