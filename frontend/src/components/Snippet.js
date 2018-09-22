import React, { Component } from "react";
import PropTypes from "prop-types";
import { translate } from "react-i18next";
import Typography from "@material-ui/core/Typography";
import { withStyles } from "@material-ui/core/styles";

import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";

import ExpandableCardContent from "./common/ExpandableCardContent";

import config from "../config";

const { datasheetFormat } = config;

const styles = theme => ({});

export const Snippet = props => {
  const { item, classes } = props;
  return (
    <ExpandableCardContent>
      <Table>
        <TableBody>
          <TableRow>
            <TableCell>a</TableCell>
            <TableCell>b</TableCell>
          </TableRow>
        </TableBody>
      </Table>
    </ExpandableCardContent>
  );
};

export default withStyles(styles)(Snippet);
