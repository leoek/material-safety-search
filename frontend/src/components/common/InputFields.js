import React from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { compose } from "redux";
import TextField from "@material-ui/core/TextField";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import Checkbox from "@material-ui/core/Checkbox";
import { Field } from "redux-form";
import { withStyles } from "@material-ui/core/styles";
import DatePicker from "material-ui-pickers/DatePicker";

import { fetchSuggestRequest } from "../../redux/actions";
import { getSuggestions } from "../../redux/selectors";
import AutoSuggest, { formatSuggestions } from "./AutoSuggest";
import IngredientSelection from "./IngredientSelection";
import config from "../../config";

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
  fetchSuggestRequest,
  options
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
        count,
        options
      })
    }
    handleClearSuggestions={() => false}
  />
);

export const InputAutoSuggest = compose(
  connect(
    (state, ownProps) => {
      const { name, suggestionsSelector } = ownProps;
      return {
        suggestions: suggestionsSelector
          ? suggestionsSelector(state)
          : getSuggestions(name)(state)
      };
    },
    { fetchSuggestRequest }
  ),
  withStyles(styles)
)(RawInputAutoSuggest);

export const RenderDatePicker = ({
  input: { value, onChange },
  meta: { touched, error },
  classes,
  dateFormat,
  ...rest
}) => {
  return (
    <DatePicker
      {...rest}
      value={value}
      onChange={onChange}
      format={dateFormat}
    />
  );
};

const RawInputDatePicker = ({ classes, ...rest }) => (
  <Field
    component={RenderDatePicker}
    className={classes.formItem}
    classes={classes}
    {...rest}
  />
);

RawInputDatePicker.proptypes = {
  clearable: PropTypes.bool,
  emptyLabel: PropTypes.string,
  invalidDateMessage: PropTypes.node,
  invalidLabel: PropTypes.string,
  dateFormat: PropTypes.string
};

RawInputDatePicker.defaultProps = {
  clearable: true,
  emptyLabel: "",
  invalidDateMessage: undefined,
  invalidLabel: "",
  dateFormat: config.DEFAULTS.dateFormat
};

export const InputDatePicker = withStyles(styles)(RawInputDatePicker);

const RawInputIngredienSelection = ({ classes, ...rest }) => (
  <Field
    component={IngredientSelection}
    className={classes.formItem}
    {...rest}
  />
);

export const InputIngredientSelection = withStyles(styles)(
  RawInputIngredienSelection
);
