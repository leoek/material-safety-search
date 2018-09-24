import React, { Component } from "react";
import { connect } from "react-redux";
import { compose } from "redux";
import PropTypes from "prop-types";
import { translate } from "react-i18next";
import AppBar from "@material-ui/core/AppBar";
import Toolbar from "@material-ui/core/Toolbar";
import Typography from "@material-ui/core/Typography";
import Grid from "@material-ui/core/Grid";
import { withStyles } from "@material-ui/core/styles";
import classnames from "classnames";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import Switch from "@material-ui/core/Switch";
import withWidth from "@material-ui/core/withWidth";

import SearchForm from "./SearchForm";

import { updateSearchInput, toggleAdvancedSearch } from "../redux/actions";
import { getSearchFormValues, getAdvancedSearch } from "../redux/selectors";
import ResultList from "./ResultList";
import DatasheetSectionDialog from "./DatasheetSectionDialog";
import NotificationHandler from "./NotificationHandler";
import FacetSelection from "./FacetSelection";

import config from "../config";
import DatasheetDialog from "./DatasheetDialog";
import AdvancedSearchForm from "./AdvancedSearchForm";

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
  },
  grow: {
    flexGrow: 1
  },
  padLeft: {
    paddingLeft: theme.spacing.unit * 2
  }
});

const RawAdvancedSearchToggle = ({ toggled, t, toggleAdvancedSearch }) => {
  return (
    <FormControlLabel
      control={
        <Switch
          checked={toggled}
          onChange={() => toggleAdvancedSearch()}
          value="checkedB"
          color="primary"
        />
      }
      label={t("advanced_search_togglelbl")}
    />
  );
};

const AdvancedSearchToggle = compose(
  connect(
    state => ({
      toggled: getAdvancedSearch(state)
    }),
    { toggleAdvancedSearch }
  ),
  translate()
)(RawAdvancedSearchToggle);

const RawSearchFormWrapper = ({ isAdvancedSearch, ...rest }) => {
  if (isAdvancedSearch) {
    return <AdvancedSearchForm {...rest} />;
  }
  return (
    <SearchForm
      {...rest}
      initialValues={config.devEnv ? { query: "test" } : undefined}
    />
  );
};

const SearchFormWrapper = connect(state => ({
  isAdvancedSearch: getAdvancedSearch(state)
}))(RawSearchFormWrapper);

const getDefaultInputValues = isAdvancedSearch => {
  const both = {
    fuzzy: false
  };
  if (isAdvancedSearch) {
    return {
      ...both,
      productId: null,
      fsgString: null,
      fscString: null,
      niin: null,
      companyName: null
    };
  }
  return {
    ...both,
    query: "",
    wholeDoc: false
  };
};

export class Screen extends Component {
  componentDidMount = () => {
    const { updateSearchInput } = this.props;
    updateSearchInput({ query: "test" });
  };

  submit = values => {
    const { updateSearchInput, isAdvancedSearch } = this.props;
    updateSearchInput({
      ...getDefaultInputValues(isAdvancedSearch),
      ...values
    });
  };

  render() {
    const { values, classes, t, width } = this.props;
    const phoneWidth = "xs" === width;

    /**
     * For the user can always submit a search
     */
    const canSubmit = true || !!values;

    return (
      <div className={classes.root}>
        <AppBar position="sticky" color="default">
          <Toolbar>
            <Typography
              variant="title"
              color="inherit"
              className={classnames({ [classes.grow]: phoneWidth })}
            >
              {t("title")}
            </Typography>
            {!phoneWidth && (
              <Typography
                color="inherit"
                className={classnames(classes.grow, classes.padLeft)}
              >
                {`${config.version}-${config.buildNumber}`}
              </Typography>
            )}
            <AdvancedSearchToggle />
          </Toolbar>
        </AppBar>
        <Grid container>
          <Grid item xs={false} sm={1} md={2} lg={3} />
          <Grid item xs={12} sm={10} md={8} lg={6}>
            <SearchFormWrapper
              ref={form => (this.form = form)}
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
    values: getSearchFormValues(state),
    isAdvancedSearch: getAdvancedSearch(state)
  };
};

const mapDispatchToProps = {
  updateSearchInput
};

export const ConnectedScreen = connect(
  mapStateToProps,
  mapDispatchToProps
)(Screen);

export default compose(
  withStyles(styles),
  translate(),
  withWidth()
)(ConnectedScreen);
