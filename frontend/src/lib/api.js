import config from "../config";

const getErrorPromise = error => {
  console.warn("Handled Connection Error", error);
  return new Promise((resolve, reject) => {
    resolve({
      ok: false,
      json: () =>
        new Promise((resolve, reject) => {
          resolve(error);
        })
    });
  });
};

const handleFetchErrors = (errorMessage, statusCode) => {
  const error = {
    statusCode: 503,
    devMessage: "An error ocurred while trying to connect to the API.",
    error: config.ERROR.NOCONNECTION
  };
  return getErrorPromise(error);
};

const getQueryFromParams = (parameters = {}) =>
  Object.keys(parameters || {}).reduce((result, parameter) => {
    if (!parameter || !parameters[parameter]) return result;
    return result === ""
      ? `${parameter}=${parameters[parameter]}`
      : `${result}&${parameter}=${parameters[parameter]}`;
  }, "");

const getHeaders = () => {
  const headers = {
    "Content-Type": "application/json; charset=utf-8",
    "x-client": "react-frontend",
    "x-client-version": config.version
  };
  return new Headers(headers);
};

export const get = async request => {
  const { endpoint, parameters } = request;
  const query = getQueryFromParams(parameters);
  const headers = getHeaders();
  return fetch(`${config.apiBaseUrl}/${endpoint}?${query}`, {
    method: "get",
    headers
  }).catch(handleFetchErrors);
};
