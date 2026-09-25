// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui.table;

import javafx.scene.control.TableCell;
import org.panteleyev.stamps.desktop.model.CollectionItem;
import org.panteleyev.stamps.desktop.util.DtoUtils;
import org.panteleyev.stamps.dto.BlockDTO;
import org.panteleyev.stamps.dto.StampDTO;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.stream.Collectors;

public class ItemDenominationCell extends TableCell<CollectionItem, CollectionItem> {
    @Override
    protected void updateItem(CollectionItem item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);

        if (item == null || empty) return;

        var text = "";
        if (item.getData() instanceof BlockDTO block) {
            if (block.getStamps().isEmpty()) {
                var denomination = item.getDenomination();
                text = denomination == null ? "" : denomination.toString();
            } else {
                text = block.getStamps().stream()
                        .map(StampDTO::getDenomination)
                        .filter(Objects::nonNull)
                        .map(DtoUtils::normalize)
                        .map(BigDecimal::toString)
                        .collect(Collectors.joining(" + "));
            }
        } else {
            var denomination = item.getDenomination();
            text = denomination == null ? "" : denomination.toString();
        }
        setText(text);
    }
}
