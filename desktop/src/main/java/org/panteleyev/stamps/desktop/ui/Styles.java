// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui;

import javafx.geometry.Insets;
import org.panteleyev.fx.Controller;

public final class Styles {
    public static final double BIG_SPACING = 5.0;
    public static final double SMALL_SPACING = 2.0;
    public static final double DOUBLE_SPACING = BIG_SPACING * 2;

    public static final Insets BIG_INSETS = new Insets(BIG_SPACING);
    public static final Insets DOUBLE_INSETS = new Insets(DOUBLE_SPACING);

    public static final String GRID_PANE = "gridPane";

    public static final String CSS_ISSUE_TITLE = "issue-title";

    public static final String CSS_HAS_CLEAN = "has-clean";
    public static final String CSS_HAS_CANCELLED = "has-cancelled";
    public static final String CSS_MISSING = "missing";

    public static final int TABLE_IMAGE_SIZE = 100;
    public static final int TOOLTIP_STAMP_IMAGE_SIZE = 300;
    public static final int TOOLTIP_BLOCK_IMAGE_SIZE = 600;

    public static final String STYLE_ABOUT_LABEL = "aboutLabel";

    public static final String ABOUT_DIALOG_STYLE_SHEET = Controller.encodeStyleSheet("""
            .gridPane {
                -fx-hgap: 5;
                -fx-vgap: 5;
            }
            
            .dialog-pane:header .header-panel .label {
                -fx-font-family: "Dialog";
                -fx-font-size: 28;
                -fx-font-weight: bold;
            }
            
            .label {
                -fx-font-family: "Dialog";
                -fx-font-size: 14;
            }
            
            .aboutLabel {
                -fx-font-family: "Dialog";
                -fx-font-size: 28;
                -fx-font-weight: bold;
            }
            """);

    private Styles(){}
}
