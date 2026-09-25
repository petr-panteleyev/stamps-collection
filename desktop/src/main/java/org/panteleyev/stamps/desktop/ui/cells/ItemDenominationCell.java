// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui.cells;

import javafx.scene.control.TreeTableCell;
import org.panteleyev.stamps.dto.StampDTO;

import java.math.RoundingMode;

public class ItemDenominationCell extends TreeTableCell<Object, Object> {
    @Override
    protected void updateItem(Object item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);

        if (item == null || empty) return;

        var denomination = switch (item) {
            case StampDTO stamp -> stamp.getDenomination();
            default -> null;
        };

        var text = denomination == null ? ""
                : denomination.setScale(2, RoundingMode.HALF_UP).toString();

        setText(text);
    }
}
