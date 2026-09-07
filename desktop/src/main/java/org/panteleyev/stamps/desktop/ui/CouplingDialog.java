// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui;

import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import org.panteleyev.fx.BaseDialog;
import org.panteleyev.stamps.dto.CouplingDTO;

import java.util.List;

import static org.panteleyev.fx.factories.LabelFactory.label;
import static org.panteleyev.fx.factories.StringFactory.COLON;
import static org.panteleyev.fx.factories.StringFactory.string;
import static org.panteleyev.fx.factories.grid.GridCell.gridCell;
import static org.panteleyev.fx.factories.grid.GridPaneFactory.gridPane;
import static org.panteleyev.fx.factories.grid.GridRow.gridRow;

public class CouplingDialog extends BaseDialog<CouplingDTO> {
    private final CouplingDTO coupling;

    private final CheckBox hasCleanCheckBox = new CheckBox("Чистый");
    private final CheckBox hasCancelledCheckBox = new CheckBox("Гашёный");
    private final CheckBox replacementCheckBox = new CheckBox("Требуется замена");
    private final TextField commentEdit = new TextField();

    public CouplingDialog(CouplingDTO coupling) {
        this.coupling = coupling;

        setTitle("Блок");

        var root = gridPane(List.of(
                gridRow(gridCell(hasCleanCheckBox, 2, 1)),
                gridRow(gridCell(hasCancelledCheckBox, 2, 1)),
                gridRow(gridCell(replacementCheckBox, 2, 1)),
                gridRow(label(string("Комментарий", COLON)), commentEdit)
        ));
        getDialogPane().setContent(root);

        createDefaultButtons(null);
        setupData();

        setResultConverter(buttonType -> {
            if (buttonType != ButtonType.OK) return null;
            return coupling.hasClean(hasCleanCheckBox.isSelected())
                    .hasCancelled(hasCancelledCheckBox.isSelected())
                    .replacementRequired(replacementCheckBox.isSelected())
                    .comment(commentEdit.getText());
        });
    }

    private void setupData() {
        hasCleanCheckBox.setSelected(coupling.getHasClean() != null && coupling.getHasClean());
        hasCancelledCheckBox.setSelected(coupling.getHasCancelled() != null && coupling.getHasCancelled());
        replacementCheckBox.setSelected(coupling.getReplacementRequired() != null && coupling.getReplacementRequired());
        commentEdit.setText(coupling.getComment());
    }
}
