// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui.cells;

import javafx.scene.control.TreeTableCell;
import org.panteleyev.stamps.dto.BlockDTO;
import org.panteleyev.stamps.dto.StampDTO;

public class ItemNumberZagCell extends TreeTableCell<Object, Object> {
    @Override
    protected void updateItem(Object item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);
        if (item == null || empty) return;

        var text = switch (item) {
            case StampDTO stamp -> stamp.getNumberZag().toString();
            case BlockDTO block -> "Бл " + block.getNumberZag().toString();
            default -> "";
        };
        setText(text);
    }
}
