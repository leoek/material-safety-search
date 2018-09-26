import React, { Component } from "react";
import PropTypes from "prop-types";
import { compose } from "redux";
import Grid from "@material-ui/core/Grid";
import withWidth from "@material-ui/core/withWidth";
import { withStyles } from "@material-ui/core/styles";
import TextField from "@material-ui/core/TextField";
import { translate } from "react-i18next";
import classnames from "classnames";
import AddIcon from "@material-ui/icons/Add";
import IconButton from "@material-ui/core/IconButton";
import Chip from "@material-ui/core/Chip";
import Avatar from "@material-ui/core/Avatar";
import isEmpty from "lodash/isEmpty";

const styles = theme => ({
  root: {
    flexGrow: 1
  },
  formItem: {
    width: "100%"
  },
  formItemChip: {
    marginTop: 12
  },
  chipLabel: {
    flexGrow: 1,
    maxWidth: "100%",
    overflow: "hidden"
  },
  formItemContainer: {
    paddingTop: theme.spacing.unit,
    paddingBottom: theme.spacing.unit,
    paddingLeft: theme.spacing.unit * 2,
    paddingRight: theme.spacing.unit * 2
  },
  inputContainer: {
    display: "flex"
  },
  grow: {
    display: "flex",
    flexGrow: 1
  },
  button: {
    marginTop: 12
  },
  avatar: {
    color: "white"
  },
  avatar3: {
    fontSize: 12
  }
});

const RawSelectedIngredient = ({ item, classes, deselect }) => {
  if (!item || !deselect) return null;
  let avatarText;
  if (item.cas) {
    avatarText = "CAS";
  } else if (item.ingredName) {
    avatarText = "N";
  } else {
    avatarText = false;
  }
  const avatarTextLength = avatarText && avatarText.length;
  return (
    <Grid
      item
      xs={12}
      sm={6}
      md={6}
      lg={4}
      className={classnames(classes.formItemContainer)}
    >
      <Chip
        avatar={
          avatarText ? (
            <Avatar
              className={classnames(
                classes.avatar,
                classes[`avatar${avatarTextLength}`]
              )}
            >
              {avatarText}
            </Avatar>
          ) : null
        }
        classes={{ label: classes.chipLabel }}
        label={item.ingredName || item.cas || ""}
        className={classnames([classes.formItem, classes.formItemChip])}
        onDelete={deselect}
        color="primary"
      />
    </Grid>
  );
};

export const SelectedIngredient = withStyles(styles)(RawSelectedIngredient);

const casRegex = new RegExp("[0-9]{5}-[0-9]{2}-[0-9]{1}");
export const isCas = value => casRegex.test(value);

const SelectedIngredients = ({
  items = [],
  handleDeselect = (...args) => console.warn("handleDselect", args)
}) => {
  return items.map(item => (
    <SelectedIngredient
      key={item.id}
      item={item}
      deselect={() => handleDeselect(item.id)}
    />
  ));
};

class RawIngredientSelection extends Component {
  state = {
    currentValue: ""
  };

  handleChangeCurrent = value => {
    this.setState({
      currentValue: value
    });
  };

  handleAdd = () => {
    const {
      input: { value = [], onChange = () => false }
    } = this.props;
    const { currentValue } = this.state;
    if (isCas(currentValue)) {
      onChange([...value, { id: value.length, cas: currentValue }]);
    } else {
      onChange([...value, { id: value.length, ingredName: currentValue }]);
    }
    this.setState({
      currentValue: ""
    });
  };

  handleDeselect = id => {
    const {
      input: { value = [], onChange = () => false }
    } = this.props;
    const newValue = value.filter(v => v.id !== id);
    onChange(newValue);
  };

  render() {
    /* eslint-disable no-unused-vars */
    const {
      input: { value, onChange, ...inputRest },
      classes,
      label,
      name
    } = this.props;
    const { currentValue } = this.state;
    /* eslint-enable no-unused-vars */

    const inputs = (value || []).map(v => v.ingredName || v.cas);
    const canAdd =
      currentValue && !isEmpty(currentValue) && !inputs.includes(currentValue);

    return [
      <SelectedIngredients
        key={`${name}selectedIngredients`}
        items={value || []}
        handleDeselect={this.handleDeselect}
      />,
      <Grid
        key={`${name}textInput`}
        item
        xs={12}
        sm={6}
        md={6}
        lg={4}
        className={classnames(classes.formItemContainer)}
      >
        <div className={classnames([classes.inputContainer, classes.grow])}>
          <TextField
            label={label}
            value={currentValue}
            className={classes.grow}
            onChange={event => this.handleChangeCurrent(event.target.value)}
          />
          <IconButton
            className={classes.button}
            aria-label="Delete"
            disabled={!canAdd}
            onClick={this.handleAdd}
            color="primary"
          >
            <AddIcon />
          </IconButton>
        </div>
      </Grid>
    ];
  }
}

RawIngredientSelection.propTypes = {
  classes: PropTypes.object.isRequired,
  t: PropTypes.func.isRequired,
  input: PropTypes.object.isRequired
};

RawIngredientSelection.defaultProps = {};

const IngredientSelection = compose(
  withWidth(),
  withStyles(styles),
  translate()
)(RawIngredientSelection);

export default IngredientSelection;
