import React, { Component } from "react";
import { withStyles } from "@material-ui/core/styles";
import { compose } from "redux";
import PropTypes from "prop-types";
import ExpandMoreIcon from "@material-ui/icons/ExpandMore";

import Collapse from "@material-ui/core/Collapse";

import IconButton from "@material-ui/core/IconButton";
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
          <div>{previewContent}</div>
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
