// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.profiles;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import org.panteleyev.fx.BaseDialog;
import org.panteleyev.fx.ToStringConverter;

import java.util.List;
import java.util.Objects;

import static java.util.Objects.requireNonNull;
import static org.panteleyev.fx.factories.LabelFactory.label;
import static org.panteleyev.fx.factories.grid.GridCell.gridCell;
import static org.panteleyev.fx.factories.grid.GridPaneFactory.gridPane;
import static org.panteleyev.fx.factories.grid.GridRow.gridRow;
import static org.panteleyev.stamps.desktop.settings.Settings.settings;
import static org.panteleyev.stamps.desktop.ui.MainWindowController.UI;
import static org.panteleyev.stamps.desktop.ui.Styles.GRID_PANE;

public class ConnectDialog extends BaseDialog<ConnectionProfile> {
    private final ComboBox<ConnectionProfile> profileComboBox;
    private final CheckBox defaultCheck;
    private final CheckBox autoConnectCheck;

    private final ConnectionProfileManager profileManager;

    public ConnectDialog(ConnectionProfileManager profileManager) {
        super(settings().getDialogCssFilePath());
        this.profileManager = requireNonNull(profileManager);

        profileComboBox = initProfileComboBox();
        defaultCheck = initDefaultCheck();
        autoConnectCheck = initAutoConnectCheck();

        setTitle("Соединение");

        var pane = gridPane(List.of(
                gridRow(label("Профиль:"), profileComboBox),
                gridRow(gridCell(defaultCheck, 2, 1)),
                gridRow(gridCell(autoConnectCheck, 2, 1))
        ), List.of(), List.of(GRID_PANE));

        getDialogPane().setContent(pane);

        profileComboBox.setMaxWidth(Double.MAX_VALUE);

        createDefaultButtons(UI);

        setResultConverter(b -> {
            if (b == ButtonType.OK) {
                ConnectionProfile selected = profileComboBox.getSelectionModel().getSelectedItem();
                if (defaultCheck.isSelected()) {
                    profileManager.setDefaultProfile(selected);
                }
                profileManager.setAutoConnect(autoConnectCheck.isSelected());
                profileManager.saveProfiles();
                return selected;
            } else {
                return null;
            }
        });

        Platform.runLater(profileComboBox::requestFocus);
    }

    private ComboBox<ConnectionProfile> initProfileComboBox() {
        var cb = new ComboBox<ConnectionProfile>();

        cb.setItems(FXCollections.observableArrayList(profileManager.getAll()));

        var defaultProfile = profileManager.getDefaultProfile();
        defaultProfile.ifPresentOrElse(p -> cb.getSelectionModel().select(p), () -> {
            if (profileManager.size() > 0) {
                cb.getSelectionModel().select(0);
            }
        });

        cb.setConverter(new ToStringConverter<>() {
            public String toString(ConnectionProfile profile) {
                return profile != null ? profile.name() : "";
            }
        });

        cb.getSelectionModel().selectedItemProperty().addListener((_, _, newValue) -> {
            defaultCheck.setSelected(Objects.equals(profileManager.getDefaultProfile().orElse(null), newValue));
            autoConnectCheck.setSelected(defaultCheck.isSelected() && profileManager.getAutoConnect());
        });

        return cb;
    }

    private CheckBox initDefaultCheck() {
        var check = new CheckBox("Профиль по умолчанию");
        check.setSelected(Objects.equals(profileManager.getDefaultProfile().orElse(null),
                profileComboBox.getSelectionModel().getSelectedItem()));
        return check;
    }

    private CheckBox initAutoConnectCheck() {
        var check = new CheckBox("Соединяться при запуске приложения");
        check.disableProperty().bind(defaultCheck.selectedProperty().not());
        check.setSelected(defaultCheck.isSelected() && profileManager.getAutoConnect());
        return check;
    }
}
