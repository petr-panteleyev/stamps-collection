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
import org.panteleyev.stamps.desktop.model.CollectionTag;
import org.panteleyev.stamps.desktop.util.DtoUtils;
import org.panteleyev.stamps.dto.BlockDTO;

import java.util.List;

import static javafx.application.Platform.runLater;
import static org.panteleyev.fx.factories.LabelFactory.label;
import static org.panteleyev.fx.factories.grid.GridCell.gridCell;
import static org.panteleyev.fx.factories.grid.GridPaneFactory.gridPane;
import static org.panteleyev.fx.factories.grid.GridRow.gridRow;
import static org.panteleyev.stamps.desktop.GlobalContext.stampsService;
import static org.panteleyev.stamps.desktop.settings.Settings.settings;
import static org.panteleyev.stamps.desktop.ui.MainWindowController.UI;
import static org.panteleyev.stamps.desktop.ui.Styles.GRID_PANE;
import static org.panteleyev.stamps.desktop.ui.Validators.DECIMAL_VALIDATOR;
import static org.panteleyev.stamps.desktop.ui.Validators.INTEGER_OR_ZERO_VALIDATOR;
import static org.panteleyev.stamps.desktop.ui.Validators.INTEGER_VALIDATOR;
import static org.panteleyev.stamps.desktop.ui.Validators.STRING_NOT_EMPTY_VALIDATOR;
import static org.panteleyev.stamps.desktop.util.StringUtil.toBigDecimalOrNull;
import static org.panteleyev.stamps.desktop.util.StringUtil.toIntOrNull;
import static org.panteleyev.stamps.desktop.util.StringUtil.toStringOrEmpty;

public class BlockDialog extends BaseDialog<BlockDTO> {
    private final BlockDTO block;

    private final TextField zagNumberEdit = new TextField();
    private final TextField cfaNumberEdit = new TextField();
    private final TextField denominationEdit = new TextField();
    private final TextField descriptionEdit = new TextField();
    private final CheckBox noPerforationCheckBox = new CheckBox("Без перфорации");
    private final CheckBox hasCleanCheckBox = new CheckBox("Чистый");
    private final CheckBox hasCancelledCheckBox = new CheckBox("Гашёный");
    private final CheckBox replacementCheckBox = new CheckBox("Требуется замена");
    private final TextField commentEdit = new TextField();
    private final CheckComboBox<String> tagsComboBox = new CheckComboBox<>();

    private final ValidationSupport validation = new ValidationSupport();

    public BlockDialog(BlockDTO block, boolean hasStamps) {
        super(settings().getDialogCssFilePath());

        this.block = block;

        setTitle("Блок");

        var root = gridPane(List.of(
                gridRow(label("Номер по Загорскому:"), zagNumberEdit),
                gridRow(label("Номер по ЦФА:"), cfaNumberEdit),
                gridRow(label("Номинал:"), denominationEdit),
                gridRow(label("Описание:"), descriptionEdit),
                gridRow(gridCell(noPerforationCheckBox, 2, 1)),
                gridRow(label("Комментарий:"), commentEdit),
                gridRow(label("Теги:"), tagsComboBox),
                gridRow(gridCell(label("В наличии:"), 2, 1)),
                gridRow(gridCell(hasCleanCheckBox, 2, 1)),
                gridRow(gridCell(hasCancelledCheckBox, 2, 1)),
                gridRow(gridCell(replacementCheckBox, 2, 1))
        ), List.of(), List.of(GRID_PANE));
        getDialogPane().setContent(root);

        createDefaultButtons(UI);
        var okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.disableProperty().bind(validation.invalidProperty());

        setupData(hasStamps);

        setResultConverter(buttonType -> {
            if (buttonType != ButtonType.OK) return null;
            return block.numberZag(toIntOrNull(zagNumberEdit.getText()))
                    .numberCfa(toIntOrNull(cfaNumberEdit.getText()))
                    .denomination(toBigDecimalOrNull(denominationEdit.getText()))
                    .description(descriptionEdit.getText())
                    .noPerforation(noPerforationCheckBox.isSelected())
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

    private void setupData(boolean hasStamps) {
        zagNumberEdit.setText(toStringOrEmpty(block.getNumberZag()));
        cfaNumberEdit.setText(toStringOrEmpty(block.getNumberCfa()));
        descriptionEdit.setText(block.getDescription());
        noPerforationCheckBox.setSelected(DtoUtils.normalize(block.getNoPerforation()));
        hasCleanCheckBox.setSelected(DtoUtils.normalize(block.getHasClean()));
        hasCancelledCheckBox.setSelected(DtoUtils.normalize(block.getHasCancelled()));
        replacementCheckBox.setSelected(DtoUtils.normalize(block.getReplacementRequired()));
        commentEdit.setText(block.getComment());

        denominationEdit.setDisable(hasStamps);
        if (hasStamps) {
            denominationEdit.setText("");
        } else {
            denominationEdit.setText(toStringOrEmpty(block.getDenomination()));
        }

        var allTags = stampsService().loadTags().stream().map(CollectionTag::name).sorted().toList();
        tagsComboBox.getItems().setAll(FXCollections.observableArrayList(allTags));
        for (var i = 0; i < allTags.size(); i++) {
            if (block.getTags().contains(allTags.get(i))) {
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
