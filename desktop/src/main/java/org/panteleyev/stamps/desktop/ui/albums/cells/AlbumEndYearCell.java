// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui.albums.cells;

import javafx.scene.control.TreeTableCell;
import org.panteleyev.stamps.dto.AlbumDTO;

public class AlbumEndYearCell extends TreeTableCell<AlbumDTO, AlbumDTO> {
    @Override
    protected void updateItem(AlbumDTO item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);
        if (item == null || empty || item.getEndYear() == null) return;
        setText(item.getEndYear().toString());
    }
}
