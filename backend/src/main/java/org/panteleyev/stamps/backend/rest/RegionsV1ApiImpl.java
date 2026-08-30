// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend.rest;

import org.panteleyev.stamps.backend.exception.BadRequestException;
import org.panteleyev.stamps.backend.openapi.api.RegionsV1ApiDelegate;
import org.panteleyev.stamps.backend.service.RegionService;
import org.panteleyev.stamps.dto.RegionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegionsV1ApiImpl implements RegionsV1ApiDelegate {
    private final RegionService service;

    public RegionsV1ApiImpl(RegionService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<List<RegionDTO>> getRegions() {
        return ResponseEntity.ok(service.getRegions());
    }

    @Override
    public ResponseEntity<RegionDTO> postRegion(RegionDTO dto) {
        if (dto.getId() != null) {
            throw new BadRequestException("RegionDTO.id must be null");
        }
        return ResponseEntity.ok(service.postRegion(dto));
    }
}
