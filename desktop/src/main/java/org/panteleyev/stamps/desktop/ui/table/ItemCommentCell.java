// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui.table;

import javafx.application.Platform;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import org.panteleyev.stamps.desktop.model.CollectionItem;
import org.panteleyev.stamps.dto.IssueItemDTO;
import org.panteleyev.stamps.dto.ItemPatchDTO;

import static org.panteleyev.stamps.desktop.GlobalContext.stampsService;

public class ItemCommentCell extends TableCell<CollectionItem, CollectionItem> {
    private final TextField textField = new TextField();

    public ItemCommentCell() {
        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                var item = getItem();
                var patched = stampsService().patchItem(item.getData(), new ItemPatchDTO()
                        .comment(textField.getText()));
                if (patched instanceof IssueItemDTO issueItem) {
                    item.setComment(issueItem.getComment());
                }
                commitEdit(item);
                event.consume();
            } else if (event.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
                event.consume();
            }
        });
    }

    @Override
    protected void updateItem(CollectionItem item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);
        if (item == null || empty) return;

        if (isEditing()) {
            textField.setText(item.getComment());
            setGraphic(textField);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        } else {
            setupCellView();
        }
    }

    @Override
    public void startEdit() {
        var item = getTableRow().getItem();
        if (item == null || !item.isEditable()) return;

        super.startEdit();
        setGraphic(textField);
        textField.setText(getItem().getComment());
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        Platform.runLater(() -> {
            textField.requestFocus();
            textField.selectAll();
        });
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setupCellView();
    }

    private void setupCellView() {
        var item = getTableRow().getItem();
        if (item == null) return;
        setGraphic(null);
        setText(item.getComment());
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }
}
