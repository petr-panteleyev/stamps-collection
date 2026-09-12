// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui.table;

import javafx.scene.control.TableCell;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import org.panteleyev.stamps.desktop.model.CollectionItem;

import static org.panteleyev.stamps.desktop.GlobalContext.stampsService;
import static org.panteleyev.stamps.desktop.ui.Styles.TABLE_IMAGE_SIZE;
import static org.panteleyev.stamps.desktop.ui.Styles.TOOLTIP_BLOCK_IMAGE_SIZE;
import static org.panteleyev.stamps.desktop.ui.Styles.TOOLTIP_STAMP_IMAGE_SIZE;

public class ItemImageCell extends TableCell<CollectionItem, CollectionItem> {
    @Override
    protected void updateItem(CollectionItem item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);
        setGraphic(null);
        setTooltip(null);
        if (item == null || empty) return;

        if (!item.isIssue()) {
            var image = stampsService().getImage(item.getId());
            if (image == null) return;

            var view = new ImageView(image);
            view.setPreserveRatio(true);
            view.setFitWidth(TABLE_IMAGE_SIZE);
            view.setFitHeight(TABLE_IMAGE_SIZE);

            var tooltip = new Tooltip();
            var tpView = new ImageView(image);
            tpView.setPreserveRatio(true);
            if (item.isBlock() || item.isCoupling()) {
                tpView.setFitWidth(TOOLTIP_BLOCK_IMAGE_SIZE);
                tpView.setFitHeight(TOOLTIP_BLOCK_IMAGE_SIZE);
            } else {
                tpView.setFitWidth(TOOLTIP_STAMP_IMAGE_SIZE);
                tpView.setFitHeight(TOOLTIP_STAMP_IMAGE_SIZE);
            }
            tooltip.setGraphic(tpView);

            setTooltip(tooltip);
            setGraphic(view);
        }
    }
}
