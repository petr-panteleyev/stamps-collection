// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui;

import javafx.scene.control.TreeTableView;
import org.panteleyev.stamps.desktop.ui.cells.ItemCommentCell;
import org.panteleyev.stamps.desktop.ui.cells.ItemDenominationCell;
import org.panteleyev.stamps.desktop.ui.cells.ItemDescriptionCell;
import org.panteleyev.stamps.desktop.ui.cells.ItemHasCancelledCell;
import org.panteleyev.stamps.desktop.ui.cells.ItemHasCleanCell;
import org.panteleyev.stamps.desktop.ui.cells.ItemNumberCfaCell;
import org.panteleyev.stamps.desktop.ui.cells.ItemNumberZagCell;
import org.panteleyev.stamps.desktop.ui.cells.ItemReplacementRequiredCell;
import org.panteleyev.stamps.desktop.ui.cells.ItemTableRow;
import org.panteleyev.stamps.desktop.ui.cells.ItemTagsCell;
import org.panteleyev.stamps.desktop.ui.cells.ItemYearCell;
import org.panteleyev.stamps.dto.IssueDTO;

import java.util.Collection;
import java.util.List;

import static org.panteleyev.fx.factories.TreeTableFactory.treeItem;
import static org.panteleyev.fx.factories.TreeTableFactory.treeTableObjectColumn;

public class CollectionTreeView extends TreeTableView<Object> {

    public CollectionTreeView() {
        setShowRoot(false);

        var w = widthProperty().subtract(20);

        var yearColumn = treeTableObjectColumn("Год");
        yearColumn.setCellFactory(_ -> new ItemYearCell());
        yearColumn.widthBinding(w.multiply(0.05));

        var numberZagColumn = treeTableObjectColumn("Заг.");
        numberZagColumn.setCellFactory(_ -> new ItemNumberZagCell());
        numberZagColumn.widthBinding(w.multiply(0.05));

        var numberCfaColumn = treeTableObjectColumn("ЦФА");
        numberCfaColumn.setCellFactory(_ -> new ItemNumberCfaCell());
        numberCfaColumn.widthBinding(w.multiply(0.05));

        var denominationColumn = treeTableObjectColumn("");
        denominationColumn.setCellFactory(_ -> new ItemDenominationCell());
        denominationColumn.widthBinding(w.multiply(0.05));

        var titleColumn = treeTableObjectColumn("Описание");
        titleColumn.setCellFactory(_ -> new ItemDescriptionCell());
        titleColumn.widthBinding(w.multiply(0.50));

//        var tagsColumn = treeTableObjectColumn("Теги");
//        tagsColumn.setCellFactory(_ -> new ItemTagsCell());
//        tagsColumn.widthBinding(w.multiply(0.15));

        var replacementRequiredColumn = treeTableObjectColumn("З");
        replacementRequiredColumn.setCellFactory(_ -> new ItemReplacementRequiredCell());
        replacementRequiredColumn.widthBinding(w.multiply(0.05));

        var hasCleanColumn = treeTableObjectColumn("Ч");
        hasCleanColumn.setCellFactory(_ -> new ItemHasCleanCell());
        hasCleanColumn.widthBinding(w.multiply(0.05));

        var hasCancelledColumn = treeTableObjectColumn("Г");
        hasCancelledColumn.setCellFactory(_ -> new ItemHasCancelledCell());
        hasCancelledColumn.widthBinding(w.multiply(0.05));

        var commentColumn = treeTableObjectColumn("Комментарий");
        commentColumn.setCellFactory(_ -> new ItemCommentCell());
        commentColumn.widthBinding(w.multiply(0.15));

        getColumns().addAll(List.of(
                yearColumn,
                numberZagColumn,
                numberCfaColumn,
                denominationColumn,
                titleColumn,
//                tagsColumn,
                hasCleanColumn,
                hasCancelledColumn,
                replacementRequiredColumn,
                commentColumn
        ));

        setRowFactory(_ -> new ItemTableRow());
    }

    public void setIssues(Collection<IssueDTO> issues) {
        var root = treeItem();

        for (var issue : issues) {
            var issueItem = treeItem((Object) issue);
            issueItem.setExpanded(true);

            for (var stamp : issue.getStamps()) {
                issueItem.getChildren().add(treeItem(stamp));
            }

            for (var coupling : issue.getCouplings()) {
                issueItem.getChildren().add(treeItem(coupling));
            }

            for (var block : issue.getBlocks()) {
                var blockItem = treeItem((Object)block);
                blockItem.setExpanded(true);
                for (var stamp : block.getStamps()) {
                    blockItem.getChildren().add(treeItem(stamp));
                }
                issueItem.getChildren().add(blockItem);
            }

            root.getChildren().add(issueItem);
        }


        setRoot(root);
    }
}
