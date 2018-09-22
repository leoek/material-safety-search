import { SHOW_DATASHEET_SECTION, CLOSE_DATASHEET_SECTION } from "../actions";

const initialState = {
  notifications: {
    current: null,
    toShow: []
  },
  datasheetSectionDialog: {
    open: false,
    datasheet: null,
    section: null
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
  }
  return state;
};

export default ui;
