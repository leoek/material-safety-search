import get from "lodash/get";

const getSearchValue = path => state => get(state, `search.${path}`);
export const getSearchItems = getSearchValue("items");
export const getSearchMeta = getSearchValue("meta");
export const getSearchIsFetching = getSearchValue("isFetching");
export const getSearchTimeFetched = getSearchValue("timeFetched");
export const getSearchFacets = getSearchValue("facets");
