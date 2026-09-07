// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.panteleyev.stamps.desktop.BaseUnitTest;
import org.panteleyev.stamps.dto.CouplingDTO;
import org.panteleyev.stamps.dto.StampDTO;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DtoUtils tests")
public class DtoUtilsTest extends BaseUnitTest {

    @ParameterizedTest
    @CsvSource(
            value = {
                    "NULL, true",
                    "false, true",
                    "true, false",
            },
            nullValues = "NULL"
    )
    @DisplayName("should negate nullable boolean value")
    public void testNegate(Boolean value, boolean expected) {
        assertThat(DtoUtils.negate(value)).isEqualTo(expected);
    }

    @Test
    @DisplayName("should make a copy of StampDTO")
    public void testStampCopy() {
        var given = new StampDTO()
                .id(UUID.randomUUID())
                .numberZag(randomInt())
                .numberCfa(randomInt())
                .blockNumber(randomInt())
                .hasClean(randomBoolean())
                .hasCancelled(randomBoolean())
                .replacementRequired(randomBoolean())
                .description(randomString())
                .comment(randomString())
                .denomination(randomDecimal())
                .tags(List.of(randomString(), randomString(), randomString()));

        var actual = DtoUtils.copy(given);

        assertThat(actual == given).isFalse();
        assertThat(actual).isEqualTo(given);
        assertThat(given.getTags() == actual.getTags()).isFalse();
    }

    @Test
    @DisplayName("should make a copy of CouplingDTO")
    public void testCouplingCopy() {
        var given = new CouplingDTO()
                .id(UUID.randomUUID())
                .hasClean(randomBoolean())
                .hasCancelled(randomBoolean())
                .replacementRequired(randomBoolean())
                .comment(randomString())
                .stampNumbers(List.of(randomInt(), randomInt(), randomInt()));

        var actual = DtoUtils.copy(given);

        assertThat(actual == given).isFalse();
        assertThat(actual).isEqualTo(given);
        assertThat(given.getStampNumbers() == actual.getStampNumbers()).isFalse();
    }
}
