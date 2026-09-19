// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui;

import javafx.scene.image.Image;

import static java.util.Objects.requireNonNull;

public enum Picture {
    SQUARE,
    STAMP;

    private final Image image;

    Picture() {
        var res = name().toLowerCase() + ".png";

        image = new Image(requireNonNull(getClass().getResourceAsStream("/images/" + res)));
    }

    public Image getImage() {
        return image;
    }
}