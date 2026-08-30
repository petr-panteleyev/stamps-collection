// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend.converter;

public final class ConverterUtils {

    public static boolean convertBoolean(Boolean value) {
        return value != null && value;
    }

    public static String convertString(String value) {
        return value == null ? "" : value;
    }

    private ConverterUtils() {
    }
}
