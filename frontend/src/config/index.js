import datasheetFormat, { ingredientFormat } from "./datasheetFormat";

const devEnv = process.env.NODE_ENV === "development";

const devConfig = {
  //apiBaseUrl: "http://localhost:8080"
  apiBaseUrl: "https://api.mss.leoek.tech"
};

export const baseConfig = {
  version: "1.1.0",
  buildNumber:
    global.MSS && global.MSS.BUILD_NUMBER !== "REPLACE_WITH_BUILD_NUMBER"
      ? global.MSS.BUILD_NUMBER
      : 0,
  apiBaseUrl:
    global.MSS && global.MSS.API_BASE_URL !== "REPLACE_WITH_APIBASEURL"
      ? global.MSS.API_BASE_URL
      : "https://api.mss.leoek.tech",
  ERROR: {
    NOCONNECTION: "NOCONNECTION",
    UNPARSABLE_RESPONSE: "UNPARSABLE_RESPONSE"
  },
  DEFAULTS: {
    page: 0,
    pageSize: 10,
    suggestionCount: 5,
    dateFormat: "LL",
    advancedSearchIsDefault: false
  },
  datasheetFormat,
  ingredientFormat
};

export const config = {
  ...baseConfig,
  ...(devEnv ? devConfig : {}),
  devEnv
};

export default config;
