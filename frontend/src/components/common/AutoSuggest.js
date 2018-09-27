import React from "react";
import PropTypes from "prop-types";
import Autosuggest from "react-autosuggest";
import match from "autosuggest-highlight/match";
import parse from "autosuggest-highlight/parse";
import TextField from "@material-ui/core/TextField";
import Paper from "@material-ui/core/Paper";
import MenuItem from "@material-ui/core/MenuItem";
import Popper from "@material-ui/core/Popper";
import { withStyles } from "@material-ui/core/styles";

export const formatSuggestions = list =>
  list.map(value => ({ value, label: value }));

const renderInputComponent = inputProps => {
  const { classes, inputRef = () => {}, ref, ...other } = inputProps;

  return (
    <TextField
      fullWidth
      InputProps={{
        inputRef: node => {
          ref(node);
          inputRef(node);
        },
        classes: {
          input: classes.input
        }
      }}
      {...other}
    />
  );
};

const renderSuggestion = (suggestion, { query, isHighlighted }) => {
  const matches = match(suggestion.label, query);
  const parts = parse(suggestion.label, matches);

  return (
    <MenuItem selected={isHighlighted} component="div">
      <div>
        {parts.map((part, index) => {
          return part.highlight ? (
            <span key={String(index)} style={{ fontWeight: 500 }}>
              {part.text}
            </span>
          ) : (
            <strong key={String(index)} style={{ fontWeight: 300 }}>
              {part.text}
            </strong>
          );
        })}
      </div>
    </MenuItem>
  );
};

const getSuggestionValue = suggestion => {
  return suggestion.label;
};

const styles = theme => ({
  root: {
    height: 250,
    flexGrow: 1
  },
  container: {
    position: "relative"
  },
  suggestionsContainerOpen: {
    position: "absolute",
    zIndex: 1,
    marginTop: theme.spacing.unit,
    left: 0,
    right: 0
  },
  suggestion: {
    display: "block"
  },
  suggestionsList: {
    margin: 0,
    padding: 0,
    listStyleType: "none"
  },
  divider: {
    height: theme.spacing.unit * 2
  }
});

class IntegrationAutosuggest extends React.Component {
  popperNode = null;

  state = {
    showSuggestions: false
  };

  handleSuggestionsFetchRequested = ({ value }) => {
    const { handleFetchSuggestions } = this.props;
    this.setState({
      showSuggestions: true
    });
    handleFetchSuggestions(value);
  };

  handleSuggestionsClearRequested = () => {
    const { handleClearSuggestions } = this.props;
    this.setState({
      showSuggestions: false
    });
    handleClearSuggestions();
  };

  handleChange = name => (event, { newValue }) => {
    const { input } = this.props;
    const { onChange } = input || {};
    onChange(newValue);
  };

  render() {
    const { placeholder, label, classes, input, suggestions } = this.props;
    const { showSuggestions } = this.state;

    const autosuggestProps = {
      renderInputComponent,
      suggestions: showSuggestions ? suggestions : [],
      onSuggestionsFetchRequested: this.handleSuggestionsFetchRequested,
      onSuggestionsClearRequested: this.handleSuggestionsClearRequested,
      getSuggestionValue,
      renderSuggestion
    };

    const { value } = input || {};

    return (
      <Autosuggest
        {...autosuggestProps}
        inputProps={{
          classes,
          label,
          placeholder,
          value,
          onChange: this.handleChange("popper"),
          inputRef: node => {
            this.popperNode = node;
          }
        }}
        theme={{
          suggestionsList: classes.suggestionsList,
          suggestion: classes.suggestion
        }}
        renderSuggestionsContainer={options => (
          <Popper anchorEl={this.popperNode} open={Boolean(options.children)}>
            <Paper
              square
              {...options.containerProps}
              style={{
                width: this.popperNode ? this.popperNode.clientWidth : null
              }}
            >
              {options.children}
            </Paper>
          </Popper>
        )}
      />
    );
  }
}

IntegrationAutosuggest.propTypes = {
  classes: PropTypes.object.isRequired,
  placeholder: PropTypes.string
};

IntegrationAutosuggest.defaultProps = {
  placeholder: undefined
};

export default withStyles(styles)(IntegrationAutosuggest);
