// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui;

import javafx.geometry.Insets;

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


    private Styles(){}
}
