// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui.table;

import javafx.scene.control.TableCell;
import org.panteleyev.stamps.desktop.model.CollectionItem;
import org.panteleyev.stamps.dto.CouplingDTO;

import java.util.stream.Collectors;

public class ItemDenominationCell extends TableCell<CollectionItem, CollectionItem> {
    @Override
    protected void updateItem(CollectionItem item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);

        if (item == null || empty) return;

        var denomination = item.getDenomination();
        var text = denomination == null ? ""
                : denomination.toString();

        setText(text);
    }

    private static String getCouplingDescription(CouplingDTO coupling) {
        return "Сцепка марок " + coupling.getStampNumbers().stream()
                .map(n -> Integer.toString(n))
                .collect(Collectors.joining(","));
    }
}
