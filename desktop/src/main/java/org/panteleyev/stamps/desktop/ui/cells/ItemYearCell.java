// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui.cells;

import javafx.scene.control.TreeTableCell;
import org.panteleyev.stamps.dto.BlockDTO;
import org.panteleyev.stamps.dto.StampDTO;

public class ItemYearCell extends TreeTableCell<Object, Object> {
    @Override
    protected void updateItem(Object item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);
        if (item == null || empty) return;

        var year = switch(item) {
            case StampDTO stamp when stamp.getBlockNumber() != null -> null;
            case StampDTO stamp -> stamp.getYear();
            case BlockDTO block -> block.getYear();
            default -> null;
        };
        var text = year == null ? "" : year.toString();
        setText(text);
    }
}
