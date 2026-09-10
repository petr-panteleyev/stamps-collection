// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui.table;

import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import org.panteleyev.stamps.desktop.model.CollectionItem;
import org.panteleyev.stamps.desktop.util.DtoUtils;
import org.panteleyev.stamps.dto.IssueItemDTO;
import org.panteleyev.stamps.dto.ItemPatchDTO;

import static org.panteleyev.stamps.desktop.GlobalContext.stampsService;

public class ItemReplacementRequiredCell extends TableCell<CollectionItem, CollectionItem> {
    @Override
    protected void updateItem(CollectionItem item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);
        setGraphic(null);
        if (item == null || empty || item.isIssue()) return;

        var checkBox = new CheckBox();
        checkBox.setSelected(item.getReplacementRequired());
        checkBox.setOnAction(_ -> {
            var patched = stampsService().patchItem(item.getData(), new ItemPatchDTO()
                    .replacementRequired(checkBox.isSelected()));
            if (patched instanceof IssueItemDTO issueItem) {
                item.setReplacementRequired(DtoUtils.normalize(issueItem.getReplacementRequired()));
            }
            this.getTableView().refresh();
        });
        setGraphic(checkBox);
    }
}
