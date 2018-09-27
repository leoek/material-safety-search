import uuidv1 from "uuid/v1";
import {
  REPORT_NEW_LOCAL_IP,
  FETCH_SEARCH_REQUEST,
  FETCH_SEARCH_SUCCESS,
  SHOW_DATASHEET_SECTION,
  SHOW_DATASHEET,
  CLOSE_DATASHEET_SECTION,
  CLOSE_DATASHEET,
  START_NEW_SESSION
} from "../actions";

const initialState = {
  ip: null,
  localIps: null,
  session: "some session id",
  search: {
    query: null,
    requestStart: null,
    start: null,
    end: null,
    resultClicks: 0,
    anyResults: null,
    resultCount: null,
    dwellTimes: [],
    page: 0
  },
  current: {},
  prevSearch: null
};

const log = (state = initialState, action) => {
  const { type, payload = {} } = action;
  if (type === REPORT_NEW_LOCAL_IP) {
    const { ip, localIps } = payload || {};
    return {
      ...state,
      ip,
      localIps
    };
  } else if (type === FETCH_SEARCH_REQUEST) {
    const { searchInput = {} } = action;
    const { query } = searchInput;
    return {
      ...state,
      search: {
        ...initialState.search,
        requestStart: new Date(),
        query
      },
      prevSearch: {
        ...state.search,
        end: new Date()
      }
    };
  } else if (type === FETCH_SEARCH_SUCCESS) {
    const { data = {}, timeFetched } = payload;
    const { meta = {} } = data;
    const { totalCount, page } = meta;
    return {
      ...state,
      search: {
        ...state.search,
        anyResults: totalCount > 0,
        resultCount: totalCount,
        start: timeFetched,
        page
      }
    };
  } else if (type === START_NEW_SESSION) {
    const { session = uuidv1() } = payload;
    return {
      ...state,
      session
    };
  } else if (type === SHOW_DATASHEET_SECTION) {
    const { datasheet = {}, section } = payload;
    const { id } = datasheet;
    const { dataKey } = section;
    return {
      ...state,
      current: {
        ...state.current,
        datasheetsection: {
          datasheetId: id,
          start: new Date(),
          section: dataKey
        }
      }
    };
  } else if (type === SHOW_DATASHEET) {
    const { datasheet } = payload;
    const { id } = datasheet || {};
    return {
      ...state,
      current: {
        ...state.current,
        datasheet: {
          datasheetId: id,
          start: new Date(),
          section: "all"
        }
      }
    };
  } else if (type === CLOSE_DATASHEET_SECTION) {
    const { datasheetsection } = state.current || {};
    let { dwellTimes = [] } = state.search || {};
    if (datasheetsection && datasheetsection.start) {
      dwellTimes = [...dwellTimes, { ...datasheetsection, end: new Date() }];
    }
    return {
      ...state,
      search: {
        ...state.search,
        dwellTimes
      }
    };
  } else if (type === CLOSE_DATASHEET) {
    const { datasheet } = state.current || {};
    let { dwellTimes = [] } = state.search || {};
    if (datasheet && datasheet.start) {
      dwellTimes = [...dwellTimes, { ...datasheet, end: new Date() }];
    }
    return {
      ...state,
      search: {
        ...state.search,
        dwellTimes
      }
    };
  }
  return state;
};

export default log;
