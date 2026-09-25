// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend.converter;

import java.math.BigDecimal;
import java.util.List;

public final class ConverterUtils {

    public static boolean convertBoolean(Boolean value) {
        return value != null && value;
    }

    public static String convertString(String value) {
        return value == null ? "" : value;
    }

    public static <T> List<T> convert(List<T> list) {
        return list == null ? List.of() : list;
    }

    public static BigDecimal convertBigDecimal(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private ConverterUtils() {
    }
}
