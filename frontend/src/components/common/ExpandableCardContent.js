import React, { Component } from "react";
import { withStyles } from "leoek-material-ui-core-fork/styles";
import { compose } from "redux";
import PropTypes from "prop-types";
import ExpandMoreIcon from "@material-ui/icons/ExpandMore";

import Collapse from "leoek-material-ui-core-fork/Collapse";

import IconButton from "leoek-material-ui-core-fork/IconButton";
import classnames from "classnames";
import translate from "react-i18next/dist/commonjs/translate";

const styles = theme => ({
  actions: {
    display: "flex"
  },
  expand: {
    transform: "rotate(0deg)",
    transition: theme.transitions.create("transform", {
      duration: theme.transitions.duration.shortest
    }),
    marginLeft: "auto",
    [theme.breakpoints.up("sm")]: {
      marginRight: -8
    }
  },
  //TODO hiding any overlfow is not optimal
  previewContent: {
    maxWidth: "100%",
    overflow: "hidden"
  },
  expandOpen: {
    transform: "rotate(180deg)"
  },
  padLeft: {
    paddingLeft: theme.spacing.unit * 2
  },
  padRight: {
    paddingRight: theme.spacing.unit * 2
  }
});

class ExpandableCardContent extends Component {
  state = {
    expanded: !!this.props.defaultExpanded
  };

  handleExpandClick = () => {
    const { expanded } = this.state;
    this.setState({
      expanded: !expanded
    });
  };

  render = () => {
    const { classes, children, previewContent, t, expandable } = this.props;
    const { expanded } = this.state;

    const isExpanded = expanded && expandable;

    return (
      <div>
        <div
          className={classnames([
            classes.actions,
            classes.padLeft,
            classes.padRight
          ])}
        >
          <div className={classes.previewContent}>{previewContent}</div>
          <IconButton
            className={classnames(classes.expand, {
              [classes.expandOpen]: isExpanded
            })}
            onClick={this.handleExpandClick}
            aria-expanded={isExpanded}
            aria-label={t("show_more")}
            disabled={!expandable}
          >
            <ExpandMoreIcon />
          </IconButton>
        </div>
        <Collapse in={isExpanded} timeout="auto" unmountOnExit>
          {children}
        </Collapse>
      </div>
    );
  };
}

ExpandableCardContent.proptypes = {
  expandable: PropTypes.bool
};

ExpandableCardContent.defaultProps = {
  expandable: true
};

export default compose(
  withStyles(styles),
  translate()
)(ExpandableCardContent);
