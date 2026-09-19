// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui.albums;

import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import org.controlsfx.validation.ValidationSupport;
import org.panteleyev.fx.BaseDialog;
import org.panteleyev.stamps.desktop.ui.RegionChoiceBox;
import org.panteleyev.stamps.desktop.ui.TagComboBox;
import org.panteleyev.stamps.dto.AlbumDTO;

import java.util.List;

import static javafx.application.Platform.runLater;
import static org.panteleyev.fx.factories.LabelFactory.label;
import static org.panteleyev.fx.factories.grid.GridPaneFactory.gridPane;
import static org.panteleyev.fx.factories.grid.GridRow.gridRow;
import static org.panteleyev.stamps.desktop.GlobalContext.stampsService;
import static org.panteleyev.stamps.desktop.settings.Settings.settings;
import static org.panteleyev.stamps.desktop.ui.MainWindowController.UI;
import static org.panteleyev.stamps.desktop.ui.Styles.GRID_PANE;
import static org.panteleyev.stamps.desktop.ui.Validators.INTEGER_OR_NULL_VALIDATOR;
import static org.panteleyev.stamps.desktop.ui.Validators.STRING_NOT_EMPTY_VALIDATOR;
import static org.panteleyev.stamps.desktop.util.DtoUtils.normalize;

public class AlbumDialog extends BaseDialog<AlbumDTO> {
    private static final int NAME_LABEL_INDEX = 2;

    private final RegionChoiceBox regionChoiceBox = new RegionChoiceBox();
    private final TextField nameEdit = new TextField();
    private final TextField startYearEdit = new TextField();
    private final TextField endYearEdit = new TextField();
    private final TagComboBox tagsComboBox = new TagComboBox();
    private final TagComboBox excludedTagsComboBox = new TagComboBox();

    private final ValidationSupport validation = new ValidationSupport();

    public AlbumDialog(AlbumDTO album) {
        this(album, false);
    }

    public AlbumDialog(AlbumDTO album, boolean filter) {
        super(settings().getDialogCssFilePath());

        if (filter) {
            setTitle("Фильтр");
            nameEdit.setDisable(true);
        }

        regionChoiceBox.setRegions(stampsService().loadRegions());

        var allTags = stampsService().loadTags();
        tagsComboBox.setTags(allTags);
        excludedTagsComboBox.setTags(allTags);

        nameEdit.setPrefColumnCount(20);

        var grid = gridPane(List.of(
                gridRow(label("Регион:"), regionChoiceBox),
                gridRow(label("Название:"), nameEdit),
                gridRow(label("Начало:"), startYearEdit),
                gridRow(label("Конец:"), endYearEdit),
                gridRow(label("Теги:"), tagsComboBox),
                gridRow(label("Искл. теги:"), excludedTagsComboBox)
        ), List.of(), List.of(GRID_PANE));

        if (filter) {
            // If this is a filter dialog name is not required
            grid.getChildren().remove(NAME_LABEL_INDEX, NAME_LABEL_INDEX + 2);
        }

        nameEdit.setText(album.getName());
        if (album.getRegion() == null) {
            regionChoiceBox.getSelectionModel().selectFirst();
        } else {
            regionChoiceBox.getSelectionModel().select(album.getRegion());
        }
        startYearEdit.setText(toString(album.getStartYear()));
        endYearEdit.setText(toString(album.getEndYear()));
        normalize(album.getTags()).forEach(t -> tagsComboBox.getCheckModel().check(t));
        normalize(album.getExcludedTags()).forEach(t -> excludedTagsComboBox.getCheckModel().check(t));

        getDialogPane().setContent(grid);

        createDefaultButtons(UI);

        setResultConverter(buttonType -> {
            if (buttonType != ButtonType.OK) return null;

            var startYear = startYearEdit.getText().isEmpty() ? null : Integer.parseInt(startYearEdit.getText());
            var endYear = endYearEdit.getText().isEmpty() ? null : Integer.parseInt(endYearEdit.getText());

            album.name(nameEdit.getText())
                    .region(regionChoiceBox.getSelectionModel().getSelectedItem())
                    .startYear(startYear)
                    .endYear(endYear);

            album.tags(tagsComboBox.getCheckModel().getCheckedItems())
                    .excludedTags(excludedTagsComboBox.getCheckModel().getCheckedItems());

            return album;
        });

        runLater(() -> {
            createValidationSupport();
            nameEdit.requestFocus();
        });
    }

    private void createValidationSupport() {
        validation.registerValidator(nameEdit, STRING_NOT_EMPTY_VALIDATOR);
        validation.registerValidator(startYearEdit, INTEGER_OR_NULL_VALIDATOR);
        validation.registerValidator(endYearEdit, INTEGER_OR_NULL_VALIDATOR);
        validation.initInitialDecoration();
    }

    private static String toString(Integer value) {
        return value == null ? "" : value.toString();
    }
}
