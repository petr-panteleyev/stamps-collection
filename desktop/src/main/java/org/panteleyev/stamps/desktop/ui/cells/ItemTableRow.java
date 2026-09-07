// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui.cells;

import javafx.scene.control.TreeTableRow;
import org.panteleyev.stamps.dto.IssueItemDTO;

import java.util.Objects;

import static org.panteleyev.stamps.desktop.ui.Styles.CSS_HAS_CANCELLED;
import static org.panteleyev.stamps.desktop.ui.Styles.CSS_HAS_CLEAN;
import static org.panteleyev.stamps.desktop.ui.Styles.CSS_MISSING;

public class ItemTableRow extends TreeTableRow<Object> {
    @Override
    protected void updateItem(Object item, boolean empty) {
        super.updateItem(item, empty);

        getStyleClass().removeAll(CSS_HAS_CLEAN, CSS_HAS_CANCELLED, CSS_MISSING);
        if (item == null || empty) return;

        if (item instanceof IssueItemDTO issueItem) {
            var style = CSS_MISSING;
            if (Objects.equals(issueItem.getReplacementRequired(), true)) {
                style = CSS_HAS_CANCELLED;
            } else if (Objects.equals(issueItem.getHasClean(), true)) {
                style = CSS_HAS_CLEAN;
            } else if (Objects.equals(issueItem.getHasCancelled(), true)) {
                style = CSS_HAS_CANCELLED;
            }
            getStyleClass().add(style);
        }
    }
}
