// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui.tags;

import javafx.scene.control.ListCell;
import org.panteleyev.stamps.desktop.model.CollectionTag;

class TagListCell extends ListCell<CollectionTag> {
    @Override
    protected void updateItem(CollectionTag item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);
        if (item == null || empty) return;
        setText(item.name());
    }
}
