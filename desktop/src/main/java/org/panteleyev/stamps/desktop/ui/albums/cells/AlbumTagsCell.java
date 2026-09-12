// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui.albums.cells;

import javafx.scene.control.TreeTableCell;
import org.panteleyev.stamps.desktop.util.DtoUtils;
import org.panteleyev.stamps.dto.AlbumDTO;

public class AlbumTagsCell extends TreeTableCell<AlbumDTO, AlbumDTO> {
    @Override
    protected void updateItem(AlbumDTO item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);
        if (item == null || empty) return;
        var tags = String.join(",", DtoUtils.normalize(item.getTags()));
        setText(tags);
    }
}
