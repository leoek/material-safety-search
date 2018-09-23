import get from "lodash/get";

export const getSearchInput = state => get(state, `searchInput`);
