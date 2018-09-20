const devEnv = process.env.NODE_ENV === "development";

const devConfig = {
  apiBaseUrl: "http://localhost:8080"
};

export const baseConfig = {
  version: "0.1.0",
  apiBaseUrl: process.env.API_BASE_URL || "https://api.mss.leoek.tech",
  ERROR: {
    NOCONNECTION: "NOCONNECTION",
    UNPARSABLE_RESPONSE: "UNPARSABLE_RESPONSE"
  },
  DEFAULTS: {
    pageSize: 10
  }
};

export const config = {
  ...baseConfig,
  ...(devEnv ? devConfig : {}),
  devEnv
};

export default config;
