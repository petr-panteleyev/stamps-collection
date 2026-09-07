// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui.cells;

import javafx.scene.control.TreeTableCell;
import org.panteleyev.stamps.dto.BlockDTO;
import org.panteleyev.stamps.dto.StampDTO;

import java.util.List;
import java.util.stream.Collectors;

public class ItemTagsCell extends TreeTableCell<Object, Object> {
    @Override
    protected void updateItem(Object item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);
        if (item == null || empty) return;

        var tags = switch (item) {
            case StampDTO stamp -> getTags(stamp.getTags());
            case BlockDTO block -> getTags(block.getTags());
            default -> null;
        };
        setText(tags);
    }

    private String getTags(List<String> tags) {
        return tags.stream()
                .sorted()
                .collect(Collectors.joining(","));
    }
}
