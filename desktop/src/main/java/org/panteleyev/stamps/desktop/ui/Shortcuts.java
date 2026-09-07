// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;

import static javafx.scene.input.KeyCombination.SHORTCUT_DOWN;

public final class Shortcuts {
    public static final KeyCodeCombination SHORTCUT_E = new KeyCodeCombination(KeyCode.E, SHORTCUT_DOWN);
    public static final KeyCodeCombination SHORTCUT_N = new KeyCodeCombination(KeyCode.N, SHORTCUT_DOWN);

    private Shortcuts() {
    }
}
