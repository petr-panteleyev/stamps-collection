// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui.cells;

import javafx.scene.control.TreeTableCell;
import org.panteleyev.stamps.dto.BlockDTO;
import org.panteleyev.stamps.dto.StampDTO;

public class ItemCommentCell extends TreeTableCell<Object, Object> {
    @Override
    protected void updateItem(Object item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);
        if (item == null || empty) return;

        var comment = switch (item) {
            case StampDTO stamp -> stamp.getComment();
            case BlockDTO block -> block.getComment();
            default -> null;
        };
        setText(comment);
    }
}
