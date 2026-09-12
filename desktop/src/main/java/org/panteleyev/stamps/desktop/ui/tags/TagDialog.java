// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui.tags;

import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import org.panteleyev.fx.BaseDialog;
import org.panteleyev.stamps.dto.TagDTO;

import java.util.List;

import static org.panteleyev.fx.factories.LabelFactory.label;
import static org.panteleyev.fx.factories.grid.GridPaneFactory.gridPane;
import static org.panteleyev.fx.factories.grid.GridRow.gridRow;
import static org.panteleyev.stamps.desktop.ui.MainWindowController.UI;

public class TagDialog extends BaseDialog<TagDTO> {
    private final TextField nameEdit = new TextField();

    public TagDialog(TagDTO tag) {
        setTitle("Тег");

        nameEdit.setText(tag.getName());

        var grid = gridPane(List.of(
                gridRow(label("Название:"), nameEdit)
        ));
        getDialogPane().setContent(grid);
        createDefaultButtons(UI);

        setResultConverter(buttonType -> {
            if (buttonType != ButtonType.OK) return null;
            return tag.name(nameEdit.getText());
        });
    }
}
