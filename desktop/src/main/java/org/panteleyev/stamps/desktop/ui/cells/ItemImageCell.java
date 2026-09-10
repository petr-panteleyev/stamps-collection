// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui.cells;

import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeTableCell;
import javafx.scene.image.ImageView;
import org.panteleyev.stamps.dto.BlockDTO;
import org.panteleyev.stamps.dto.IssueItemDTO;
import org.panteleyev.stamps.dto.StampDTO;

import static org.panteleyev.stamps.desktop.GlobalContext.stampsService;
import static org.panteleyev.stamps.desktop.ui.Styles.TABLE_IMAGE_SIZE;
import static org.panteleyev.stamps.desktop.ui.Styles.TOOLTIP_BLOCK_IMAGE_SIZE;
import static org.panteleyev.stamps.desktop.ui.Styles.TOOLTIP_STAMP_IMAGE_SIZE;

public class ItemImageCell extends TreeTableCell<Object, Object> {
    @Override
    protected void updateItem(Object item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);
        setGraphic(null);
        if (item == null || empty) return;

        if (item instanceof IssueItemDTO issueItem) {
            var image = stampsService().getImage(issueItem.getId());
            if (image == null) return;

            var view = new ImageView(image);
            view.setPreserveRatio(true);
            view.setFitWidth(TABLE_IMAGE_SIZE);
            view.setFitHeight(TABLE_IMAGE_SIZE);

            var tooltip = new Tooltip();
            var tpView = new ImageView(image);
            tpView.setPreserveRatio(true);
            if (issueItem instanceof BlockDTO) {
                tpView.setFitWidth(TOOLTIP_BLOCK_IMAGE_SIZE);
                tpView.setFitHeight(TOOLTIP_BLOCK_IMAGE_SIZE);
            } else {
                tpView.setFitWidth(TOOLTIP_STAMP_IMAGE_SIZE);
                tpView.setFitHeight(TOOLTIP_STAMP_IMAGE_SIZE);
            }
            tooltip.setGraphic(tpView);


            this.setTooltip(tooltip);

            setGraphic(view);
        }
    }
}
