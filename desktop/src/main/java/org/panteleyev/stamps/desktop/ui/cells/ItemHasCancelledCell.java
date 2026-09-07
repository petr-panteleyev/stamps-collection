// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui.cells;

import javafx.scene.control.TreeTableCell;
import org.panteleyev.stamps.dto.BlockDTO;
import org.panteleyev.stamps.dto.CouplingDTO;
import org.panteleyev.stamps.dto.StampDTO;

import static org.panteleyev.stamps.desktop.util.StringUtil.CHECK_SYMBOL;

public class ItemHasCancelledCell extends TreeTableCell<Object, Object> {
    @Override
    protected void updateItem(Object item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);
        if (item == null || empty) return;

        var hasCancelled = switch (item) {
            case StampDTO stamp -> stamp.getHasCancelled();
            case BlockDTO block -> block.getHasCancelled();
            case CouplingDTO coupling -> coupling.getHasCancelled();
            default -> false;
        };
        var text = hasCancelled ? CHECK_SYMBOL : "";
        setText(text);
    }
}
