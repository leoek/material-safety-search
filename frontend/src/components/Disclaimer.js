import React from "react";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/core/styles";
import { translate } from "react-i18next";
import RawText from "./common/RawText";
import config from "../config";
const { datasheetFormat } = config;

const styles = theme => ({});

const Disclaimer = ({ t }) => {
  const content = `Disclaimer (provided with this information by the compiling agencies):
    This information is formulated for use by elements of the Department
    of Defense.  The United States of America in no manner whatsoever,
    expressly or implied, warrants this information to be accurate and
    disclaims all liability for its use.  Any person utilizing this
    document should seek competent professional advice to verify and
    assume responsibility for the suitability of this information to their
    particular situation.`;

  return (
    <div key="disclaimer">
      <h3>{t(`${datasheetFormat.translationKeyPrefix}.disclaimer`)}</h3>
      <RawText text={content} />
    </div>
  );
};

Disclaimer.propTypes = {
  classes: PropTypes.object.isRequired,
  t: PropTypes.func.isRequired
};

export default translate()(withStyles(styles)(Disclaimer));
