import React from "react";
import TextField from "@material-ui/core/TextField";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import Checkbox from "@material-ui/core/Checkbox";

export const RenderTextField = ({
  input,
  meta: { touched, error },
  ...rest
}) => <TextField {...rest} {...input} error={touched && error} />;

export const RenderCheckbox = ({
  label,
  input,
  meta: { touched, error },
  ...rest
}) => (
  <FormControlLabel
    control={<Checkbox {...rest} {...input} value="checkedB" color="primary" />}
    label={label}
  />
);
