import config from "../../config";
import {
  SHOW_DATASHEET_SECTION,
  CLOSE_DATASHEET_SECTION,
  SHOW_DATASHEET,
  CLOSE_DATASHEET,
  TOGGLE_ADVANCED_SEARCH,
  SELECT_FACET,
  DESELECT_FACET,
  UPDATE_SEARCH_INPUT
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
  },
  facets: {}
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
  } else if (type === SELECT_FACET) {
    const { facet } = payload || {};
    const { type } = facet || {};
    return {
      ...state,
      facets: {
        ...state.facets,
        ...(type ? { [type]: facet } : {})
      }
    };
  } else if (type === DESELECT_FACET) {
    const { facet } = payload || {};
    const { type } = facet || {};
    return {
      ...state,
      facets: {
        ...state.facets,
        ...(type ? { [type]: null } : {})
      }
    };
  } else if (type === UPDATE_SEARCH_INPUT) {
  /**
   * TODO: fix this it is ugly
   */
    const { update } = payload || {};
    const { fscFacet, fsgFacet } = update;
    return {
      ...state,
      facets: {
        ...state.facets,
        ...(fscFacet === null ? { fscFacet } : {}),
        ...(fsgFacet === null ? { fsgFacet } : {})
      }
    };
  }
  return state;
};

export default ui;
