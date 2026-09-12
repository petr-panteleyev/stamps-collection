// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui.albums;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.BorderPane;
import org.panteleyev.fx.BaseDialog;
import org.panteleyev.fx.factories.TreeTableFactory;
import org.panteleyev.stamps.desktop.ui.Shortcuts;
import org.panteleyev.stamps.desktop.ui.albums.cells.AlbumEndYearCell;
import org.panteleyev.stamps.desktop.ui.albums.cells.AlbumExcludedTagsCell;
import org.panteleyev.stamps.desktop.ui.albums.cells.AlbumNameCell;
import org.panteleyev.stamps.desktop.ui.albums.cells.AlbumRegionCell;
import org.panteleyev.stamps.desktop.ui.albums.cells.AlbumStartYearCell;
import org.panteleyev.stamps.desktop.ui.albums.cells.AlbumTagsCell;
import org.panteleyev.stamps.dto.AlbumDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static javafx.event.ActionEvent.ACTION;
import static org.panteleyev.fx.factories.ButtonFactory.buttonType;
import static org.panteleyev.fx.factories.MenuFactory.menuItem;
import static org.panteleyev.fx.factories.TreeTableFactory.treeItem;
import static org.panteleyev.stamps.desktop.GlobalContext.stampsService;
import static org.panteleyev.stamps.desktop.ui.MainWindowController.UI;
import static org.panteleyev.stamps.desktop.util.DtoUtils.ALBUM_COMPARATOR_BY_NAME;

public class AlbumsEditor extends BaseDialog<AlbumEditorResult> {
    private final TreeTableView<AlbumDTO> view = new TreeTableView<>();
    private final TreeItem<AlbumDTO> rootItem = new TreeItem<>();

    private final ObservableList<AlbumDTO> albums = FXCollections.observableArrayList();
    private final SortedList<AlbumDTO> sortedAlbums = albums.sorted(ALBUM_COMPARATOR_BY_NAME);

    private final Map<String, AlbumDTO> toAdd = new HashMap<>();
    private final Map<UUID, AlbumDTO> toUpdate = new HashMap<>();
    private final Map<UUID, AlbumDTO> toDelete = new HashMap<>();

    public AlbumsEditor() {
        setTitle("Альбомы");

        setupView();

        albums.addAll(stampsService().loadAlbums());
        refreshData();
        view.setRoot(rootItem);
        view.setShowRoot(false);

        var root = new BorderPane(view);
        getDialogPane().setContent(root);
        createDefaultButtons(UI);

        var newAlbumButtonType = buttonType("Новый альбом", ButtonBar.ButtonData.LEFT);
        var editAlbumButtonType = buttonType("Редактировать", ButtonBar.ButtonData.LEFT);
        getDialogPane().getButtonTypes().addAll(newAlbumButtonType, editAlbumButtonType);

        getButton(newAlbumButtonType).ifPresent(b -> b.addEventFilter(ACTION, this::onNewAlbum));
        getButton(editAlbumButtonType).ifPresent(b -> b.addEventFilter(ACTION, this::onEditAlbum));

        setResizable(true);

        setResultConverter(buttonType -> {
            if (buttonType != ButtonType.OK) return null;
            return new AlbumEditorResult(toAdd.values(), toUpdate.values(), toDelete.values());
        });
    }

    private void setupView() {
        var newAlbumMenuItem = menuItem("Новый альбом", this::onNewAlbum);
        newAlbumMenuItem.setAccelerator(Shortcuts.SHORTCUT_N);
        var editAlbumMenuItem = menuItem("Редактировать...", this::onEditAlbum);
        editAlbumMenuItem.setAccelerator(Shortcuts.SHORTCUT_E);
        var deleteAlbumMenuItem = menuItem("Удалить...");
        var menu = new ContextMenu(
                newAlbumMenuItem,
                new SeparatorMenuItem(),
                editAlbumMenuItem,
                new SeparatorMenuItem(),
                deleteAlbumMenuItem
        );
        view.setContextMenu(menu);

        var nameColumn = TreeTableFactory.<AlbumDTO>treeTableObjectColumn("Название");
        nameColumn.setCellFactory(_ -> new AlbumNameCell());

        var regionColumn = TreeTableFactory.<AlbumDTO>treeTableObjectColumn("Регион");
        regionColumn.setCellFactory(_ -> new AlbumRegionCell());

        var startYearColumn = TreeTableFactory.<AlbumDTO>treeTableObjectColumn("Начало");
        startYearColumn.setCellFactory(_ -> new AlbumStartYearCell());

        var endYearColumn = TreeTableFactory.<AlbumDTO>treeTableObjectColumn("Конец");
        endYearColumn.setCellFactory(_ -> new AlbumEndYearCell());

        var tagsColumn = TreeTableFactory.<AlbumDTO>treeTableObjectColumn("Теги");
        tagsColumn.setCellFactory(_ -> new AlbumTagsCell());

        var excludedTagsColumn = TreeTableFactory.<AlbumDTO>treeTableObjectColumn("Искл. теги");
        excludedTagsColumn.setCellFactory(_ -> new AlbumExcludedTagsCell());

        view.getColumns().setAll(List.of(
                nameColumn,
                regionColumn,
                startYearColumn,
                endYearColumn,
                tagsColumn,
                excludedTagsColumn
        ));
    }

    private void refreshData() {
        rootItem.getChildren().clear();
        for (var album : sortedAlbums) {
            var item = treeItem(album);
            item.setExpanded(true);
            rootItem.getChildren().add(item);

            for (var subalbum : album.getSubalbums()) {
                var subItem = treeItem(subalbum);
                item.getChildren().add(subItem);
            }
        }
        view.refresh();
    }

    private void onNewAlbum(ActionEvent event) {
        event.consume();

        var selected = view.getSelectionModel().getSelectedItem();
        var album = new AlbumDTO()
                .name("Новый альбом")
                .noTags(false);

        AlbumDTO parent;
        if (selected == null) {
            parent = null;
        } else {
            parent = getParentAlbum(selected);
            album.region(parent.getRegion());
        }

        new AlbumDialog(album).showAndWait().ifPresent(newAlbum -> {
            if (parent != null) {
                newAlbum.id(UUID.randomUUID());
                parent.getSubalbums().add(newAlbum);
                if (parent.getId() != null) {
                    toUpdate.put(parent.getId(), parent);
                }
            } else {
                toAdd.put(newAlbum.getName(), newAlbum);
                albums.add(newAlbum);
            }
            refreshData();
        });
    }

    private void onEditAlbum(ActionEvent event) {
        event.consume();

        var selected = view.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        var parent = getParentAlbum(selected);

        new AlbumDialog(selected.getValue()).showAndWait().ifPresent(_ -> {
            toUpdate.put(parent.getId(), parent);
            refreshData();
        });
    }

    private AlbumDTO getParentAlbum(TreeItem<AlbumDTO> item) {
        return item.getParent() == rootItem ? item.getValue() : item.getParent().getValue();
    }
}
