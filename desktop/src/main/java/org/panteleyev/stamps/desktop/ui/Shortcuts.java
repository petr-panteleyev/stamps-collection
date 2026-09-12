// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;

import static javafx.scene.input.KeyCombination.ALT_DOWN;
import static javafx.scene.input.KeyCombination.SHORTCUT_DOWN;

public final class Shortcuts {
    public static final KeyCodeCombination SHORTCUT_E = new KeyCodeCombination(KeyCode.E, SHORTCUT_DOWN);
    public static final KeyCodeCombination SHORTCUT_N = new KeyCodeCombination(KeyCode.N, SHORTCUT_DOWN);

    public static final KeyCodeCombination SHORTCUT_ALT_B = new KeyCodeCombination(KeyCode.B, SHORTCUT_DOWN, ALT_DOWN);
    public static final KeyCodeCombination SHORTCUT_ALT_C = new KeyCodeCombination(KeyCode.C, SHORTCUT_DOWN, ALT_DOWN);
    public static final KeyCodeCombination SHORTCUT_ALT_I = new KeyCodeCombination(KeyCode.I, SHORTCUT_DOWN, ALT_DOWN);

    private Shortcuts() {
    }
}
