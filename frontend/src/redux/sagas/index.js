import { all, put, takeLatest, takeEvery } from "redux-saga/effects";
import { REHYDRATE } from "redux-persist";
import { reduxRehydrationCompleted, FETCH_SEARCH_REQUEST } from "../actions";
import { get } from "../../lib/api";

export function* reduxRehydrateSaga(action) {
  yield put(reduxRehydrationCompleted());
}

export function* fetchSearchSaga(action) {
  const {
    payload: { searchField }
  } = action;
  const response = yield get({
    endpoint: "search",
    parameters: {
      s: searchField
    }
  });
  const data = yield response.json();
  console.log(data);
  yield put({ type: "test", paload: { data } });
}

export default function* root() {
  yield all([
    takeLatest(REHYDRATE, reduxRehydrateSaga),
    takeEvery(FETCH_SEARCH_REQUEST, fetchSearchSaga)
  ]);
}
