//Form values
export const getForm = (formName = "default") => state => state.form[formName];
export const getFormValues = formName => state =>
  (getForm(formName)(state) || {}).values;
export const getSearchFormValues = state => getFormValues("search")(state);
