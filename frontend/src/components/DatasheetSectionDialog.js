import React from "react";
import Button from "leoek-material-ui-core-fork/Button";
import Dialog from "leoek-material-ui-core-fork/Dialog";
import DialogActions from "leoek-material-ui-core-fork/DialogActions";
import DialogContent from "leoek-material-ui-core-fork/DialogContent";
import DialogTitle from "leoek-material-ui-core-fork/DialogTitle";
import CloseIcon from "@material-ui/icons/Close";
import withMobileDialog from "leoek-material-ui-core-fork/withMobileDialog";

import { compose } from "redux";
import { translate } from "react-i18next";
import { connect } from "react-redux";
import {
  getDatasheetSectionDialogName,
  getDatasheetSectionDialogDatasheet,
  getDatasheetSectionDialogRaw,
  getDatasheetSectionDialogIsOpen
} from "../redux/selectors/uiSelectors";
import { closeDatasheetSection, showDatasheet } from "../redux/actions";

import RawText from "./common/RawText";
import { SnippetTable } from "./Snippet";
import { ResultTitle } from "./ResultList";

import config from "../config";
import Disclaimer from "./Disclaimer";
const { datasheetFormat } = config;

class DatasheetSectionDialog extends React.Component {
  handleShowDatasheet = () => {
    const { showDatasheet, datasheet } = this.props;
    showDatasheet(datasheet);
  };

  handleClose = () => {
    const { closeDatasheetSection } = this.props;
    closeDatasheetSection();
  };

  render() {
    const {
      isOpen,
      rawSection,
      name,
      t,
      datasheet = {},
      fullScreen
    } = this.props;
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
        <DialogContent>
          {datasheet && <SnippetTable item={datasheet} />}
          <DialogTitle style={{ paddingLeft: 0 }} id="dialog-title-section">
            {t(`${datasheetFormat.translationKeyPrefix}.${name}`)}
          </DialogTitle>
          <RawText text={rawSection} />
          <Disclaimer />
        </DialogContent>
        <DialogActions>
          <Button onClick={this.handleShowDatasheet} color="primary">
            {t("datasheetsectiondialog.show_dataset")}
          </Button>
          <Button onClick={this.handleClose} color="primary" autoFocus>
            {t("datasheetsectiondialog.close")}
          </Button>
        </DialogActions>
      </Dialog>
    );
  }
}

const mapStateToProps = state => ({
  name: getDatasheetSectionDialogName(state),
  datasheet: getDatasheetSectionDialogDatasheet(state),
  rawSection: getDatasheetSectionDialogRaw(state),
  isOpen: getDatasheetSectionDialogIsOpen(state)
});

const mapDispatchToProps = {
  closeDatasheetSection,
  showDatasheet
};

export default compose(
  translate(),
  connect(
    mapStateToProps,
    mapDispatchToProps
  ),
  withMobileDialog()
)(DatasheetSectionDialog);
