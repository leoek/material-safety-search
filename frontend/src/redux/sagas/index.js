import {
  all,
  put,
  takeLatest,
  takeEvery,
  select,
  take,
  fork
} from "redux-saga/effects";
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
  fetchSuggestFailure,
  SELECT_PAGE,
  RESET_SEARCH_INPUT,
  reduxFormResetSearchForm,
  REPORT_APP_START,
  REDUX_REHYDRATION_COMPLETED,
  reportNewLocalIp
} from "../actions";
import {
  getSearchInput,
  getAdvancedSearch,
  isReduxRehydrationComplete
} from "../selectors";
import { post, get } from "../../lib/api";
import { config } from "../../config";
import { findIP } from "../../lib/localIp";

export function* reduxRehydrateSaga(action) {
  yield put(reduxRehydrationCompleted());
}

export function* appStartSaga(dispatch, action) {
  const rehydrated = yield select(isReduxRehydrationComplete);
  if (!rehydrated) {
    yield take(REDUX_REHYDRATION_COMPLETED);
  }
  yield fork(() =>
    findIP((ip, localIps) => dispatch(reportNewLocalIp(ip, localIps)))
  );
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
    yield put(fetchSuggestSuccess({ field, data: reponseData }));
  } else {
    yield put(fetchSuggestFailure({ field, error: reponseData }));
  }
}

export function* fetchSearchSaga(action) {
  let { searchInput, meta, advancedSearch } = action;
  if (!searchInput) {
    searchInput = yield select(getSearchInput) || {};
  }
  if (!advancedSearch) {
    advancedSearch = yield select(getAdvancedSearch) ||
      config.DEFAULTS.advancedSearchIsDefault;
  }
  if (!meta) {
    meta = {
      page: config.DEFAULTS.page,
      size: config.DEFAULTS.pageSize
    };
  }
  const { page = config.DEFAULTS.page, size = config.DEFAULTS.pageSize } = meta;
  const { query, ingredients, ...rest } = searchInput;
  const parameters = {
    page,
    size
  };
  const data = {
    searchTerm: query,
    ingredients:
      ingredients && Array.isArray(ingredients)
        ? ingredients.map(ingr => ({
            ingredName: ingr.ingredName,
            cas: ingr.cas
          }))
        : undefined,
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
  yield put(fetchSearchRequest({ searchInput, advancedSearch }));
}

export function* resetSearchInputSaga(action) {
  const advancedSearch = yield select(getAdvancedSearch);
  yield put(reduxFormResetSearchForm());
  yield put(fetchSearchRequest({ advancedSearch }));
}

export function* updateSearchMetaSaga(action) {
  const { type, payload } = action;
  if (type === SELECT_PAGE) {
    yield put(fetchSearchRequest({ meta: payload }));
  }
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

export default function* root(dispatch) {
  yield all([
    takeLatest(REHYDRATE, reduxRehydrateSaga),
    takeLatest(REPORT_APP_START, appStartSaga, dispatch),
    takeEvery(FETCH_SEARCH_REQUEST, fetchSearchSaga),
    takeEvery(FETCH_SUGGEST_REQUEST, fetchSuggestSaga),
    takeEvery(UPDATE_SEARCH_INPUT, updateSearchInputSaga),
    takeEvery(RESET_SEARCH_INPUT, resetSearchInputSaga),
    takeEvery(SELECT_PAGE, updateSearchMetaSaga),
    takeEvery([SELECT_FACET, DESELECT_FACET], handleSelectFacetSaga),
    takeEvery(DESELECT_FACETS, handleDeselectFacetsSaga),
    takeLatest(
      [REDUX_FORM_SUBMIT, REDUX_FORM_SUBMIT_SUCCEEDED],
      handleSubmitSaga
    )
  ]);
}
