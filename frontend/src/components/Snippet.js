import React from "react";
import PropTypes from "prop-types";
import { compose } from "redux";
import { translate } from "react-i18next";
import Typography from "@material-ui/core/Typography";
import { withStyles } from "@material-ui/core/styles";

import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableRow from "@material-ui/core/TableRow";
import Chip from "leoek-material-ui-core-fork/Chip";
import Avatar from "@material-ui/core/Avatar";

import ExpandableCardContent from "./common/ExpandableCardContent";

import config from "../config";
import classnames from "classnames";

const { datasheetFormat } = config;

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
  chip: {
    marginRight: theme.spacing.unit,
    marginTop: theme.spacing.unit * 0.5,
    marginBottom: theme.spacing.unit * 0.5
  }
});

const getSnippetElementValue = item => (element, keyName = "dataKey") => {
  let content;
  const dataKey = element[keyName];
  if (Array.isArray(dataKey)) {
    content = dataKey.reduce(
      (prev, key) =>
        `${prev ? `${prev}${element.betweenValues || " "}` : ""}${item[key] ||
          ""} `,
      ""
    );
  } else if (item[dataKey]) {
    content = item[dataKey];
  } else {
    content = "";
  }
  return content;
};

const OldRawSnippetPreviewRow = ({ item, t, row }) => {
  const generator = getSnippetElementValue(item);
  return (
    <Typography>
      {row
        .map(
          element =>
            `${t(
              `${datasheetFormat.translationKeyPrefix}.${element.name}`
            )} ${generator(element)}`
        )
        .join(" ")}
    </Typography>
  );
};

// eslint-disable-next-line no-unused-vars
const OldSnippetPreviewRow = translate()(OldRawSnippetPreviewRow);

export const RawSnippetPreviewRow = ({ item, row, classes, t }) => {
  const avatar = t(`${datasheetFormat.translationKeyPrefix}.${row.name}`);
  const value = getSnippetElementValue(item)(row);
  return (
    <Chip
      avatar={<Avatar style={{ width: 50 }}>{avatar}</Avatar>}
      label={value}
      className={classes.chip}
      variant="outlined"
      aria-label={getSnippetElementValue(item)(row, "ariaDataKey")}
      title={getSnippetElementValue(item)(row, "ariaDataKey")}
    />
  );
};

RawSnippetPreviewRow.propTypes = {
  classes: PropTypes.object.isRequired,
  item: PropTypes.object.isRequired,
  row: PropTypes.object.isRequired,
  t: PropTypes.func.isRequired
};

const SnippetPreviewRow = compose(
  translate(),
  withStyles(styles)
)(RawSnippetPreviewRow);

const RawSnippetPreview = ({ item, classes }) => {
  return (
    <div className={classnames([classes.smallPadLeft, classes.smallPadRight])}>
      {datasheetFormat.snippetPreview.map((row, index) => (
        <SnippetPreviewRow key={index} row={row} item={item} />
      ))}
    </div>
  );
};

RawSnippetPreview.propTypes = {
  classes: PropTypes.object.isRequired,
  item: PropTypes.object.isRequired
};

const SnippetPreview = withStyles(styles)(RawSnippetPreview);

const RawSnippetRow = ({ row, item, t }) => {
  const content = getSnippetElementValue(item)(row);
  return (
    <TableRow>
      <TableCell>
        <Typography>
          {t(`${datasheetFormat.translationKeyPrefix}.${row.name}`)}
        </Typography>
      </TableCell>
      <TableCell>
        <Typography>{content}</Typography>
      </TableCell>
    </TableRow>
  );
};

RawSnippetRow.propTypes = {
  row: PropTypes.object.isRequired,
  item: PropTypes.object.isRequired,
  t: PropTypes.func.isRequired
};

const SnippetRow = translate()(RawSnippetRow);

export const SnippetTable = ({ item }) => (
  <Table>
    <TableBody>
      {datasheetFormat.snippetProperties.map(row => (
        <SnippetRow key={row.name} row={row} item={item} />
      ))}
    </TableBody>
  </Table>
);

SnippetTable.propTypes = {
  item: PropTypes.object.isRequired
};

export const Snippet = props => {
  const { item, classes } = props;
  return (
    <ExpandableCardContent
      previewContent={<SnippetPreview item={item} />}
      moveContentUp
    >
      <div className={classnames([classes.padLeft, classes.padRight])}>
        <SnippetTable item={item} />
      </div>
    </ExpandableCardContent>
  );
};

Snippet.propTypes = {
  classes: PropTypes.object.isRequired,
  item: PropTypes.object.isRequired
};

export default withStyles(styles)(Snippet);
