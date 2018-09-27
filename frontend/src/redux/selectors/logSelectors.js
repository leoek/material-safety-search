import get from "lodash/get";

/* eslint-disable no-unused-vars */
const getLog = state => get(state, `log`);
const getLogValue = path => state => get(state, `log.${path}`);
export const getLogPrevSearch = getLogValue("prevSearch");
export const getLogIp = getLogValue("ip");
export const getLogSession = getLogValue("session");
