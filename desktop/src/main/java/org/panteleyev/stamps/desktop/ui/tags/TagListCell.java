// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui.tags;

import javafx.scene.control.ListCell;
import org.panteleyev.stamps.dto.TagDTO;

class TagListCell extends ListCell<TagDTO> {
    @Override
    protected void updateItem(TagDTO item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);
        if (item == null || empty) return;
        setText(item.getName());
    }
}
