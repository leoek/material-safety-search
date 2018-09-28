import get from "lodash/get";

export const getSuggestValue = value => field => state =>
  get(state, `suggest.${field}.${value}`);

export const getSuggestions = getSuggestValue("suggestions");
