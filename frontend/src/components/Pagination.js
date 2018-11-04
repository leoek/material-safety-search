import React, { Component } from "react";
import { connect } from "react-redux";
import { compose } from "redux";
import PropTypes from "prop-types";
import { translate } from "react-i18next";
import Card from "@material-ui/core/Card";
import CardContent from "@material-ui/core/CardContent";
import Button from "@material-ui/core/Button";
import { withStyles } from "@material-ui/core/styles";
import classnames from "classnames";

import lodashMin from "lodash/min";
import lodashMax from "lodash/max";

import { selectPage } from "../redux/actions";
import { getSearchIsFetching, getSearchMeta } from "../redux/selectors";

const styles = theme => ({
  root: {
    flexGrow: 1
  },
  paper: {
    marginTop: theme.spacing.unit * 2,
    marginBottom: theme.spacing.unit * 2
  },
  link: {
    fontSize: 10
  },
  loadingContainer: {
    display: "block",
    height: 5,
    justifyContent: "center",
    alignItems: "center"
  },
  padder: {
    margin: 5
  },
  padLeft: {
    paddingLeft: theme.spacing.unit * 2
  },
  padRight: {
    paddingRight: theme.spacing.unit * 2
  },
  button: {
    margin: theme.spacing.unit * 0.25,
    minWidth: 0
  },
  omittedButton: {
    color: theme.palette.text.primary,
    paddingLeft: 0,
    paddingRight: 0
  },
  buttonLabel: {},
  omittedButtonLabel: {
    color: theme.palette.text.primary
  },
  center: {
    flexGrow: 1,
    display: "flex",
    alignItems: "center",
    justifyContent: "center"
  }
});

const LeftButton = ({ classes, page, firstPage, selectPage }) => (
  <Button
    classes={{ label: classes.buttonLabel }}
    className={classes.button}
    disabled={page <= firstPage}
    onClick={() => selectPage(page - 1)}
  >
    {"<"}
  </Button>
);
const RightButton = ({ classes, page, lastPage, selectPage }) => (
  <Button
    classes={{ label: classes.buttonLabel }}
    className={classes.button}
    disabled={page >= lastPage}
    onClick={() => selectPage(page + 1)}
  >
    {">"}
  </Button>
);
const PageButton = ({ classes, pageNumber, selectPage, page }) => (
  <Button
    variant={page === pageNumber ? "outlined" : "text"}
    classes={{ label: classes.buttonLabel }}
    className={classnames({
      [classes.button]: true,
      [classes.activeButton]: page === pageNumber
    })}
    onClick={() => selectPage(pageNumber)}
  >
    {pageNumber + 1}
  </Button>
);
const OmittedPagesButton = ({ classes }) => (
  <Button
    classes={{ label: classes.omittedButtonLabel }}
    className={classnames([classes.button, classes.omittedButton])}
    disabled
  >
    {"..."}
  </Button>
);

const PageButtons = props => {
  const {
    classes,
    firstPage,
    lastPage,
    page,
    defaultBefore = 2,
    defaultAfter = 2
  } = props;
  const items = [
    <PageButton key={firstPage} {...props} pageNumber={firstPage} />
  ];

  //TODO add some proper testing for this
  const correctionBefore = lodashMin([0, page - defaultBefore - 1]);
  const correctionAfter = lodashMin([0, lastPage - (page + defaultAfter) - 1]);
  const before = defaultBefore + correctionBefore;
  const after = defaultAfter + correctionAfter;

  if (before > 0 && page - before > 1) {
    items.push(<OmittedPagesButton key={"omit1"} classes={classes} />);
  }
  for (let i = -before; i <= after; i++) {
    items.push(<PageButton key={page + i} {...props} pageNumber={page + i} />);
  }
  if (after > 0 && page + after < lastPage - 1) {
    items.push(<OmittedPagesButton key={"omit2"} classes={classes} />);
  }
  if (firstPage !== lastPage) {
    items.push(<PageButton key={lastPage} {...props} pageNumber={lastPage} />);
  }
  return items;
};

export class Pagination extends Component {
  render() {
    const { classes, meta, selectPage } = this.props;
    const { page, totalPages } = meta || {};

    const pageButtonProps = {
      selectPage,
      page,
      totalPages,
      classes,
      firstPage: 0,
      lastPage: totalPages - 1
    };

    if (!totalPages) return null;

    return (
      <Card className={classes.paper}>
        <CardContent style={{ paddingBottom: 16 }}>
          <div className={classes.center}>
            <LeftButton {...pageButtonProps} />
            <PageButtons {...pageButtonProps} />
            <RightButton {...pageButtonProps} />
          </div>
        </CardContent>
      </Card>
    );
  }
}

Pagination.propTypes = {
  classes: PropTypes.object.isRequired,
  t: PropTypes.func.isRequired,
  isFetching: PropTypes.bool,
  meta: PropTypes.object
};

Pagination.defaultProps = {
  isFetching: false,
  meta: {}
};

const mapStateToProps = state => {
  return {
    isFetching: getSearchIsFetching(state),
    meta: getSearchMeta(state)
  };
};

const mapDispatchToProps = {
  selectPage
};

export default compose(
  connect(
    mapStateToProps,
    mapDispatchToProps
  ),
  withStyles(styles),
  translate()
)(Pagination);
