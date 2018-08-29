import React, { Component } from "react";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { translate } from "react-i18next";
import Snackbar from "@material-ui/core/Snackbar";
import AppBar from "@material-ui/core/AppBar";
import Toolbar from "@material-ui/core/Toolbar";
import Typography from "@material-ui/core/Typography";
import Card from "@material-ui/core/Card";
import CardContent from "@material-ui/core/CardContent";
import Grid from "@material-ui/core/Grid";
import Button from "@material-ui/core/Button";
import ExpansionPanel from "@material-ui/core/ExpansionPanel";
import ExpansionPanelSummary from "@material-ui/core/ExpansionPanelSummary";
import ExpandMoreIcon from "@material-ui/icons/ExpandMore";
import ExpansionPanelDetails from "@material-ui/core/ExpansionPanelDetails";
import { withStyles } from "@material-ui/core/styles";
import SearchForm from "./SearchForm";
import { getSearchFormValues } from "../redux/selectors";

const styles = theme => ({
  root: {
    flexGrow: 1
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

  handleClose = () => {
    this.setState({ error: null });
  };

  submit = values => {
    console.log({ values });
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
    const { error, options, quickAnswer } = this.state;

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

    const loremIpsum = `Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse malesuada lacus ex, sit amet blandit leo lobortis eget.`;

    const results = [
      "https://example.org",
      "https://herr-ek.de",
      "https://xkcd.com"
    ].map((link, index) => ({
      title: `Awesome Title ${index}`,
      link,
      snippet: loremIpsum
    }));

    return (
      <div className={classes.root}>
        <AppBar position="static" color="default">
          <Toolbar>
            <Typography variant="title" color="inherit">
              {t("title")}
            </Typography>
          </Toolbar>
        </AppBar>
        <Grid container>
          <Grid item xs={false} sm={1} md={2} lg={3} />
          <Grid item xs={12} sm={10} md={8} lg={6}>
            <SearchForm
              ref={form => (this.form = form)}
              initialValues={{ searchField: "" }}
              onSubmit={this.submit}
              canSubmit={canSubmit}
              quickselect={quickselect}
            />
            {quickAnswer && (
              <ExpansionPanel defaultExpanded className={classes.paper}>
                <ExpansionPanelSummary expandIcon={<ExpandMoreIcon />}>
                  <Typography className={classes.title}>
                    {quickAnswer.summary}
                  </Typography>
                </ExpansionPanelSummary>
                <ExpansionPanelDetails className={classes.details}>
                  {quickAnswer.details.map((step, index) => (
                    <Typography key={index}>
                      {`${index + 1}. ${step}`}
                    </Typography>
                  ))}
                </ExpansionPanelDetails>
              </ExpansionPanel>
            )}
            {results &&
              Array.isArray(results) &&
              results.map((result, index) => (
                <Card key={index} className={classes.paper}>
                  <CardContent>
                    <Typography
                      variant="headline"
                      component="h2"
                      className={classes.title}
                    >
                      {result.title}
                    </Typography>
                    <a href={result.link}>
                      <Button className={classes.link}>{result.link}</Button>
                    </a>
                    <Typography className={classes.snippet}>
                      {result.snippet}
                    </Typography>
                  </CardContent>
                </Card>
              ))}
          </Grid>
          <Grid item xs={false} sm={1} md={2} lg={3} />
        </Grid>
        <Snackbar
          anchorOrigin={{ vertical: "bottom", horizontal: "center" }}
          open={!!error}
          onClose={this.handleClose}
          SnackbarContentProps={{
            "aria-describedby": "message-id"
          }}
          //TODO replace displayed tex with the message from the error object.
          message={<span id="message-id">{t("failure")}</span>}
        />
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

export const ConnectedScreen = connect(
  mapStateToProps,
  null
)(Screen);

export default withStyles(styles)(translate()(ConnectedScreen));
