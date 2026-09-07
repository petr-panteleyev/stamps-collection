// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.settings;

import org.panteleyev.fx.Controller;
import org.panteleyev.stamps.desktop.ApplicationFiles;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Base64;
import java.util.Map;

import static java.util.Map.entry;
import static org.panteleyev.stamps.desktop.ApplicationFiles.files;

public final class Settings {
    private static final Settings SETTINGS = new Settings(files());

    private final ApplicationFiles files;
    private final WindowsSettings windowsSettings = new WindowsSettings();

    private String mainCssEncoded = "";

    public static Settings settings() {
        return SETTINGS;
    }

    private Settings(ApplicationFiles files) {
        this.files = files;
    }

    public String getMainCssFilePath() {
        return mainCssEncoded;
    }

    public void saveStageDimensions(Controller controller) {
        windowsSettings.storeWindowDimensions(controller);
    }

    public void loadStageDimensions(Controller controller) {
        windowsSettings.restoreWindowDimensions(controller);
    }

    public void saveWindowsSettings() {
        files.write(ApplicationFiles.AppFile.WINDOWS, windowsSettings::save);
    }

    private void generateCssFiles() {
        try {
            var mainCssBytes = this.getClass().getResourceAsStream("/css/main.css").readAllBytes();
            mainCssEncoded = encode(mainCssBytes);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public void load() {
        files.read(ApplicationFiles.AppFile.WINDOWS, windowsSettings::load);

        generateCssFiles();
    }

    private static String encode(byte[] css) {
        return "data:text/css;base64," + Base64.getEncoder().encodeToString(css);
    }
}
