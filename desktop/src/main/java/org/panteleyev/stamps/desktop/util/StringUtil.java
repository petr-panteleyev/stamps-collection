// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.util;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

public final class StringUtil {
    public static final String CHECK_SYMBOL = "\u2714";

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    /// Converts integer value to string.
    ///
    /// @param value integer value
    /// @return string representation or empty string for `null`
    public static String toStringOrEmpty(Integer value) {
        if (value == null) return "";
        return Integer.toString(value);
    }

    /// Converts [BigDecimal] value to string.
    ///
    /// @param value decimal value
    /// @return string representation or empty string for `null`
    public static String toStringOrEmpty(BigDecimal value) {
        if (value == null) return "";
        return value.toString();
    }

    /// Converts [String] value to [Integer]. If value cannot be converted returns `null`.
    ///
    /// @param value string value
    /// @return integer value or `null`
    public static Integer toIntOrNull(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /// Converts [String] value to [BigDecimal]. If value cannot be converted returns `null`.
    ///
    /// @param value string value
    /// @return decimal value or `null`
    public static BigDecimal toBigDecimalOrNull(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private StringUtil() {
    }
}
