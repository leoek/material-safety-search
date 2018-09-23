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

import { fetchSearchRequest } from "../redux/actions";
import { getSearchFormValues } from "../redux/selectors";
import ResultList from "./ResultList";
import QuickAnswerSection from "./QuickAnswerSection";
import DatasheetSectionDialog from "./DatasheetSectionDialog";
import NotificationHandler from "./NotificationHandler";

import config from "../config";

const styles = theme => ({
  root: {
    flexGrow: (config.devEnv && console.log("Used theme: ", theme)) || 1,
    backgroundColor: theme.palette.background.main,
    minHeight: "100vh"
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
  constructor(props) {
    super(props);
    this.state = {
      error: null,
      options: null,
      quickAnswer: null
    };
  }

  componentDidMount = () => {
    const { fetchSearchRequest } = this.props;
    fetchSearchRequest({ query: "test" });
  };

  componentWillReceiveProps(nextProps) {
    const { error } = this.props;
    if (error !== nextProps.error) {
      this.setState({
        error
      });
    }
  }

  setMockOptions = () => {
    this.setState({
      options: {
        0: { lbl: "on fire" },
        1: { lbl: "spilled" },
        2: { lbl: "ingested" }
      }
    });
  };

  setMockAnswer = () => {
    const { quickAnswer } = this.state;
    if (quickAnswer) return false;
    this.setState({
      quickAnswer: {
        summary: "first aid measures",
        details: [
          "Lorem ipsum dolor sit amet.",
          "consectetur adipiscing elit.",
          "Suspendisse malesuada lacus ex",
          "sit amet blandit leo lobortis eget."
        ]
      }
    });
  };

  resetMockAnswer = () => {
    this.setState({
      quickAnswer: null
    });
  };

  submit = values => {
    const { fetchSearchRequest } = this.props;
    fetchSearchRequest(values);
    this.setMockOptions();
  };

  handleQuickSelect = id => () => {
    const { options } = this.state;
    const selected = options[id];
    const newOptions = {
      ...options,
      [id]: {
        ...selected,
        active: !selected.active
      }
    };
    this.setState({
      options: newOptions
    });
    const isActive = Object.values(newOptions).reduce(
      (result, option) => result || option.active,
      false
    );
    if (isActive) {
      this.setMockAnswer();
    } else {
      this.resetMockAnswer();
    }
  };

  render() {
    const { values, classes, t } = this.props;
    const { options, quickAnswer } = this.state;

    const canSubmit = !!values;

    const quickselect = {
      options:
        options &&
        Object.keys(options).map(id => ({
          ...options[id],
          id,
          handler: this.handleQuickSelect(id)
        }))
    };

    return (
      <div className={classes.root}>
        <AppBar position="static" color="default">
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
              quickselect={quickselect}
            />
            <QuickAnswerSection quickAnswer={quickAnswer} />
            <ResultList hideLoading={false} />
          </Grid>
          <Grid item xs={false} sm={1} md={2} lg={3} />
        </Grid>
        <DatasheetSectionDialog />
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
  fetchSearchRequest
};

export const ConnectedScreen = connect(
  mapStateToProps,
  mapDispatchToProps
)(Screen);

export default withStyles(styles)(translate()(ConnectedScreen));
