// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui.tags;

import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import org.controlsfx.validation.ValidationSupport;
import org.panteleyev.fx.BaseDialog;
import org.panteleyev.stamps.desktop.model.CollectionTag;

import java.util.List;

import static javafx.application.Platform.runLater;
import static org.panteleyev.fx.factories.LabelFactory.label;
import static org.panteleyev.fx.factories.grid.GridPaneFactory.gridPane;
import static org.panteleyev.fx.factories.grid.GridRow.gridRow;
import static org.panteleyev.stamps.desktop.ui.MainWindowController.UI;
import static org.panteleyev.stamps.desktop.ui.Validators.STRING_NOT_EMPTY_VALIDATOR;

public class TagDialog extends BaseDialog<CollectionTag> {
    private final TextField nameEdit = new TextField();

    private final ValidationSupport validation = new ValidationSupport();

    public TagDialog(CollectionTag tag) {
        setTitle("Тег");

        nameEdit.setText(tag.name());

        var grid = gridPane(List.of(
                gridRow(label("Название:"), nameEdit)
        ));
        getDialogPane().setContent(grid);
        createDefaultButtons(UI);

        setResultConverter(buttonType -> {
            if (buttonType != ButtonType.OK) return null;
            return new CollectionTag(tag.id(), nameEdit.getText());
        });

        runLater(() -> {
            createValidationSupport();
            nameEdit.requestFocus();
        });
    }

    private void createValidationSupport() {
        validation.registerValidator(nameEdit, STRING_NOT_EMPTY_VALIDATOR);
        validation.initInitialDecoration();
    }
}
