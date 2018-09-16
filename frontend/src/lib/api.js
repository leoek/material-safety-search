import config from "../config";

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
  console.info("request: ", endpoint, parameters, query, headers);
  return fetch(`${config.apiBaseUrl}/${endpoint}?${query}`, {
    method: "get",
    headers
  });
};
