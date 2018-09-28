import get from "lodash/get";

/**
 * Facet Selection
 * TODO reafactor this, its a fast but hacky solution
 */

export const getUiSelectedFacets = state => get(state, `ui.facets`, {});
export const getUiSelectedFsgFacet = state =>
  getUiSelectedFacets(state).fsgFacet;

/**
 * advancedSearch
 */

export const getAdvancedSearch = state => get(state, `ui.advancedSearch`);

/**
 * datasheetSectionDialog substate
 */
export const getDatasheetSectionDialogValue = path => state =>
  get(state, `ui.datasheetSectionDialog.${path}`);
export const getDatasheetSectionDialogIsOpen = getDatasheetSectionDialogValue(
  "open"
);
export const getDatasheetSectionDialogName = getDatasheetSectionDialogValue(
  "section.name"
);
export const getDatasheetSectionDialogDataKey = getDatasheetSectionDialogValue(
  "section.dataKey"
);
export const getDatasheetSectionDialogDatasheet = getDatasheetSectionDialogValue(
  "datasheet"
);
export const getDatasheetSectionDialogRaw = state => {
  const dataKey = getDatasheetSectionDialogDataKey(state);
  const rawSection = getDatasheetSectionDialogValue(`datasheet.${dataKey}`)(
    state
  );
  return rawSection;
};

/**
 * datasheetDialog substate
 */

export const getDatasheetDialogValue = path => state =>
  get(state, `ui.datasheetDialog.${path}`);
export const getDatasheetDialogIsOpen = getDatasheetDialogValue("open");
export const getDatasheetDialogDatasheet = getDatasheetDialogValue("datasheet");
