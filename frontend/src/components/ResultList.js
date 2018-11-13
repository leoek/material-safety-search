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

import Padder from "./common/Padder";
import SectionSelections from "./SectionSelections";
import Snippet from "./Snippet";
import Pagination from "./Pagination";

import { showDatasheet } from "../redux/actions";
import { getSearchItems, getSearchIsFetching } from "../redux/selectors";
import ResultMeta from "./ResultMeta";

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
  titleContainer: {
    display: "flex",
    flex: 1,
    alignItems: "flex-end"
  },
  title: {
    fontSize: 20,
    fontWeight: theme.typography.fontWeightMedium
  },
  presubtitle: {
    fontSize: 14,
    marginLeft: theme.spacing.unit * 0.5
  },
  subtitle: {
    fontSize: 14,
    fontWeight: theme.typography.fontWeightMedium,
    marginLeft: theme.spacing.unit
  },
  subtitleButton: {
    marginBottom: 3
  }
});

const RawResultTitle = ({
  t,
  item,
  classes,
  companyAction,
  productIdAction
}) => {
  const { productId, companyName } = item;

  return (
    <div className={classes.titleContainer}>
      <Button className={classes.titleButton} onClick={productIdAction}>
        <Typography className={classes.title}>{productId}</Typography>
      </Button>
      <Button className={classes.subtitleButton} onClick={companyAction}>
        <Typography className={classnames([classes.presubtitle])}>
          {t("fromCompany_label")}
        </Typography>
        <Typography className={classnames([classes.subtitle])}>
          {companyName}
        </Typography>
      </Button>
    </div>
  );
};

RawResultTitle.propTypes = {
  item: PropTypes.object.isRequired,
  classes: PropTypes.object.isRequired,
  t: PropTypes.func.isRequired,
  companyAction: PropTypes.func,
  productIdAction: PropTypes.func
};

RawResultTitle.defaultProps = {
  companyAction: () => false,
  productIdAction: () => false
};

export const ResultTitle = compose(
  translate(),
  withStyles(styles)
)(RawResultTitle);

export const RawResultListCard = ({ item, classes, showDatasheet }) => {
  return (
    <Card className={classes.paper}>
      <CardContent>
        <ResultTitle
          item={item}
          companyAction={() => showDatasheet(item)}
          productIdAction={() => showDatasheet(item)}
        />
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

const ResultListCard = compose(
  translate(),
  withStyles(styles),
  connect(
    null,
    { showDatasheet }
  )
)(RawResultListCard);

const ResultListContainer = ({
  hideLoading,
  isFetching,
  classes,
  children = null
}) => (
  <div className={classes.root}>
    <ResultMeta />
    <Pagination />
    {!hideLoading && (
      <div className={classes.loadingContainer}>
        {isFetching && <LinearProgress />}
      </div>
    )}
    {children}
    <Pagination />
  </div>
);

export class ResultList extends Component {
  render() {
    const { items } = this.props;

    if (!items || !Array.isArray(items) || isEmpty(items))
      return <ResultListContainer {...this.props} />;

    return (
      <ResultListContainer {...this.props}>
        <div>
          {items.map((item, index) => (
            <ResultListCard key={item.id || index} item={item} />
          ))}
        </div>
      </ResultListContainer>
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

const mapDispatchToProps = null;

export default compose(
  connect(
    mapStateToProps,
    mapDispatchToProps
  ),
  withStyles(styles),
  translate()
)(ResultList);
