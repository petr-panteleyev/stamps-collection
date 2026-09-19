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
                .setStartYear(dto.getStartYear())
                .setEndYear(dto.getEndYear());
    }

    public RegionDTO entityToRegionDTO(RegionEntity entity) {
        return new RegionDTO()
                .id(entity.getId())
                .name(entity.getName())
                .startYear(entity.getStartYear())
                .endYear(entity.getEndYear());
    }
}
