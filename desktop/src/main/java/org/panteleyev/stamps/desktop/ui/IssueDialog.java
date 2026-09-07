// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui;

import javafx.event.ActionEvent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.BorderPane;
import org.panteleyev.fx.BaseDialog;
import org.panteleyev.stamps.desktop.ui.cells.ItemCommentCell;
import org.panteleyev.stamps.desktop.ui.cells.ItemDenominationCell;
import org.panteleyev.stamps.desktop.ui.cells.ItemDescriptionCell;
import org.panteleyev.stamps.desktop.ui.cells.ItemHasCancelledCell;
import org.panteleyev.stamps.desktop.ui.cells.ItemHasCleanCell;
import org.panteleyev.stamps.desktop.ui.cells.ItemNumberCfaCell;
import org.panteleyev.stamps.desktop.ui.cells.ItemNumberZagCell;
import org.panteleyev.stamps.desktop.ui.cells.ItemReplacementRequiredCell;
import org.panteleyev.stamps.desktop.ui.cells.ItemTagsCell;
import org.panteleyev.stamps.dto.BlockDTO;
import org.panteleyev.stamps.dto.CouplingDTO;
import org.panteleyev.stamps.dto.IssueDTO;
import org.panteleyev.stamps.dto.StampDTO;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import static org.panteleyev.fx.factories.BoxFactory.hBox;
import static org.panteleyev.fx.factories.MenuFactory.menuItem;
import static org.panteleyev.fx.factories.TreeTableFactory.treeItem;
import static org.panteleyev.fx.factories.TreeTableFactory.treeTableObjectColumn;
import static org.panteleyev.stamps.desktop.ui.MainWindowController.UI;
import static org.panteleyev.stamps.desktop.util.DtoUtils.STAMP_COMPARATOR_BY_NUMBER_ZAG;
import static org.panteleyev.stamps.desktop.util.DtoUtils.copy;

public class IssueDialog extends BaseDialog<IssueDTO> {
    private final IssueDTO issue;

    private final DatePicker issueDatePicker = new DatePicker();
    private final TextField issueTitleField = new TextField();
    private final TreeTableView<Object> treeTableView = new TreeTableView<>();

    public IssueDialog(IssueDTO issue) {
        setTitle("Выпуск");

        this.issue = issue;

        issueTitleField.setPrefColumnCount(40);

        treeTableView.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        var issueAttrBox = hBox(5, issueDatePicker, issueTitleField);

        var root = new BorderPane();
        root.setTop(issueAttrBox);
        root.setCenter(treeTableView);

        getDialogPane().setContent(root);
        createDefaultButtons(UI);

        setupTreeTable();
        setData(this.issue);

        setResizable(true);

        issueDatePicker.valueProperty().addListener((_, _, newValue) -> issue.setDate(newValue));
        issueTitleField.textProperty().addListener((_, _, newValue) -> issue.setTitle(newValue));

        setResultConverter(buttonType -> {
            if (buttonType != ButtonType.OK) return null;

            return issue.title(issueTitleField.getText())
                    .date(issueDatePicker.getValue());
        });
    }

    private void setData(IssueDTO issue) {
        issueDatePicker.setValue(issue.getDate());
        issueTitleField.setText(issue.getTitle());
        setupTreeViewData(treeTableView, issue);
    }

    private void setupTreeTable() {
        var newStampMenuItem = menuItem("Новая марка", this::onNewStamp);
        newStampMenuItem.setAccelerator(Shortcuts.SHORTCUT_N);
        var newCouplingMenuItem = menuItem("Новая сцепка", this::onNewCoupling);
        var newBlockMenuItem = menuItem("Новый блок", this::onNewBlock);
        var editMenuItem = menuItem("Редактировать...", this::onEdit);
        editMenuItem.setAccelerator(Shortcuts.SHORTCUT_E);
        var deleteMenuItem = menuItem("Удалить...", this::onDelete);
        var menu = new ContextMenu(
                newStampMenuItem,
                newCouplingMenuItem,
                newBlockMenuItem,
                new SeparatorMenuItem(),
                editMenuItem,
                new SeparatorMenuItem(),
                deleteMenuItem
        );
        treeTableView.setContextMenu(menu);

        treeTableView.setShowRoot(false);
        treeTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        var numberZagColumn = treeTableObjectColumn("Заг.");
        numberZagColumn.setCellFactory(_ -> new ItemNumberZagCell());

        var numberCfaColumn = treeTableObjectColumn("ЦФА");
        numberCfaColumn.setCellFactory(_ -> new ItemNumberCfaCell());

        var denominationColumn = treeTableObjectColumn("");
        denominationColumn.setCellFactory(_ -> new ItemDenominationCell());

        var titleColumn = treeTableObjectColumn("Описание");
        titleColumn.setCellFactory(_ -> new ItemDescriptionCell());

        var tagsColumn = treeTableObjectColumn("Теги");
        tagsColumn.setCellFactory(_ -> new ItemTagsCell());

        var replacementRequiredColumn = treeTableObjectColumn("З");
        replacementRequiredColumn.setCellFactory(_ -> new ItemReplacementRequiredCell());

        var hasCleanColumn = treeTableObjectColumn("Ч");
        hasCleanColumn.setCellFactory(_ -> new ItemHasCleanCell());

        var hasCancelledColumn = treeTableObjectColumn("Г");
        hasCancelledColumn.setCellFactory(_ -> new ItemHasCancelledCell());

        var commentColumn = treeTableObjectColumn("Комментарий");
        commentColumn.setCellFactory(_ -> new ItemCommentCell());

        treeTableView.getColumns().addAll(List.of(
                numberZagColumn,
                numberCfaColumn,
                denominationColumn,
                titleColumn,
                tagsColumn,
                hasCleanColumn,
                hasCancelledColumn,
                replacementRequiredColumn,
                commentColumn
        ));

        setupTreeViewData(treeTableView, issue);
    }

