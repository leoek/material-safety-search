import React, { Component } from "react";
import PropTypes from "prop-types";
import { compose } from "redux";
import { connect } from "react-redux";
import TextField from "@material-ui/core/TextField";
import Button from "@material-ui/core/Button";
import Grid from "@material-ui/core/Grid";
import Paper from "@material-ui/core/Paper";
import LinearProgress from "@material-ui/core/LinearProgress";
import { withStyles } from "@material-ui/core/styles";
import { Field, reduxForm } from "redux-form";
import { translate } from "react-i18next";
import classnames from "classnames";
import { getSearchIsFetching } from "../redux/selectors";

const styles = theme => ({
  root: {
    flexGrow: 1
  },
  formItem: {
    width: "100%"
  },
  formItemContainer: {
    padding: theme.spacing.unit * 2
  },
  buttonContainer: {
    display: "flex",
    alignItems: "flex-end",
    justifyContent: "flex-end"
  },
  paper: {
    marginTop: theme.spacing.unit * 2,
    marginBottom: theme.spacing.unit * 2
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

const renderTextField = ({ input, meta: { touched, error }, ...rest }) => (
  <TextField {...rest} {...input} error={touched && error} />
);

export class SearchForm extends Component {
  render() {
    const {
      handleSubmit,
      canSubmit,
      classes,
      t,
      loading,
      isFetching
    } = this.props;
    const isLoading = isFetching || loading;
    return (
      <div className={classes.root}>
        <form onSubmit={handleSubmit} className={classes.form}>
          <Paper className={classnames(classes.paper)}>
            <Grid container>
              <Grid
                item
                xs={6}
                sm={8}
                md={8}
                lg={10}
                className={classes.formItemContainer}
              >
                <Field
                  label={t("searchform.search.querylbl")}
                  name="query"
                  component={renderTextField}
                  className={classes.formItem}
                />
              </Grid>
              <Grid
                item
                xs={6}
                sm={4}
                md={4}
                lg={2}
                className={classnames(
                  classes.formItemContainer,
                  classes.buttonContainer
                )}
              >
                <Button
                  variant="contained"
                  color="primary"
                  type="submit"
                  disabled={!canSubmit}
                  className={classnames(classes.formItem, classes.searchButton)}
                >
                  {t("searchform.search.submit")}
                </Button>
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
  withStyles(styles),
  translate(),
  connect(
    mapStateToProps,
    null
  )
)(SearchForm);
