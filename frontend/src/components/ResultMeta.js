import React, { Component } from "react";
import { connect } from "react-redux";
import { compose } from "redux";
import PropTypes from "prop-types";
import { translate } from "react-i18next";
import { withStyles } from "@material-ui/core/styles";
import classnames from "classnames";
import withWidth from "@material-ui/core/withWidth";
import { getSearchIsFetching, getSearchMeta } from "../redux/selectors";

const styles = theme => ({
  root: {
    flexGrow: 1
  },
  left: {
    display: "flex",
    justifyContent: "flex-end"
  },
  padder: {
    paddingLeft: theme.spacing.unit * 3,
    paddingRight: theme.spacing.unit * 3
  }
});

export class ResultMeta extends Component {
  render() {
    const { classes, meta, t, width } = this.props;
    const { totalCount } = meta || {};

    if (!totalCount) return null;

    const padderWidths = ["xs"];
    const isPadder = padderWidths.includes(width);

    return (
      <div
        className={classnames({
          [classes.root]: true,
          [classes.left]: true,
          [classes.padder]: isPadder
        })}
      >
        {`${parseInt(totalCount, 10).toLocaleString()} ${t(
          "totalresultcountlbl"
        )}`}
      </div>
    );
  }
}

ResultMeta.propTypes = {
  classes: PropTypes.object.isRequired,
  t: PropTypes.func.isRequired,
  isFetching: PropTypes.bool,
  meta: PropTypes.object
};

ResultMeta.defaultProps = {
  isFetching: false,
  meta: {}
};

const mapStateToProps = state => {
  return {
    isFetching: getSearchIsFetching(state),
    meta: getSearchMeta(state)
  };
};

export default compose(
  connect(
    mapStateToProps,
    null
  ),
  withWidth(),
  withStyles(styles),
  translate()
)(ResultMeta);
