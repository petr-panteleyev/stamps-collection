// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import org.panteleyev.fx.factories.TableFactory;
import org.panteleyev.stamps.desktop.model.CollectionItem;
import org.panteleyev.stamps.desktop.ui.table.ItemCommentCell;
import org.panteleyev.stamps.desktop.ui.table.ItemDenominationCell;
import org.panteleyev.stamps.desktop.ui.table.ItemDescriptionCell;
import org.panteleyev.stamps.desktop.ui.table.ItemHasCancelledCell;
import org.panteleyev.stamps.desktop.ui.table.ItemHasCleanCell;
import org.panteleyev.stamps.desktop.ui.table.ItemImageCell;
import org.panteleyev.stamps.desktop.ui.table.ItemNumberCfaCell;
import org.panteleyev.stamps.desktop.ui.table.ItemNumberZagCell;
import org.panteleyev.stamps.desktop.ui.table.ItemReplacementRequiredCell;
import org.panteleyev.stamps.desktop.ui.table.ItemTableRow;
import org.panteleyev.stamps.desktop.ui.table.ItemYearCell;
import org.panteleyev.stamps.dto.IssueDTO;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import static org.panteleyev.stamps.desktop.ui.Styles.TABLE_IMAGE_SIZE;
import static org.panteleyev.stamps.desktop.util.DtoUtils.ISSUE_COMPARATOR_BY_DATE;

public class CollectionTableView extends TableView<CollectionItem> {
    private final ObservableList<CollectionItem> list = FXCollections.observableArrayList();

    private final ObjectProperty<Predicate<CollectionItem>> tableItemPredicateProperty =
            new SimpleObjectProperty<>(_ -> true);

    public CollectionTableView() {
        var filteredList = list.filtered(tableItemPredicateProperty.get());
        filteredList.predicateProperty().bind(tableItemPredicateProperty);
        setItems(filteredList);

        var w = widthProperty().subtract(20);

        var yearColumn = TableFactory.<CollectionItem>tableObjectColumn("Год");
        yearColumn.setCellFactory(_ -> new ItemYearCell());
        yearColumn.widthBinding(w.multiply(0.05));

        var numberZagColumn = TableFactory.<CollectionItem>tableObjectColumn("Заг.");
        numberZagColumn.setCellFactory(_ -> new ItemNumberZagCell());
        numberZagColumn.widthBinding(w.multiply(0.05));

        var numberCfaColumn = TableFactory.<CollectionItem>tableObjectColumn("ЦФА");
        numberCfaColumn.setCellFactory(_ -> new ItemNumberCfaCell());
        numberCfaColumn.widthBinding(w.multiply(0.05));

        var imageColumn = TableFactory.<CollectionItem>tableObjectColumn("");
        imageColumn.setCellFactory(_ -> new ItemImageCell());
        imageColumn.setMinWidth(TABLE_IMAGE_SIZE + 10);

        var denominationColumn = TableFactory.<CollectionItem>tableObjectColumn("");
        denominationColumn.setCellFactory(_ -> new ItemDenominationCell());
        denominationColumn.widthBinding(w.multiply(0.05));

        var titleColumn = TableFactory.<CollectionItem>tableObjectColumn("Описание");
        titleColumn.setCellFactory(_ -> new ItemDescriptionCell());
        titleColumn.widthBinding(w.multiply(0.40));

        var replacementRequiredColumn = TableFactory.<CollectionItem>tableObjectColumn("З");
        replacementRequiredColumn.setCellFactory(_ -> new ItemReplacementRequiredCell());
        replacementRequiredColumn.widthBinding(w.multiply(0.05));

        var hasCleanColumn = TableFactory.<CollectionItem>tableObjectColumn("Ч");
        hasCleanColumn.setCellFactory(_ -> new ItemHasCleanCell());
        hasCleanColumn.widthBinding(w.multiply(0.05));

        var hasCancelledColumn = TableFactory.<CollectionItem>tableObjectColumn("Г");
        hasCancelledColumn.setCellFactory(_ -> new ItemHasCancelledCell());
        hasCancelledColumn.widthBinding(w.multiply(0.05));

        var commentColumn = TableFactory.<CollectionItem>tableObjectColumn("Комментарий");
        commentColumn.setCellFactory(_ -> new ItemCommentCell());
        commentColumn.widthBinding(w.multiply(0.15));

        getColumns().addAll(List.of(
                yearColumn,
                numberZagColumn,
                numberCfaColumn,
                imageColumn,
                denominationColumn,
                titleColumn,
                hasCleanColumn,
                hasCancelledColumn,
                replacementRequiredColumn,
                commentColumn
        ));

        setRowFactory(_ -> new ItemTableRow());
    }

    public void setIssues(Collection<IssueDTO> issues) {
        list.clear();
        issues.stream()
                .sorted(ISSUE_COMPARATOR_BY_DATE)
                .forEach(issue -> {
                    list.add(new CollectionItem(issue, issue));
                    for (var stamp : issue.getStamps()) {
                        list.add(new CollectionItem(stamp, issue));
                    }
                    for (var coupling : issue.getCouplings()) {
                        list.add(new CollectionItem(coupling, issue));
                    }
                    for (var block : issue.getBlocks()) {
                        list.add(new CollectionItem(block, issue));
                    }
                });
    }

    public void showAll() {
        tableItemPredicateProperty.set(_ -> true);
    }

    public void showPresent() {
        tableItemPredicateProperty.set(x -> x.getHasClean() || x.getHasCancelled());
    }

    public void showMissing() {
        tableItemPredicateProperty.set(x -> !x.getHasCancelled()
                && !x.getHasClean()
                && !x.getReplacementRequired());
    }

    public void showReplacement() {
        tableItemPredicateProperty.set(CollectionItem::getHasCancelled);
    }
}
