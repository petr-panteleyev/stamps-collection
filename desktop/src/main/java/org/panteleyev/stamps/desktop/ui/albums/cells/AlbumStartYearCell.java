// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui.albums.cells;

import javafx.scene.control.TreeTableCell;
import org.panteleyev.stamps.dto.AlbumDTO;

public class AlbumStartYearCell extends TreeTableCell<AlbumDTO, AlbumDTO> {
    @Override
    protected void updateItem(AlbumDTO item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);
        if (item == null || empty || item.getStartYear() == null) return;
        setText(item.getStartYear().toString());
    }
}
