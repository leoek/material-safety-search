import get from "lodash/get";

export const getSearchInput = state => get(state, `searchInput`);
export const getSearchInputSelectedFacet = type => state => {
  const searchInput = getSearchInput(state);
  return get(searchInput, type, null);
};
