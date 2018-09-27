import React, { Component } from "react";
import PropTypes from "prop-types";
import { compose } from "redux";
import { connect } from "react-redux";
import Grid from "leoek-material-ui-core-fork/Grid";
import Paper from "leoek-material-ui-core-fork/Paper";
import LinearProgress from "leoek-material-ui-core-fork/LinearProgress";
import withWidth from "leoek-material-ui-core-fork/withWidth";
import { withStyles } from "leoek-material-ui-core-fork/styles";
import { reduxForm } from "redux-form";
import { translate } from "react-i18next";
import classnames from "classnames";
import { getSearchIsFetching } from "../redux/selectors";

import {
  InputCheckbox,
  InputAutoSuggest,
  InputDatePicker,
  InputIngredientSelection
} from "./common/InputFields";
import { SearchButton } from "./SearchForm";

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

const RawTextFieldGrid = ({ children, name, label, classes }) => {
  if (children) {
    return (
      <Grid
        item
        xs={12}
        sm={6}
        md={6}
        lg={4}
        className={classes.formItemContainer}
      >
        {children}
      </Grid>
    );
  }
  return (
    <TextFieldGrid>
      <InputAutoSuggest label={label} name={name} />
    </TextFieldGrid>
  );
};

const TextFieldGrid = withStyles(styles)(RawTextFieldGrid);

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

    let columns = 1;
    if (["sm", "md"].includes(width)) {
      columns = 2;
    } else if (["lg", "xl"].includes(width)) {
      columns = 3;
    }

    return (
      <div className={classes.root}>
        <form onSubmit={handleSubmit} className={classes.form}>
          <Paper className={classnames(classes.paper)}>
            <Grid container>
              <TextFieldGrid
                label={t("searchform.search.productIdlbl")}
                name="productId"
              />
              <TextFieldGrid
                label={t("searchform.search.fsgStringlbl")}
                name="fsgString"
              />
              <TextFieldGrid
                label={t("searchform.search.fscStringlbl")}
                name="fscString"
              />
              <TextFieldGrid
                label={t("searchform.search.niinlbl")}
                name="niin"
              />
              <TextFieldGrid
                label={t("searchform.search.companyNamelbl")}
                name="companyName"
              />
              <Grid
                item
                xs={12}
                sm={6}
                md={6}
                lg={4}
                className={classnames(classes.formItemContainer)}
              >
                <InputCheckbox
                  label={t("searchform.search.fuzzylbl")}
                  name="fuzzy"
                />
              </Grid>
              <Grid
                item
                xs={12}
                sm={6}
                md={6}
                lg={4}
                className={classnames(classes.formItemContainer)}
              >
                <InputDatePicker
                  name="beginDate"
                  label={t("searchform.search.beginDatelbl")}
                />
              </Grid>
              <Grid
                item
                xs={12}
                sm={6}
                md={6}
                lg={4}
                className={classnames(classes.formItemContainer)}
              >
                <InputDatePicker
                  name="endDate"
                  label={t("searchform.search.endDatelbl")}
                />
              </Grid>
              <InputIngredientSelection
                name="ingredients"
                label={t("searchform.search.ingredientslbl")}
              />
            </Grid>
            <Grid container>
              {[...Array(columns - 1)].map((_, i) => (
                <Grid key={i} item xs={12} sm={6} md={6} lg={4}>
                  {/* Empty container for positioning purposes*/}
                </Grid>
              ))}
              <Grid
                item
                xs={12}
                sm={6}
                md={6}
                lg={4}
                className={classnames(classes.formItemContainer)}
              >
                <SearchButton canSubmit={canSubmit} />
              </Grid>
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
  connect(mapStateToProps)
)(SearchForm);
