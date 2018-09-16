import { all, put, takeLatest, takeEvery } from "redux-saga/effects";
import { REHYDRATE } from "redux-persist";
import {
  reduxRehydrationCompleted,
  FETCH_SEARCH_REQUEST,
  fetchSearchSuccess
} from "../actions";
import { get } from "../../lib/api";
import { config } from "../../config";

export function* reduxRehydrateSaga(action) {
  yield put(reduxRehydrationCompleted());
}

export function* fetchSearchSaga(action) {
  const { payload = {} } = action;
  const { query, page = 0, size = config.DEFAULTS.pageSize } = payload;
  const parameters = {
    s: query,
    page,
    size
  };
  const response = yield get({
    endpoint: "search",
    parameters
  });
  const data = yield response.json();
  yield put(fetchSearchSuccess(data));
}

export default function* root() {
  yield all([
    takeLatest(REHYDRATE, reduxRehydrateSaga),
    takeEvery(FETCH_SEARCH_REQUEST, fetchSearchSaga)
  ]);
}
