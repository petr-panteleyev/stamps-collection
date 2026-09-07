// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui.cells;

import javafx.scene.control.TreeTableCell;
import org.panteleyev.stamps.dto.BlockDTO;
import org.panteleyev.stamps.dto.CouplingDTO;
import org.panteleyev.stamps.dto.StampDTO;

import static org.panteleyev.stamps.desktop.util.StringUtil.CHECK_SYMBOL;

public class ItemReplacementRequiredCell extends TreeTableCell<Object, Object> {
    @Override
    protected void updateItem(Object item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);
        if (item == null || empty) return;

        var replacementRequired = switch (item) {
            case StampDTO stamp -> stamp.getReplacementRequired();
            case BlockDTO block -> block.getReplacementRequired();
            case CouplingDTO coupling -> coupling.getReplacementRequired();
            default -> false;
        };
        var text = replacementRequired ? CHECK_SYMBOL : "";
        setText(text);
    }
}
