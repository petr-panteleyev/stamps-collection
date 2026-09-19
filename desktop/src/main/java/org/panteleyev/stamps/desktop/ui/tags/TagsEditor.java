// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui.tags;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.BorderPane;
import org.panteleyev.fx.BaseDialog;
import org.panteleyev.stamps.desktop.model.CollectionTag;
import org.panteleyev.stamps.desktop.ui.Shortcuts;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static javafx.event.ActionEvent.ACTION;
import static org.panteleyev.fx.factories.ButtonFactory.buttonType;
import static org.panteleyev.fx.factories.MenuFactory.menuItem;
import static org.panteleyev.stamps.desktop.GlobalContext.stampsService;
import static org.panteleyev.stamps.desktop.ui.MainWindowController.UI;

public class TagsEditor extends BaseDialog<TagEditorResult> {
    private final ObservableList<CollectionTag> items = FXCollections.observableArrayList();
    private final ObservableList<CollectionTag> sortedItems = items.sorted(Comparator.comparing(CollectionTag::name));
    private final ListView<CollectionTag> tagList = new ListView<>(sortedItems);

    private final Map<String, CollectionTag> toAdd = new HashMap<>();
    private final Map<UUID, CollectionTag> toUpdate = new HashMap<>();
    private final Map<UUID, CollectionTag> toDelete = new HashMap<>();

    public TagsEditor() {
        setTitle("Теги");

        getDialogPane().setContent(new BorderPane(tagList));

        tagList.setCellFactory(_ -> new TagListCell());
        items.setAll(stampsService().loadTags());

        tagList.setContextMenu(createContextMenu());

        var newAlbumButtonType = buttonType("Создать", ButtonBar.ButtonData.LEFT);
        var editAlbumButtonType = buttonType("Редактировать", ButtonBar.ButtonData.LEFT);
        getDialogPane().getButtonTypes().addAll(newAlbumButtonType, editAlbumButtonType);
        createDefaultButtons(UI);

        getButton(newAlbumButtonType).ifPresent(b -> b.addEventFilter(ACTION, this::onNewTag));
        getButton(editAlbumButtonType).ifPresent(b -> b.addEventFilter(ACTION, this::onEditTag));

        setResultConverter(buttonType -> {
            if (buttonType != ButtonType.OK) return null;
            return new TagEditorResult(toAdd.values(), toUpdate.values(), toDelete.values());
        });

        setResizable(true);
    }

    private ContextMenu createContextMenu() {
        var newAlbumMenuItem = menuItem("Создать", this::onNewTag);
        newAlbumMenuItem.setAccelerator(Shortcuts.SHORTCUT_N);
        var editAlbumMenuItem = menuItem("Редактировать", this::onEditTag);
        editAlbumMenuItem.setAccelerator(Shortcuts.SHORTCUT_E);
//        var deleteAlbumMenuItem = menuItem("Удалить...");
        return new ContextMenu(
                newAlbumMenuItem,
                new SeparatorMenuItem(),
                editAlbumMenuItem
//                new SeparatorMenuItem(),
//                deleteAlbumMenuItem
        );
    }

    private void onNewTag(ActionEvent event) {
        event.consume();
        new TagDialog(new CollectionTag(null, "Новый тег")).showAndWait().ifPresent(tag -> {
            toAdd.put(tag.name(), tag);
            items.add(tag);
        });
    }

    private void onEditTag(ActionEvent event) {
        event.consume();
        var selected = tagList.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        new TagDialog(selected).showAndWait().ifPresent(tag -> {
            toUpdate.put(tag.id(), tag);
            items.replaceAll(t -> Objects.equals(t.id(), tag.id()) ? tag : t);
        });
    }
}
