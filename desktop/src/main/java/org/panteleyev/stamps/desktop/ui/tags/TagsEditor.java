// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui.tags;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import org.panteleyev.fx.BaseDialog;
import org.panteleyev.stamps.dto.TagDTO;

import java.util.Comparator;

import static org.panteleyev.fx.factories.BoxFactory.vBox;
import static org.panteleyev.fx.factories.ButtonFactory.button;
import static org.panteleyev.stamps.desktop.GlobalContext.stampsService;
import static org.panteleyev.stamps.desktop.ui.Styles.BIG_SPACING;

public class TagsEditor extends BaseDialog<Object> {
    private final ObservableList<TagDTO> items = FXCollections.observableArrayList();
    private final ObservableList<TagDTO> sortedItems = items.sorted(Comparator.comparing(TagDTO::getName));
    private final ListView<TagDTO> tagList = new ListView<>(sortedItems);

    public TagsEditor() {
        setTitle("Теги");

        var buttonBox = vBox(BIG_SPACING,
                button("Создать", this::onNewTag),
                button("Изменить", this::onEditTag));

        var root = new BorderPane(tagList);
        root.setRight(buttonBox);
        getDialogPane().setContent(root);

        tagList.setCellFactory(_ -> new TagListCell());
        items.setAll(stampsService().loadTags());

        getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
        if (getDialogPane().lookupButton(ButtonType.CLOSE) instanceof Button closeButton) {
            closeButton.setText("Закрыть");
        }

        setResizable(true);
    }

    private void onNewTag(ActionEvent ignored) {
        new TagDialog(new TagDTO()).showAndWait().ifPresent(tag -> {
            var created = stampsService().createTag(tag);
            items.add(created);
        });
    }

    private void onEditTag(ActionEvent ignored) {
        var selected = tagList.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        new TagDialog(selected).showAndWait().ifPresent(tag -> {
            stampsService().updateTag(tag);
            tagList.refresh();
        });
    }
}
