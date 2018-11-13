import React from "react";
import Button from "@material-ui/core/Button";
import Dialog from "@material-ui/core/Dialog";
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import DialogTitle from "@material-ui/core/DialogTitle";
import CloseIcon from "@material-ui/icons/Close";
import withMobileDialog from "@material-ui/core/withMobileDialog";

import isEmpty from "lodash/isEmpty";
import { compose } from "redux";
import { translate } from "react-i18next";
import { connect } from "react-redux";
import { closeDatasheet } from "../redux/actions";
import {
  getDatasheetDialogDatasheet,
  getDatasheetDialogIsOpen
} from "../redux/selectors";

import RawText from "./common/RawText";
import { SnippetTable } from "./Snippet";
import { ResultTitle } from "./ResultList";

import config from "../config";
const { datasheetFormat } = config;

const RawDataSheetSection = ({ t, section, datasheet }) => {
  const content = datasheet && datasheet[section.dataKey];
  if (!content || isEmpty(content)) return null;
  return (
    <div key={section.name}>
      <h3>{t(`${datasheetFormat.translationKeyPrefix}.${section.name}`)}</h3>
      <RawText text={content} />
    </div>
  );
};

const DataSheetSection = translate()(RawDataSheetSection);

class DatasheetDialog extends React.Component {
  handleClose = () => {
    const { closeDatasheet } = this.props;
    closeDatasheet();
  };

  render() {
    const { isOpen, t, datasheet = {}, fullScreen } = this.props;
    return (
      <Dialog
        fullScreen={fullScreen}
        open={isOpen}
        onClose={this.handleClose}
        aria-labelledby="dialog-title"
        aria-describedby="dialog-description"
        fullWidth
        maxWidth="md"
      >
        <DialogActions style={{ marginBottom: -36 }}>
          <Button onClick={this.handleClose} color="primary">
            <CloseIcon />
          </Button>
        </DialogActions>
        <DialogTitle id="dialog-title">
          <ResultTitle item={datasheet} />
        </DialogTitle>
        {datasheet && (
          <DialogContent>
            <SnippetTable item={datasheet} />
            {datasheetFormat.sections.map(section => (
              <DataSheetSection
                key={section.name}
                section={section}
                datasheet={datasheet}
              />
            ))}
          </DialogContent>
        )}
        <DialogActions>
          <Button onClick={this.handleClose} color="primary" autoFocus>
            {t("datasheetdialog.close")}
          </Button>
        </DialogActions>
      </Dialog>
    );
  }
}

const mapStateToProps = state => ({
  datasheet: getDatasheetDialogDatasheet(state),
  isOpen: getDatasheetDialogIsOpen(state)
});

const mapDispatchToProps = {
  closeDatasheet
};

export default compose(
  translate(),
  connect(
    mapStateToProps,
    mapDispatchToProps
  ),
  withMobileDialog()
)(DatasheetDialog);
