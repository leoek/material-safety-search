export const datasheetFormat = {
  translationKeyPrefix: "datasheet",
  oldSnippetPreview: [
    [
      {
        name: "fsg",
        dataKey: ["fsg", "fsgString"],
        betweenValues: " - "
      }
    ],
    [
      {
        name: "company",
        dataKey: "companyName"
      }
    ]
  ],
  snippetPreview: [
    {
      name: "fsg",
      dataKey: "fsg",
      betweenValues: " - ",
      ariaDataKey: ["fsg", "fsgString"]
    },
    {
      name: "fsc",
      dataKey: "fsc",
      betweenValues: " - ",
      ariaDataKey: ["fsc", "fscString"]
    },
    {
      name: "niin",
      dataKey: "niin"
    }
  ],
  snippetProperties: [
    {
      name: "niin",
      dataKey: "niin"
    },
    {
      name: "company",
      dataKey: "companyName"
    },
    {
      name: "fsg",
      dataKey: ["fsg", "fsgString"],
      betweenValues: " - "
    },
    {
      name: "fsc",
      dataKey: ["fsc", "fscString"],
      betweenValues: " - "
    }
  ],
  sections: [
    {
      name: "Identification",
      dataKey: "rawIdentification"
    },
    {
      name: "Composition",
      dataKey: "rawComposition"
    },
    {
      name: "Hazards",
      dataKey: "rawHazards"
    },
    {
      name: "FirstAid",
      dataKey: "rawFirstAid"
    },
    {
      name: "FireFighting",
      dataKey: "rawFireFighting"
    },
    {
      name: "AcidentalRelease",
      dataKey: "rawAccidentalRelease"
    },
    {
      name: "HandlingStorage",
      dataKey: "rawHandlingStorage"
    },
    {
      name: "Protection",
      dataKey: "rawProtection"
    },
    {
      name: "ChemicalProperties",
      dataKey: "rawChemicalProperties"
    },
    {
      name: "StabilityReactivity",
      dataKey: "rawStabilityReactivity"
    },
    {
      name: "Toxic",
      dataKey: "rawToxic"
    },
    {
      name: "Eco",
      dataKey: "rawEco"
    },
    {
      name: "Disposal",
      dataKey: "rawDisposal"
    },
    {
      name: "Transport",
      dataKey: "rawTransport"
    },
    {
      name: "Regulatory",
      dataKey: "rawRegulatory"
    },
    {
      name: "Other",
      dataKey: "rawOther"
    }
  ]
};

export const ingredientFormat = {};

export default datasheetFormat;
