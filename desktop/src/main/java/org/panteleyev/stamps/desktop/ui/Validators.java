// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui;

import javafx.scene.control.Control;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.Validator;

import java.math.BigDecimal;

public final class Validators {
    public static final Validator<String> INTEGER_VALIDATOR = (Control control, String value) -> {
        var invalid = false;
        try {
            var intValue = Integer.parseInt(value);
            if (intValue <= 0) {
                invalid = true;
            }
        } catch (NumberFormatException ex) {
            invalid = true;
        }

        return ValidationResult.fromErrorIf(control, null, invalid && !control.isDisabled());
    };

    public static final Validator<String> INTEGER_OR_ZERO_VALIDATOR = (Control control, String value) -> {
        var invalid = false;
        try {
            var intValue = Integer.parseInt(value);
            if (intValue < 0) {
                invalid = true;
            }
        } catch (NumberFormatException ex) {
            invalid = true;
        }

        return ValidationResult.fromErrorIf(control, null, invalid && !control.isDisabled());
    };

    public static final Validator<String> DECIMAL_VALIDATOR = (Control control, String value) -> {
        var invalid = false;
        try {
            var decimal = new BigDecimal(value);
            if (decimal.compareTo(BigDecimal.ZERO) <= 0) {
                invalid = true;
            }
        } catch (NumberFormatException ex) {
            invalid = true;
        }

        return ValidationResult.fromErrorIf(control, null, invalid && !control.isDisabled());
    };

    public static final Validator<String> STRING_NOT_EMPTY_VALIDATOR = (Control control, String value) ->
            ValidationResult.fromErrorIf(control, null, (value == null || value.isBlank()) && !control.isDisabled());

    private Validators(){}
}
