import React, { Component } from "react";
import PropTypes from "prop-types";
import { compose } from "redux";
import { connect } from "react-redux";
import Button from "@material-ui/core/Button";
import Grid from "@material-ui/core/Grid";
import Paper from "@material-ui/core/Paper";
import LinearProgress from "@material-ui/core/LinearProgress";
import withWidth from "@material-ui/core/withWidth";
import { withStyles } from "@material-ui/core/styles";
import { Field, reduxForm } from "redux-form";
import { translate } from "react-i18next";
import classnames from "classnames";
import { getSearchIsFetching } from "../redux/selectors";

import { RenderTextField, RenderCheckbox } from "./common/InputFields";

const styles = theme => ({
  root: {
    flexGrow: 1
  },
  formItem: {
    width: "100%"
  },
  formItemContainer: {
    paddingTop: theme.spacing.unit,
    paddingBottom: theme.spacing.unit,
    paddingLeft: theme.spacing.unit * 2,
    paddingRight: theme.spacing.unit * 2
  },
  buttonContainer: {
    display: "flex",
    alignItems: "flex-end",
    justifyContent: "flex-end"
  },
  paper: {
    marginTop: theme.spacing.unit * 2,
    marginBottom: theme.spacing.unit * 2,
    paddingTop: theme.spacing.unit * 2
  },
  title: {
    fontSize: 14
  },
  loadingContainer: {
    display: "block",
    height: 5,
    justifyContent: "center",
    alignItems: "center"
  }
});

const RawSearchButton = ({ classes, canSubmit, t }) => (
  <Button
    variant="contained"
    color="primary"
    type="submit"
    disabled={!canSubmit}
    className={classnames(classes.formItem, classes.searchButton)}
  >
    {t("searchform.search.submit")}
  </Button>
);

export const SearchButton = compose(
  withStyles(styles),
  translate()
)(RawSearchButton);

const RawSearchButtonGrid = ({ classes, ...props }) => (
  <Grid
    item
    xs={12}
    sm={4}
    md={4}
    lg={2}
    className={classnames(classes.formItemContainer, classes.buttonContainer)}
  >
    <SearchButton {...props} />
  </Grid>
);

const SearchButtonGrid = withStyles(styles)(RawSearchButtonGrid);
export class SearchForm extends Component {
  render() {
    const {
      handleSubmit,
      canSubmit,
      classes,
      t,
      loading,
      isFetching,
      width
    } = this.props;
    const isLoading = isFetching || loading;
    const searchButtonTopWidths = ["xs", "md", "lg", "xl"];
    const searchButtonTop = searchButtonTopWidths.includes(width);
    return (
      <div className={classes.root}>
        <form onSubmit={handleSubmit} className={classes.form}>
          <Paper className={classnames(classes.paper)}>
            <Grid container>
              <Grid
                item
                xs={12}
                sm={12}
                md={8}
                lg={10}
                className={classes.formItemContainer}
              >
                <Field
                  label={t("searchform.search.querylbl")}
                  name="query"
                  component={RenderTextField}
                  className={classes.formItem}
                />
              </Grid>
              {searchButtonTop && <SearchButtonGrid canSubmit={canSubmit} />}
              <Grid
                item
                xs={6}
                sm={4}
                md={4}
                lg={4}
                className={classnames(classes.formItemContainer)}
              >
                <Field
                  label={t("searchform.search.fuzzylbl")}
                  name="fuzzy"
                  component={RenderCheckbox}
                  className={classes.checkboxField}
                  type="checkbox"
                />
              </Grid>
              <Grid
                item
                xs={6}
                sm={4}
                md={4}
                lg={4}
                className={classnames(classes.formItemContainer)}
              >
                <Field
                  label={t("searchform.search.wholedoclbl")}
                  name="wholeDoc"
                  component={RenderCheckbox}
                  className={classes.checkboxField}
                  type="checkbox"
                />
              </Grid>
              {!searchButtonTop && <SearchButtonGrid canSubmit={canSubmit} />}
            </Grid>
            <div className={classes.loadingContainer}>
              {isLoading && <LinearProgress />}
            </div>
          </Paper>
        </form>
      </div>
    );
  }
}

SearchForm.propTypes = {
  classes: PropTypes.object.isRequired,
  t: PropTypes.func.isRequired,
  isFetching: PropTypes.bool
};

SearchForm.defaultProps = {
  isFetching: false
};

const mapStateToProps = state => ({
  isFetching: getSearchIsFetching(state)
});

export default compose(
  reduxForm({ form: "search" }),
  withWidth(),
  withStyles(styles),
  translate(),
  connect(
    mapStateToProps,
    null
  )
)(SearchForm);
