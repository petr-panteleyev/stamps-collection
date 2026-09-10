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

public class ItemHasCleanCell extends TableCell<CollectionItem, CollectionItem> {

    @Override
    protected void updateItem(CollectionItem item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);
        setGraphic(null);
        if (item == null || empty || item.isIssue()) return;

        var checkBox = new CheckBox();
        checkBox.setSelected(item.getHasClean());
        checkBox.setOnAction(_ -> {
            var patched = stampsService().patchItem(item.getData(), new ItemPatchDTO()
                    .hasClean(checkBox.isSelected()));
            if (patched instanceof IssueItemDTO issueItem) {
                item.setHasClean(DtoUtils.normalize(issueItem.getHasClean()));
            }
            this.getTableView().refresh();
        });
        setGraphic(checkBox);
    }
}
