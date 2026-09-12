// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui;

import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.validation.ValidationSupport;
import org.panteleyev.fx.BaseDialog;
import org.panteleyev.stamps.dto.StampDTO;
import org.panteleyev.stamps.dto.TagDTO;

import java.util.List;

import static javafx.application.Platform.runLater;
import static org.panteleyev.fx.factories.LabelFactory.label;
import static org.panteleyev.fx.factories.StringFactory.COLON;
import static org.panteleyev.fx.factories.StringFactory.string;
import static org.panteleyev.fx.factories.grid.GridCell.gridCell;
import static org.panteleyev.fx.factories.grid.GridPaneFactory.gridPane;
import static org.panteleyev.fx.factories.grid.GridRow.gridRow;
import static org.panteleyev.stamps.desktop.GlobalContext.stampsService;
import static org.panteleyev.stamps.desktop.ui.MainWindowController.UI;
import static org.panteleyev.stamps.desktop.ui.Validators.DECIMAL_VALIDATOR;
import static org.panteleyev.stamps.desktop.ui.Validators.INTEGER_OR_ZERO_VALIDATOR;
import static org.panteleyev.stamps.desktop.ui.Validators.INTEGER_VALIDATOR;
import static org.panteleyev.stamps.desktop.ui.Validators.STRING_NOT_EMPTY_VALIDATOR;
import static org.panteleyev.stamps.desktop.util.StringUtil.toBigDecimalOrNull;
import static org.panteleyev.stamps.desktop.util.StringUtil.toIntOrNull;
import static org.panteleyev.stamps.desktop.util.StringUtil.toStringOrEmpty;

public class StampDialog extends BaseDialog<StampDTO> {
    private final StampDTO stamp;

    private final TextField zagNumberEdit = new TextField();
    private final TextField cfaNumberEdit = new TextField();
    private final TextField denominationEdit = new TextField();
    private final TextField descriptionEdit = new TextField();
    private final CheckBox hasCleanCheckBox = new CheckBox("Чистая");
    private final CheckBox hasCancelledCheckBox = new CheckBox("Гашёная");
    private final CheckBox replacementCheckBox = new CheckBox("Требуется замена");
    private final TextField commentEdit = new TextField();
    private final CheckComboBox<String> tagsComboBox = new CheckComboBox<>();

    private final ValidationSupport validation = new ValidationSupport();

    public StampDialog(StampDTO stamp) {
        this.stamp = stamp;

        setTitle("Марка");

        var root = gridPane(List.of(
                gridRow(label(string("Номер по Загорскому", COLON)), zagNumberEdit),
                gridRow(label(string("Номер по ЦФА", COLON)), cfaNumberEdit),
                gridRow(label(string("Номинал", COLON)), denominationEdit),
                gridRow(label(string("Описание", COLON)), descriptionEdit),
                gridRow(gridCell(hasCleanCheckBox, 2, 1)),
                gridRow(gridCell(hasCancelledCheckBox, 2, 1)),
                gridRow(gridCell(replacementCheckBox, 2, 1)),
                gridRow(label(string("Комментарий", COLON)), commentEdit),
                gridRow(label(string("Теги", COLON)), tagsComboBox)
        ));
        getDialogPane().setContent(root);

        createDefaultButtons(UI);
        var okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.disableProperty().bind(validation.invalidProperty());
        setupData();

        setResultConverter(buttonType -> {
            if (buttonType != ButtonType.OK) return null;
            return stamp.numberZag(toIntOrNull(zagNumberEdit.getText()))
                    .numberCfa(toIntOrNull(cfaNumberEdit.getText()))
                    .denomination(toBigDecimalOrNull(denominationEdit.getText()))
                    .description(descriptionEdit.getText())
                    .hasClean(hasCleanCheckBox.isSelected())
                    .hasCancelled(hasCancelledCheckBox.isSelected())
                    .replacementRequired(replacementCheckBox.isSelected())
                    .comment(commentEdit.getText())
                    .tags(tagsComboBox.getCheckModel().getCheckedItems());
        });

        runLater(() -> {
            createValidationSupport();
            zagNumberEdit.requestFocus();
        });
    }

    private void setupData() {
        zagNumberEdit.setText(toStringOrEmpty(stamp.getNumberZag()));
        cfaNumberEdit.setText(toStringOrEmpty(stamp.getNumberCfa()));
        denominationEdit.setText(toStringOrEmpty(stamp.getDenomination()));
        descriptionEdit.setText(stamp.getDescription());
        hasCleanCheckBox.setSelected(stamp.getHasClean() != null && stamp.getHasClean());
        hasCancelledCheckBox.setSelected(stamp.getHasCancelled() != null && stamp.getHasCancelled());
        replacementCheckBox.setSelected(stamp.getReplacementRequired() != null && stamp.getReplacementRequired());
        commentEdit.setText(stamp.getComment());

        var allTags = stampsService().loadTags().stream().map(TagDTO::getName).sorted().toList();
        tagsComboBox.getItems().setAll(FXCollections.observableArrayList(allTags));
        for (var i = 0; i < allTags.size(); i++) {
            if (stamp.getTags().contains(allTags.get(i))) {
                tagsComboBox.getCheckModel().check(i);
            }
        }
    }

    private void createValidationSupport() {
        validation.registerValidator(zagNumberEdit, INTEGER_VALIDATOR);
        validation.registerValidator(cfaNumberEdit, INTEGER_OR_ZERO_VALIDATOR);
        validation.registerValidator(descriptionEdit, STRING_NOT_EMPTY_VALIDATOR);
        validation.registerValidator(denominationEdit, DECIMAL_VALIDATOR);
        validation.initInitialDecoration();
    }
}
