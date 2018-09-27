import React, { Component } from "react";
import { connect } from "react-redux";
import { compose } from "redux";
import PropTypes from "prop-types";
import Typography from "leoek-material-ui-core-fork/Typography";
import ExpansionPanel from "leoek-material-ui-core-fork/ExpansionPanel";
import ExpansionPanelSummary from "leoek-material-ui-core-fork/ExpansionPanelSummary";
import ExpandMoreIcon from "@material-ui/icons/ExpandMore";
import ExpansionPanelDetails from "leoek-material-ui-core-fork/ExpansionPanelDetails";
import { withStyles } from "leoek-material-ui-core-fork/styles";

import { getSearchItems, getSearchIsFetching } from "../redux/selectors";

const styles = theme => ({
  root: {
    flexGrow: 1
  },
  details: {
    flex: 1,
    flexDirection: "column"
  },
  paper: {
    marginTop: theme.spacing.unit * 2,
    marginBottom: theme.spacing.unit * 2
  },
  link: {
    fontSize: 10
  }
});

export class QuickAnswerSection extends Component {
  render() {
    const { classes, quickAnswer } = this.props;
    if (!quickAnswer) return null;
    return (
      <ExpansionPanel defaultExpanded className={classes.paper}>
        <ExpansionPanelSummary expandIcon={<ExpandMoreIcon />}>
          <Typography className={classes.title}>
            {quickAnswer.summary}
          </Typography>
        </ExpansionPanelSummary>
        <ExpansionPanelDetails className={classes.details}>
          {quickAnswer.details.map((step, index) => (
            <Typography key={index}>{`${index + 1}. ${step}`}</Typography>
          ))}
        </ExpansionPanelDetails>
      </ExpansionPanel>
    );
  }
}

QuickAnswerSection.propTypes = {
  classes: PropTypes.object.isRequired,
  quickAnswer: PropTypes.object
};

QuickAnswerSection.defaultProps = {
  quickAnswer: null
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
  withStyles(styles)
)(QuickAnswerSection);
