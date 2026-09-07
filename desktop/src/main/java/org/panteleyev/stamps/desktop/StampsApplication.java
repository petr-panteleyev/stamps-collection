// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.panteleyev.stamps.desktop.ui.MainWindowController;

import java.io.ByteArrayInputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.panteleyev.stamps.desktop.ApplicationFiles.files;
import static org.panteleyev.stamps.desktop.settings.Settings.settings;

public class StampsApplication extends Application {
    private final static Logger LOGGER = Logger.getLogger(StampsApplication.class.getName());

    private final static String LOG_PROPERTIES = """
            handlers                                = java.util.logging.FileHandler
            
            java.util.logging.FileHandler.level     = ALL
            java.util.logging.FileHandler.formatter = java.util.logging.SimpleFormatter
            java.util.logging.FileHandler.pattern   = %FILE_PATTERN%
            java.util.logging.FileHandler.append    = true
            
            java.util.logging.SimpleFormatter.format = %1$tF %1$tk:%1$tM:%1$tS %2$s%n%4$s: %5$s%6$s%n
            """;

    @Override
    public void start(Stage primaryStage) throws Exception {
        files().initialize();
        settings().load();

        var logProperties = LOG_PROPERTIES.replace("%FILE_PATTERN%",
                files().getLogDirectory().resolve("StampsCollection.log").toString().replace("\\", "/"));
        try (var inputStream = new ByteArrayInputStream(logProperties.getBytes(UTF_8))) {
            LogManager.getLogManager().readConfiguration(inputStream);
        }

        Thread.setDefaultUncaughtExceptionHandler((_, e) -> uncaughtException(e));

        new MainWindowController(primaryStage);
        primaryStage.show();
    }

    public static void uncaughtException(Throwable e) {
        LOGGER.log(Level.SEVERE, "Uncaught exception", e);
        Platform.runLater(() -> {
            var alert = new Alert(Alert.AlertType.ERROR, e.toString());
            alert.showAndWait();
        });
    }

    static void main(String[] args) {
        Application.launch(StampsApplication.class, args);
    }
}
