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

import { selectFacet, deselectFacet } from "../redux/actions";
import {
  getSearchFacets,
  getSearchIsFetching,
  getSearchInputSelectedFacet,
  getUiSelectedFsgFacet
} from "../redux/selectors";
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
    marginBottom: theme.spacing.unit * 2,
    maxWidth: "100%"
  },
  title: {
    fontSize: 20
  },
  chip: {
    marginTop: theme.spacing.unit,
    marginRight: theme.spacing.unit
  },
  avatar: {
    fontSize: 15
  },
  avatar3: {
    fontSize: 14
  },
  avatar4: {
    fontSize: 12
  },
  avatar5: {
    fontSize: 10
  }
});

const RawFacetChips = ({
  preSelectedFacets = [],
  classes,
  facets,
  isFetching,
  selected
}) => {
  return (
    <div className={classnames([classes.smallPadLeft, classes.smallPadRight])}>
      {preSelectedFacets &&
        preSelectedFacets.map(facet => (
          <Grow
            key={`preselected${facet.facetNumber}`}
            in={!isFetching}
            {...(!isFetching ? { timeout: 1000 } : {})}
          >
            <FacetChip facet={facet} isSelected={true} />
          </Grow>
        ))}
      {facets &&
        facets.map(facet => (
          <Grow
            key={facet.facetNumber}
            in={!isFetching}
            {...(!isFetching ? { timeout: 1000 } : {})}
          >
            <FacetChip
              facet={facet}
              isSelected={selected === facet.facetNumber && facet.facetNumber}
            />
          </Grow>
        ))}
    </div>
  );
};

const facetChipsMapStateToProps = (state, ownProps) => {
  const { facets = [] } = ownProps;
  const type = facets[0] && facets[0].type;
  return {
    isFetching: getSearchIsFetching(state),
    selected: getSearchInputSelectedFacet(type)(state)
  };
};

const FacetChips = compose(
  withStyles(styles),
  connect(facetChipsMapStateToProps)
)(RawFacetChips);

const RawFacetChip = ({
  facet,
  classes,
  t,
  selectFacet,
  deselectFacet,
  style,
  isSelected = false
}) => {
  const { facetNumber, facetString, count } = facet;

  if (!count) return <div />;

  const label = `${facetNumber || ""} - ${facetString ||
    t("facetselection.no_facet")}`;

  const countLength = `${count}`.length || 0;

  return (
    <Chip
      avatar={
        <Avatar
          className={classnames(
            classes.avatar,
            classes[`avatar${countLength}`]
          )}
        >
          {count}
        </Avatar>
      }
      label={label}
      clickable={!isSelected}
      onDelete={isSelected ? () => deselectFacet(facet) : undefined}
      onClick={isSelected ? undefined : () => selectFacet(facet)}
      className={classes.chip}
      color="primary"
      variant={isSelected ? "contained" : "outlined"}
      aria-label={label}
      title={label}
      /**
       * We need the style here for the transition.
       * It is injected by the Transitions component (Grow)
       */
      style={style}
    />
  );
};

const FacetChip = compose(
  translate(),
  withStyles(styles),
  connect(
    null,
    {
      selectFacet,
      deselectFacet
    }
  )
)(RawFacetChip);

export class FacetSelection extends Component {
  render() {
    const { classes, t, facets, selectedFsgFacet } = this.props;

    const preSelectedFacets = [];
    if (selectedFsgFacet) {
      preSelectedFacets.push(selectedFsgFacet);
    }

    if (
      !facets ||
      !Array.isArray(facets) ||
      isEmpty(facets.filter(f => f.count))
    ) {
      return null;
    }

    const previewCount = 2 - preSelectedFacets.length;

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
              preSelectedFacets={preSelectedFacets}
              facets={facets.slice(0, previewCount)}
            />
          }
          expandable={Boolean(facets.length > 2)}
        >
          <div className={classnames([classes.padLeft, classes.padRight])}>
            <FacetChips facets={facets.slice(previewCount)} />
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
  facets: PropTypes.array,
  selectedFsgFacet: PropTypes.object
};

FacetSelection.defaultProps = {
  facets: null,
  selectedFsgFacet: null
};

const mapStateToRrops = state => ({
  facets: getSearchFacets(state),
  selectedFsgFacet: getUiSelectedFsgFacet(state)
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
