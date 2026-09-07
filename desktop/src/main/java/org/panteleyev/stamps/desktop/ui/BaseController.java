// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui;

import javafx.stage.Stage;
import org.panteleyev.fx.Controller;

import static org.panteleyev.stamps.desktop.settings.Settings.settings;

public class BaseController extends Controller {

    public BaseController(Stage stage, String css) {
        super(stage, css);
    }

    @Override
    protected void onWindowHiding() {
        settings().saveStageDimensions(this);
    }
}
