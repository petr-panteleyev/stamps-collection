// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.converter;

import org.panteleyev.stamps.desktop.model.CollectionRegion;
import org.panteleyev.stamps.dto.RegionDTO;

import java.time.LocalDate;

public final class RegionConverter {

    public static CollectionRegion dtoToModel(RegionDTO dto) {
        var endYear = dto.getEndYear() == null ? LocalDate.now().getYear() : dto.getEndYear();
        return new CollectionRegion(dto.getId(), dto.getName(), dto.getStartYear(), endYear);
    }

    private RegionConverter(){}
}
