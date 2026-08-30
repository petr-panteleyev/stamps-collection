// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend.converter;

import org.panteleyev.stamps.backend.domain.RegionEntity;
import org.panteleyev.stamps.dto.RegionDTO;
import org.springframework.stereotype.Component;

@Component
public class RegionConverter {
    public RegionEntity regionDTOToEntity(RegionDTO dto) {
        return new RegionEntity()
                .setId(dto.getId())
                .setName(dto.getName())
                .setYearStart(dto.getYearStart())
                .setYearEnd(dto.getYearEnd());
    }

    public RegionDTO entityToRegionDTO(RegionEntity entity) {
        return new RegionDTO()
                .id(entity.getId())
                .name(entity.getName())
                .yearStart(entity.getYearStart())
                .yearEnd(entity.getYearEnd());
    }
}
