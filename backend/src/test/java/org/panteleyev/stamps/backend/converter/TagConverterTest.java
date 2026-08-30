// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend.converter;

import org.junit.jupiter.api.Test;
import org.panteleyev.stamps.backend.domain.TagEntity;
import org.panteleyev.stamps.dto.TagDTO;

import static org.assertj.core.api.Assertions.assertThat;

public class TagConverterTest {
    private final TagConverter converter = new TagConverter();

    @Test
    public void testEntityToTagDTO() {
        assertThat(converter.entityToTagDTO(new TagEntity("name")))
                .isEqualTo(new TagDTO().name("name"));
    }
}
