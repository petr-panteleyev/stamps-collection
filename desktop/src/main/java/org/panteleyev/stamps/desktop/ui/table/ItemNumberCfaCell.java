// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui.table;

import javafx.scene.control.TableCell;
import org.panteleyev.stamps.desktop.model.CollectionItem;

public class ItemNumberCfaCell extends TableCell<CollectionItem, CollectionItem> {
    @Override
    protected void updateItem(CollectionItem item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);
        if (item == null || empty) return;

        var number = item.getNumberCfa();
        if (number == 0) return;
        setText(Integer.toString(number));
    }
}
