// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui.table;

import javafx.scene.control.TableCell;
import javafx.scene.image.ImageView;
import org.panteleyev.stamps.desktop.model.CollectionItem;
import org.panteleyev.stamps.desktop.ui.Picture;
import org.panteleyev.stamps.desktop.util.DtoUtils;
import org.panteleyev.stamps.dto.IssueItemDTO;

import static org.panteleyev.stamps.desktop.ui.Styles.CSS_ISSUE_TITLE;

public class ItemDescriptionCell extends TableCell<CollectionItem, CollectionItem> {
    @Override
    protected void updateItem(CollectionItem item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);
        setGraphic(null);
        getStyleClass().remove(CSS_ISSUE_TITLE);
        if (item == null || empty) return;
        if (item.isIssue()) {
            getStyleClass().add(CSS_ISSUE_TITLE);
        }
        setText(item.getDescription());

        if (item.getNoPerforation()) {
            var view = new ImageView(Picture.SQUARE.getImage());
            view.setFitWidth(16);
            view.setFitHeight(16);
            setGraphic(view);
        }
    }
}
