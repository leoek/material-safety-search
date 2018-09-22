import React from "react";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/core/styles";
import classnames from "classnames";

const styles = theme => ({
  text: {
    color: theme.palette.text.primary
  }
});

const RawText = ({ classes, text, innerClassName, ...rest }) => {
  const enhancedText = text.split("\n").join("<br />");
  return (
    <div
      {...rest}
      className={classnames([innerClassName, classes.text])}
      dangerouslySetInnerHTML={{ __html: enhancedText }}
    />
  );
};

RawText.propTypes = {
  classes: PropTypes.object.isRequired,
  text: PropTypes.string
};

RawText.defaultProps = {
  text: ""
};

export default withStyles(styles)(RawText);
