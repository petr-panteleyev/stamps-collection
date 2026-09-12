// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class ValidatorsTest {

    @ParameterizedTest
    @CsvSource(
            value = {
                    "NULL, 0, false",
                    "100, 0, true",
                    "-1, 0, false",
                    "0, 0, true",
                    "aaa, 0, false",
            },
            nullValues = "NULL"
    )
    public void testValidateInteger(String value, int minValue, boolean expected) {
        assertThat(Validators.validateInteger(value, minValue)).isEqualTo(expected);
    }
}
