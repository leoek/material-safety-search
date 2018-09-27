import React, { Component } from "react";
import { connect } from "react-redux";
import { compose } from "redux";
import PropTypes from "prop-types";
import { translate } from "react-i18next";
import Typography from "@material-ui/core/Typography";
import Card from "@material-ui/core/Card";
import CardContent from "@material-ui/core/CardContent";
import Button from "@material-ui/core/Button";
import LinearProgress from "@material-ui/core/LinearProgress";
import { withStyles } from "@material-ui/core/styles";
import isEmpty from "lodash/isEmpty";
import classnames from "classnames";

import lodashMin from "lodash/min";
import lodashMax from "lodash/max";

import Padder from "./common/Padder";
import SectionSelections from "./SectionSelections";
import Snippet from "./Snippet";

import { fetchSearchRequest, showDatasheet } from "../redux/actions";
import {
  getSearchItems,
  getSearchIsFetching,
  getSearchMeta
} from "../redux/selectors";

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

const LeftButton = ({ classes, page, firstPage, fetchPage }) => (
  <Button
    classes={{ label: classes.buttonLabel }}
    className={classes.button}
    disabled={page <= firstPage}
    onClick={() => fetchPage(page - 1)}
  >
    {"<"}
  </Button>
);
const RightButton = ({ classes, page, lastPage, fetchPage }) => (
  <Button
    classes={{ label: classes.buttonLabel }}
    className={classes.button}
    disabled={page >= lastPage}
    onClick={() => fetchPage(page + 1)}
  >
    {">"}
  </Button>
);
const PageButton = ({ classes, pageNumber, fetchPage }) => (
  <Button
    classes={{ label: classes.buttonLabel }}
    className={classes.button}
    onClick={() => fetchPage(pageNumber)}
  >
    {pageNumber}
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
  const before =
    defaultBefore + correctionBefore + lodashMax([-(correctionAfter + 1), 0]);
  const after =
    defaultAfter + correctionAfter + lodashMax([-(correctionBefore + 1), 0]);

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
    const fetchPage = page => console.log("fetch page", page);
    const { items, isFetching, hideLoading, classes, meta } = this.props;
    const { page, totalPages } = meta;

    const pageButtonProps = {
      fetchPage,
      page,
      totalPages,
      classes,
      firstPage: 0,
      lastPage: totalPages - 1
    };

    return (
      <Card className={classes.paper}>
        <CardContent>
          <div className={classes.center}>
            <LeftButton {...pageButtonProps} />
            <PageButtons {...pageButtonProps} />
            <RightButton {...pageButtonProps} />
          </div>
        </CardContent>
        <Padder />
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
  fetchSearchRequest
};

export default compose(
  connect(
    mapStateToProps,
    mapDispatchToProps
  ),
  withStyles(styles),
  translate()
)(Pagination);
