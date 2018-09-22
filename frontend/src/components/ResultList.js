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

import Padder from "./common/Padder";
import SectionSelections from "./SectionSelections";
import Snippet from "./Snippet";

import { fetchSearchRequest } from "../redux/actions";
import { getSearchItems, getSearchIsFetching } from "../redux/selectors";

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
  }
});

export const RawResultListCard = ({ t, item, classes }) => {
  const { productId, companyName } = item;

  const title = `${productId}: ${companyName}`;

  return (
    <Card className={classes.paper}>
      <CardContent>
        <Button>
          <Typography
            variant="headline"
            component="h2"
            className={classes.title}
          >
            {title}
          </Typography>
        </Button>
      </CardContent>
      <Snippet item={item} />
      <SectionSelections item={item} />
      <Padder />
    </Card>
  );
};

RawResultListCard.propTypes = {
  item: PropTypes.object.isRequired,
  classes: PropTypes.object.isRequired,
  t: PropTypes.func.isRequired
};

RawResultListCard.defaultProps = {};

const ResultListCard = translate()(withStyles(styles)(RawResultListCard));

export class ResultList extends Component {
  render() {
    const { items, isFetching, hideLoading, classes } = this.props;
    if (isFetching && hideLoading) return null;
    if (isFetching && (!items || isEmpty(items))) {
      return (
        <div className={classes.loadingContainer}>
          <LinearProgress />
        </div>
      );
    }
    if (!items || !Array.isArray(items)) return null;
    return (
      <div className={classes.root}>
        <div className={classes.loadingContainer}>
          {isFetching && <LinearProgress />}
        </div>
        <div>
          {items.map((item, index) => (
            <ResultListCard key={item.id || index} item={item} />
          ))}
        </div>
      </div>
    );
  }
}

ResultList.propTypes = {
  classes: PropTypes.object.isRequired,
  t: PropTypes.func.isRequired,
  items: PropTypes.array
};

const mapStateToProps = state => {
  return {
    items: getSearchItems(state),
    isFetching: getSearchIsFetching(state)
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
)(ResultList);
