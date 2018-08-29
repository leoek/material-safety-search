export const config = {
  apiBaseUrl:
    process.env.NODE_ENV === "development"
      ? "http://localhost:8080"
      : "https://api.mss.leoek.eu",
  ERROR: {
    NOCONNECTION: "NOCONNECTION"
  }
};

export default config;
