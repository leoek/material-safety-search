import React, { Component } from "react";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/core/styles";
import classnames from "classnames";

const styles = theme => ({
  padder: {
    height: theme.spacing.unit * 2
  }
});

const Padder = ({ height, classes }) => (
  <div
    style={!!height ? { height } : undefined}
    className={classnames({ [classes.padder]: !height })}
  />
);

Padder.propTypes = {
  classes: PropTypes.object.isRequired,
  height: PropTypes.number
};

Padder.defaultProps = {
  height: null
};

export default withStyles(styles)(Padder);
