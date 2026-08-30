// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend.converter;

import org.junit.jupiter.api.Test;
import org.panteleyev.stamps.backend.domain.RegionEntity;
import org.panteleyev.stamps.dto.RegionDTO;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class RegionConverterTest {
    private static final UUID ID = UUID.randomUUID();

    private final RegionConverter converter = new RegionConverter();

    @Test
    public void testRegionDTOToEntity() {
        var dto = new RegionDTO()
                .id(ID)
                .name("name")
                .yearStart(2001)
                .yearEnd(2010);
        var expected = new RegionEntity()
                .setId(ID)
                .setName("name")
                .setYearStart(2001)
                .setYearEnd(2010);

        assertThat(converter.regionDTOToEntity(dto)).isEqualTo(expected);
    }

    @Test
    public void testEntityToRegionDTO() {
        var entity = new RegionEntity()
                .setId(ID)
                .setName("name")
                .setYearStart(2001)
                .setYearEnd(2010);
        var expected = new RegionDTO()
                .id(ID)
                .name("name")
                .yearStart(2001)
                .yearEnd(2010);

        assertThat(converter.entityToRegionDTO(entity)).isEqualTo(expected);
    }
}
