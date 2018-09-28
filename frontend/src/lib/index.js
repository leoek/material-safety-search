export const getDefaultInputValues = isAdvancedSearch => {
  const both = {
    fuzzy: false
  };
  if (isAdvancedSearch) {
    return {
      ...both,
      productId: null,
      fsgString: null,
      fscString: null,
      niin: null,
      companyName: null,
      beginDate: null,
      endDate: null,
      ingredients: null
    };
  }
  return {
    ...both,
    query: "",
    wholeDoc: false
  };
};
