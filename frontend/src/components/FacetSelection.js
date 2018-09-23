import React, { Component } from "react";
import PropTypes from "prop-types";
import Card from "@material-ui/core/Card";
import CardContent from "@material-ui/core/CardContent";
import Typography from "@material-ui/core/Typography";
import { withStyles } from "@material-ui/core/styles";
import { translate } from "react-i18next";
import classnames from "classnames";
import { compose } from "redux";
import { connect } from "react-redux";
import isEmpty from "lodash/isEmpty";

import Chip from "@material-ui/core/Chip";
import Avatar from "@material-ui/core/Avatar";
import Grow from "@material-ui/core/Grow";

import { selectFacet } from "../redux/actions";
import { getSearchFacets, getSearchIsFetching } from "../redux/selectors";
import ExpandableCardContent from "./common/ExpandableCardContent";
import Padder from "./common/Padder";

const styles = theme => ({
  smallPadLeft: {
    paddingLeft: 5
  },
  smallPadRight: {
    paddingRight: 5
  },
  padLeft: {
    paddingLeft: theme.spacing.unit * 2
  },
  padRight: {
    paddingRight: theme.spacing.unit * 2
  },
  paper: {
    marginTop: theme.spacing.unit * 2,
    marginBottom: theme.spacing.unit * 2
  },
  title: {
    fontSize: 20
  },
  chip: {
    marginTop: theme.spacing.unit,
    marginRight: theme.spacing.unit
  },
  wideAvatar: {
    width: 50
  }
});

const RawFacetChips = ({ classes, facets, isFetching }) => {
  console.log(isFetching);
  return (
    <div className={classnames([classes.smallPadLeft, classes.smallPadRight])}>
      {facets &&
        facets.map(facet => (
          <Grow
            key={facet.facetNumber}
            in={!isFetching}
            {...(!isFetching ? { timeout: 1000 } : {})}
          >
            <FacetChip facet={facet} />
          </Grow>
        ))}
    </div>
  );
};

const FacetChips = withStyles(styles)(RawFacetChips);

const RawFacetChip = ({ facet, classes, t, selectFacet, ...props }) => {
  const { facetNumber, facetString, count } = facet;
  const isLong = facetNumber && facetNumber.length > 2;

  const label = `${facetString} x${count}`;

  return (
    <Chip
      avatar={
        <Avatar className={classnames({ [classes.wideAvatar]: isLong })}>
          {facetNumber}
        </Avatar>
      }
      label={label}
      clickable
      onClick={() => selectFacet(facet)}
      className={classes.chip}
      color="primary"
      variant="outlined"
      aria-label={label}
      title={label}
      {...props}
    />
  );
};

const FacetChip = compose(
  withStyles(styles),
  connect(
    null,
    { selectFacet }
  )
)(RawFacetChip);

export class FacetSelection extends Component {
  render() {
    const { classes, t, facets, isFetching } = this.props;

    if (!facets || !Array.isArray(facets) || isEmpty(facets)) {
      return null;
    }

    const previewCount = 2;

    return (
      <Card className={classes.paper}>
        <CardContent>
          <Typography className={classes.title}>
            {t("facetselection.title")}
          </Typography>
        </CardContent>
        <ExpandableCardContent
          previewContent={
            <FacetChips
              facets={facets.slice(0, previewCount)}
              isFetching={isFetching}
            />
          }
          expandable={Boolean(facets.length > 2)}
        >
          <div className={classnames([classes.padLeft, classes.padRight])}>
            <FacetChips
              facets={facets.slice(previewCount)}
              isFetching={isFetching}
            />
            <Padder />
          </div>
        </ExpandableCardContent>
        <Padder />
      </Card>
    );
  }
}

FacetSelection.propTypes = {
  classes: PropTypes.object.isRequired,
  t: PropTypes.func.isRequired,
  facets: PropTypes.array
};

FacetSelection.defaultProps = {
  facets: null
};

const mapStateToRrops = state => ({
  facets: getSearchFacets(state),
  isFetching: getSearchIsFetching(state)
});

const mapDispatchToProps = {};

export default compose(
  withStyles(styles),
  translate(),
  connect(
    mapStateToRrops,
    mapDispatchToProps
  )
)(FacetSelection);
