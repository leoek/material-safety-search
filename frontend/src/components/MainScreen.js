import React, { Component } from "react";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { translate } from "react-i18next";
import AppBar from "@material-ui/core/AppBar";
import Toolbar from "@material-ui/core/Toolbar";
import Typography from "@material-ui/core/Typography";
import Grid from "@material-ui/core/Grid";
import { withStyles } from "@material-ui/core/styles";

import SearchForm from "./SearchForm";

import { updateSearchInput } from "../redux/actions";
import { getSearchFormValues } from "../redux/selectors";
import ResultList from "./ResultList";
import DatasheetSectionDialog from "./DatasheetSectionDialog";
import NotificationHandler from "./NotificationHandler";
import FacetSelection from "./FacetSelection";

import config from "../config";
import DatasheetDialog from "./DatasheetDialog";

const styles = theme => ({
  root: {
    flexGrow: (config.devEnv && console.log("Used theme: ", theme)) || 1,
    backgroundColor: theme.palette.background.main,
    //TDOO fix this scrollbar normalizing hack
    minHeight: "101vh"
  },
  details: {
    flex: 1,
    flexDirection: "column"
  },
  paper: {
    marginTop: theme.spacing.unit * 2,
    marginBottom: theme.spacing.unit * 2
  },
  link: {
    fontSize: 10
  }
});

export class Screen extends Component {
  componentDidMount = () => {
    const { updateSearchInput } = this.props;
    updateSearchInput({ query: "test" });
  };

  componentWillReceiveProps(nextProps) {
    const { error } = this.props;
    if (error !== nextProps.error) {
      this.setState({
        error
      });
    }
  }

  submit = values => {
    const { updateSearchInput } = this.props;
    updateSearchInput(values);
  };

  render() {
    const { values, classes, t } = this.props;

    const canSubmit = !!values;

    return (
      <div className={classes.root}>
        <AppBar position="sticky" color="default">
          <Toolbar>
            <Typography variant="title" color="inherit">
              {t("title")}
            </Typography>
            <Typography color="inherit">
              {`${config.version}-${config.buildNumber}`}
            </Typography>
          </Toolbar>
        </AppBar>
        <Grid container>
          <Grid item xs={false} sm={1} md={2} lg={3} />
          <Grid item xs={12} sm={10} md={8} lg={6}>
            <SearchForm
              ref={form => (this.form = form)}
              initialValues={{ query: "test" }}
              onSubmit={this.submit}
              canSubmit={canSubmit}
            />
            <FacetSelection />
            <ResultList hideLoading />
          </Grid>
          <Grid item xs={false} sm={1} md={2} lg={3} />
        </Grid>
        <DatasheetSectionDialog />
        <DatasheetDialog />
        <NotificationHandler />
      </div>
    );
  }
}

Screen.propTypes = {
  classes: PropTypes.object.isRequired,
  t: PropTypes.func.isRequired,
  values: PropTypes.object
};

const mapStateToProps = state => {
  return {
    values: getSearchFormValues(state)
  };
};

const mapDispatchToProps = {
  updateSearchInput
};

export const ConnectedScreen = connect(
  mapStateToProps,
  mapDispatchToProps
)(Screen);

export default withStyles(styles)(translate()(ConnectedScreen));
