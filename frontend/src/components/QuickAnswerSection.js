import React, { Component } from "react";
import { connect } from "react-redux";
import { compose } from "redux";
import PropTypes from "prop-types";
import Typography from "@material-ui/core/Typography";
import ExpansionPanel from "@material-ui/core/ExpansionPanel";
import ExpansionPanelSummary from "@material-ui/core/ExpansionPanelSummary";
import ExpandMoreIcon from "@material-ui/icons/ExpandMore";
import ExpansionPanelDetails from "@material-ui/core/ExpansionPanelDetails";
import { withStyles } from "@material-ui/core/styles";

import { fetchSearchRequest } from "../redux/actions";
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

const mapDispatchToProps = {
  fetchSearchRequest
};

export default compose(
  connect(
    mapStateToProps,
    mapDispatchToProps
  ),
  withStyles(styles)
)(QuickAnswerSection);
