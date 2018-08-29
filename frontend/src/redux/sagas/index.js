import { all, put, takeLatest } from "redux-saga/effects";
import { REHYDRATE } from "redux-persist";
import { reduxRehydrationCompleted } from "../actions";

export function* reduxRehydrateSaga(action) {
  yield put(reduxRehydrationCompleted());
}

export default function* root() {
  yield all([takeLatest(REHYDRATE, reduxRehydrateSaga)]);
}
