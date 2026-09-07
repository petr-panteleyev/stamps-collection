// Copyright © 2020-2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.profiles;

import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.controlsfx.validation.ValidationSupport;

import java.util.List;

import static org.panteleyev.fx.factories.LabelFactory.label;
import static org.panteleyev.fx.factories.TextFieldFactory.textField;
import static org.panteleyev.fx.factories.grid.ColumnConstraintsFactory.columnConstraints;
import static org.panteleyev.fx.factories.grid.GridPaneFactory.gridPane;
import static org.panteleyev.fx.factories.grid.GridRow.gridRow;
import static org.panteleyev.stamps.desktop.ui.Styles.DOUBLE_INSETS;
import static org.panteleyev.stamps.desktop.ui.Styles.GRID_PANE;

final class ConnectionEditor extends VBox {
    private final TextField serverUrlEdit = textField();

    ConnectionEditor(ValidationSupport validation) {
        var constraints = columnConstraints();
        constraints.setHgrow(Priority.ALWAYS);

        getChildren().addAll(gridPane(
                List.of(
                        gridRow(label("Сервер:"), serverUrlEdit)
                ),
                List.of(constraints, constraints),
                List.of(GRID_PANE)
        ));

        VBox.setMargin(getChildren().getFirst(), DOUBLE_INSETS);
    }

    TextField getServerUrlEdit() {
        return serverUrlEdit;
    }

    void setServerUrl(String serverUrl) {
        serverUrlEdit.setText(serverUrl);
    }

    String getServerUrl() {
        return serverUrlEdit.getText();
    }

    void setProfile(ConnectionProfile profile) {
        if (profile != null) {
            setServerUrl(profile.serverUrl());
        } else {
            setServerUrl("http://localhost:1705");
        }
    }
}