    private void onNewStamp(ActionEvent ignored) {
        var lastStamp = issue.getStamps().stream()
                .max(Comparator.comparingInt(StampDTO::getNumberZag));
        var nextNum = lastStamp.map(s -> s.getNumberZag() + 1).orElse(0);
        var nextDenomination = lastStamp.map(StampDTO::getDenomination).orElse(BigDecimal.ZERO);
        var nextTags = lastStamp.map(StampDTO::getTags).orElseGet(List::of);

        new StampDialog(new StampDTO()
                .numberZag(nextNum)
                .numberCfa(0)
                .denomination(nextDenomination)
                .year(issue.getDate().getYear())
                .tags(nextTags)
        ).showAndWait().ifPresent(stamp -> {
            issue.getStamps().add(stamp);
            setData(issue);
        });
    }

    private void onNewCoupling(ActionEvent ignored) {
        var stamps = getSelectedStamps(treeTableView)                .stream()
                .filter(s -> s.getBlockNumber() == null)
                .toList();
        if (stamps.isEmpty()) return;

        new CouplingDialog(new CouplingDTO().stampNumbers(
                stamps.stream().map(StampDTO::getNumberZag).toList()
        )).showAndWait().ifPresent(coupling -> {
            issue.getCouplings().add(coupling);
            setData(issue);
        });
    }

    private void onNewBlock(ActionEvent ignored) {
        var blockStamps = getSelectedStamps(treeTableView);
        if (blockStamps.isEmpty()) return;

        new BlockDialog(new BlockDTO()
                .numberZag(0)
                .numberCfa(0)
                .year(issue.getDate().getYear())
        ).showAndWait().ifPresent(block -> {
            block.getStamps().addAll(blockStamps);
            for (var stamp : blockStamps) {
                stamp.setBlockNumber(block.getNumberZag());
            }
            issue.getStamps().removeAll(blockStamps);
            issue.getBlocks().add(block);
            setData(issue);
        });
    }

    private void onEdit(ActionEvent ignored) {
        var selected = treeTableView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        switch (selected.getValue()) {
            case StampDTO stamp when stamp.getBlockNumber() == null ->
                    new StampDialog(copy(stamp)).showAndWait().ifPresent(newStamp -> {
                        var index = issue.getStamps().indexOf(stamp);
                        issue.getStamps().set(index, newStamp);
                        selected.setValue(newStamp);
                    });
            case BlockDTO block -> new BlockDialog(copy(block)).showAndWait().ifPresent(newBlock -> {
                var index = issue.getBlocks().indexOf(block);
                issue.getBlocks().set(index, newBlock);
                selected.setValue(newBlock);
            });
            case CouplingDTO coupling -> new CouplingDialog(copy(coupling)).showAndWait().ifPresent(newCoupling -> {
                var index = issue.getCouplings().indexOf(coupling);
                issue.getCouplings().set(index, newCoupling);
                selected.setValue(newCoupling);
            });
            default -> {
            }
        }
    }

    private void onDelete(ActionEvent ignored) {
        var selected = treeTableView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        switch (selected.getValue()) {
            case StampDTO stamp when stamp.getBlockNumber() == null -> {
                issue.getStamps().remove(stamp);
                setData(issue);
            }
            case StampDTO stamp when stamp.getBlockNumber() != null -> {

            }
            case BlockDTO block -> {

            }
            case CouplingDTO coupling -> {

            }
            default -> {
            }
        }
    }

    private static void setupTreeViewData(TreeTableView<Object> view, IssueDTO issue) {
        var root = treeItem();

        for (var stamp : issue.getStamps()) {
            root.getChildren().add(treeItem(stamp));
        }

        for (var coupling : issue.getCouplings()) {
            root.getChildren().add(treeItem(coupling));
        }

        for (var block : issue.getBlocks()) {
            var blockItem = treeItem((Object) block);
            blockItem.setExpanded(true);
            root.getChildren().add(blockItem);

            for (var stamp : block.getStamps()) {
                blockItem.getChildren().add(treeItem(stamp));
            }
        }

        view.setRoot(root);
    }

    private static List<StampDTO> getSelectedStamps(TreeTableView<?> treeTableView) {
        return treeTableView.getSelectionModel().getSelectedItems().stream()
                .map(TreeItem::getValue)
                .filter(obj -> obj instanceof StampDTO)
                .map(obj -> (StampDTO) obj)
                .filter(stamp -> stamp.getBlockNumber() == null)
                .sorted(STAMP_COMPARATOR_BY_NUMBER_ZAG)
                .toList();
    }
}
