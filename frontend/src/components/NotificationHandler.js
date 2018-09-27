import React, { Component } from "react";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { translate } from "react-i18next";
import Snackbar from "leoek-material-ui-core-fork/Snackbar";

export class NotificationHandler extends Component {
  handleClose = () => {
    console.log("Close Snackabar.");
  };

  render = () => {
    const { notification, t } = this.props;
    return (
      <Snackbar
        anchorOrigin={{ vertical: "bottom", horizontal: "center" }}
        open={!!notification}
        onClose={this.handleClose}
        SnackbarContentProps={{
          "aria-describedby": "message-id"
        }}
        //TODO replace displayed tex with the message from the error object.
        message={<span id="message-id">{t("failure")}</span>}
      />
    );
  };
}

NotificationHandler.propTypes = {
  t: PropTypes.func.isRequired,
  notification: PropTypes.object
};

NotificationHandler.defaultProps = {
  notification: null
};

const mapStateToProps = state => ({
  notification: null
});

const mapDispatchToProps = null;

export default translate()(
  connect(
    mapStateToProps,
    mapDispatchToProps
  )(NotificationHandler)
);
