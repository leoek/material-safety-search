import config from "../../config";
import {
  SHOW_DATASHEET_SECTION,
  CLOSE_DATASHEET_SECTION,
  SHOW_DATASHEET,
  CLOSE_DATASHEET,
  TOGGLE_ADVANCED_SEARCH
} from "../actions";

const initialState = {
  advancedSearch: config.DEFAULTS.advancedSearchIsDefault,
  notifications: {
    current: null,
    toShow: []
  },
  datasheetSectionDialog: {
    open: false,
    datasheet: null,
    section: null
  },
  datasheetDialog: {
    open: false,
    datasheet: null
  }
};

const ui = (state = initialState, action) => {
  const { type, payload } = action;
  if (type === SHOW_DATASHEET_SECTION) {
    const { datasheet, section } = payload || {};
    return {
      ...state,
      datasheetSectionDialog: {
        open: true,
        datasheet,
        section
      }
    };
  } else if (type === CLOSE_DATASHEET_SECTION) {
    return {
      ...state,
      datasheetSectionDialog: {
        open: false
      }
    };
  } else if (type === SHOW_DATASHEET) {
    const { datasheet } = payload || {};
    return {
      ...state,
      datasheetDialog: {
        open: true,
        datasheet
      }
    };
  } else if (type === CLOSE_DATASHEET) {
    return {
      ...state,
      datasheetDialog: {
        open: false
      }
    };
  } else if (type === TOGGLE_ADVANCED_SEARCH) {
    const { value } = payload || {};
    return {
      ...state,
      advancedSearch: value === undefined ? !state.advancedSearch : !!value
    };
  }
  return state;
};

export default ui;
