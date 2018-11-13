import React from "react";
import { connect } from "react-redux";
import { compose } from "redux";
import TextField from "@material-ui/core/TextField";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import Checkbox from "@material-ui/core/Checkbox";
import { Field } from "redux-form";
import { withStyles } from "@material-ui/core/styles";

import { fetchSuggestRequest } from "../../redux/actions";
import { getSuggestions } from "../../redux/selectors";
import AutoSuggest, { formatSuggestions } from "./AutoSuggest";

const styles = theme => ({
  formItem: {
    width: "100%"
  }
});

export const RenderTextField = ({
  input,
  meta: { touched, error },
  ...rest
}) => <TextField {...rest} {...input} error={touched && error} />;

const RawInputTextField = ({ label, name, classes }) => (
  <Field
    label={label}
    name={name}
    component={RenderTextField}
    className={classes.formItem}
  />
);

export const InputTextField = withStyles(styles)(RawInputTextField);

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

export const InputCheckbox = ({ label, name }) => (
  <Field label={label} name={name} component={RenderCheckbox} type="checkbox" />
);

const RawInputAutoSuggest = ({
  label,
  name,
  count,
  suggestions,
  classes,
  fetchSuggestRequest
}) => (
  <Field
    label={label}
    name={name}
    component={AutoSuggest}
    className={classes.formItem}
    suggestions={formatSuggestions(suggestions || [])}
    handleFetchSuggestions={value =>
      fetchSuggestRequest({
        s: value,
        field: name,
        count
      })
    }
    handleClearSuggestions={() => false}
  />
);

export const InputAutoSuggest = compose(
  connect(
    (state, ownProps) => {
      const { name } = ownProps;
      return {
        suggestions: getSuggestions(name)(state)
      };
    },
    { fetchSuggestRequest }
  ),
  withStyles(styles)
)(RawInputAutoSuggest);
