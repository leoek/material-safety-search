export const config = {
  version: "0.1.0",
  apiBaseUrl:
    process.env.NODE_ENV === "development"
      ? "http://localhost:8080"
      : "https://api.mss.leoek.tech",
  ERROR: {
    NOCONNECTION: "NOCONNECTION",
    UNPARSABLE_RESPONSE: "UNPARSABLE_RESPONSE"
  },
  DEFAULTS: {
    pageSize: 10
  }
};

export default config;
