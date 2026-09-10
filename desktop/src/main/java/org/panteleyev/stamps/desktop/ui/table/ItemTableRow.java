// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui.table;

import javafx.scene.control.TableRow;
import org.panteleyev.stamps.desktop.model.CollectionItem;

import static org.panteleyev.stamps.desktop.ui.Styles.CSS_HAS_CANCELLED;
import static org.panteleyev.stamps.desktop.ui.Styles.CSS_HAS_CLEAN;
import static org.panteleyev.stamps.desktop.ui.Styles.CSS_MISSING;

public class ItemTableRow extends TableRow<CollectionItem> {
    @Override
    protected void updateItem(CollectionItem item, boolean empty) {
        super.updateItem(item, empty);

        getStyleClass().removeAll(CSS_HAS_CLEAN, CSS_HAS_CANCELLED, CSS_MISSING);
        if (item == null || empty || item.isIssue()) return;

        var style = CSS_MISSING;
        if (item.getReplacementRequired()) {
            style = CSS_HAS_CANCELLED;
        } else if (item.getHasClean()) {
            style = CSS_HAS_CLEAN;
        } else if (item.getHasCancelled()) {
            style = CSS_HAS_CANCELLED;
        }
        getStyleClass().add(style);
    }
}
