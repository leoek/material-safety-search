import React from "react";
import { connect } from "react-redux";
import { compose } from "redux";
import PropTypes from "prop-types";
import { translate } from "react-i18next";
import Typography from "@material-ui/core/Typography";
import Button from "@material-ui/core/Button";
import { withStyles } from "@material-ui/core/styles";

import classnames from "classnames";

import ExpandableCardContent from "./common/ExpandableCardContent";
import { showDatasheetSection } from "../redux/actions";

import config from "../config";

const { datasheetFormat } = config;

const styles = theme => ({
  padder: {
    margin: 5
  },
  padLeft: {
    paddingLeft: theme.spacing.unit * 2
  },
  padRight: {
    paddingRight: theme.spacing.unit * 2
  }
});

export const RawSectionSelectButton = ({
  t,
  section,
  label,
  showDatasheetSection,
  Icon,
  customColor,
  classes,
  variant
}) => (
  <Button
    variant={variant}
    className={classes.padder}
    style={customColor ? { backgroundColor: customColor } : undefined}
    onClick={() => showDatasheetSection(section)}
  >
    <Typography>{t(label)}</Typography>
    {Icon}
  </Button>
);

RawSectionSelectButton.propTypes = {
  t: PropTypes.func.isRequired,
  section: PropTypes.object.isRequired,
  label: PropTypes.string.isRequired,
  showDatasheetSection: PropTypes.func.isRequired,
  Icon: PropTypes.node,
  classes: PropTypes.object.isRequired,
  customColor: PropTypes.string,
  variant: PropTypes.string
};

RawSectionSelectButton.defaultProps = {
  Icon: null,
  customColor: null,
  variant: "contained"
};

export const SectionSelectButton = compose(
  translate(),
  withStyles(styles)
)(RawSectionSelectButton);

export const SectionSelections = ({
  item,
  showDatasheetSection,
  classes,
  skip = []
}) => (
  <div className={classnames([classes.padLeft, classes.padRight])}>
    {datasheetFormat.sections
      .filter(section => !skip.includes(section.name))
      .map(section => {
        const content = item && item[section.dataKey];
        if (content) {
          return (
            <SectionSelectButton
              section={section}
              key={section.name}
              label={`${datasheetFormat.translationKeyPrefix}.${section.name}`}
              showDatasheetSection={section =>
                showDatasheetSection(item, section)
              }
            />
          );
        }
        return null;
      })}
  </div>
);

export const ConnectedSectionSelections = compose(
  withStyles(styles),
  connect(
    null,
    { showDatasheetSection }
  )
)(SectionSelections);

export const ExpandableSectionSelections = ({ item }) => {
  const firstAidSection = datasheetFormat.sections.find(
    section => section.name === "FirstAid"
  );
  const fireFightingSection = datasheetFormat.sections.find(
    section => section.name === "FireFighting"
  );

  return (
    <ExpandableCardContent
      previewContent={
        <div>
          <SectionSelectButton
            variant="contained"
            customColor="#83bc7a"
            section={firstAidSection}
            key={firstAidSection.name}
            label={`${datasheetFormat.translationKeyPrefix}.${
              firstAidSection.name
            }`}
            showDatasheetSection={firstAidSection =>
              showDatasheetSection(item, firstAidSection)
            }
          />
          <SectionSelectButton
            variant="contained"
            customColor="#ff6363"
            section={fireFightingSection}
            key={fireFightingSection.name}
            label={`${datasheetFormat.translationKeyPrefix}.${
              fireFightingSection.name
            }`}
            showDatasheetSection={fireFightingSection =>
              showDatasheetSection(item, fireFightingSection)
            }
          />
        </div>
      }
    >
      <ConnectedSectionSelections
        skip={["FirstAid", "FireFighting"]}
        item={item}
      />
    </ExpandableCardContent>
  );
};

export default ExpandableSectionSelections;
