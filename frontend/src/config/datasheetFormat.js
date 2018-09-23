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
      name: "AcidentalRelease",
      dataKey: "rawAccidentalRelease"
    },
    {
      name: "ChemicalProperties",
      dataKey: "rawChemicalProperties"
    },
    {
      name: "Composition",
      dataKey: "rawComposition"
    },
    {
      name: "Disposal",
      dataKey: "rawDisposal"
    },
    {
      name: "Eco",
      dataKey: "rawEco"
    },
    {
      name: "FireFighting",
      dataKey: "rawFireFighting"
    },
    {
      name: "FirstAid",
      dataKey: "rawFirstAid"
    },
    {
      name: "HandlingStorage",
      dataKey: "rawHandlingStorage"
    },
    {
      name: "Hazards",
      dataKey: "rawHazards"
    },
    {
      name: "Identification",
      dataKey: "rawIdentification"
    },
    {
      name: "Other",
      dataKey: "rawOther"
    },
    {
      name: "Protection",
      dataKey: "rawProtection"
    },
    {
      name: "Regulatory",
      dataKey: "rawRegulatory"
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
      name: "Transport",
      dataKey: "rawTransport"
    }
  ]
};

export const ingredientFormat = {};

export default datasheetFormat;
