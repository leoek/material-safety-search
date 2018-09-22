import get from "lodash/get";

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
