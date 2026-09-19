// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.converter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.panteleyev.stamps.desktop.BaseUnitTest;
import org.panteleyev.stamps.desktop.model.CollectionTag;
import org.panteleyev.stamps.dto.TagDTO;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TagDTO <-> CollectionTag converter")
public class TagConverterTest extends BaseUnitTest {

    @Test
    @DisplayName("Should convert TagDTO to CollectionTag")
    public void testDtoToModel() {
        var dto = new TagDTO().id(UUID.randomUUID()).name(randomString());
        var model = TagConverter.dtoToModel(dto);
        assertThat(model.id()).isEqualTo(dto.getId());
        assertThat(model.name()).isEqualTo(dto.getName());
    }

    @Test
    @DisplayName("Should convert CollectionTag to TagDTO")
    public void testModelToDto() {
        var model = new CollectionTag(UUID.randomUUID(), randomString());
        var dto = TagConverter.modelToDto(model);
        assertThat(dto.getId()).isEqualTo(model.id());
        assertThat(dto.getName()).isEqualTo(model.name());
    }
}
