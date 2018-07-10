import React from "react";
import ReactDOM from "react-dom";
import App from "./App";
import { shallow } from "enzyme";

it.skip("renders without crashing", () => {
  shallow(<App />);
});
