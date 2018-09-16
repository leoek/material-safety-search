export const config = {
  version: "0.1.0",
  apiBaseUrl:
    process.env.NODE_ENV === "development"
      ? "http://localhost:8080"
      : "https://api.mss.leoek.eu",
  ERROR: {
    NOCONNECTION: "NOCONNECTION"
  }
};

export default config;
