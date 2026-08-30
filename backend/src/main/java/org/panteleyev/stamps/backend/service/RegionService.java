// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend.service;

import org.panteleyev.stamps.backend.converter.RegionConverter;
import org.panteleyev.stamps.backend.exception.ConflictException;
import org.panteleyev.stamps.backend.repository.RegionRepository;
import org.panteleyev.stamps.dto.RegionDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class RegionService {
    private final RegionRepository repository;
    private final RegionConverter converter;

    public RegionService(RegionRepository repository, RegionConverter converter) {
        this.repository = repository;
        this.converter = converter;
    }

    @Transactional(readOnly = true)
    public List<RegionDTO> getRegions() {
        return repository.findAll().stream()
                .map(converter::entityToRegionDTO)
                .toList();
    }

    @Transactional
    public RegionDTO postRegion(RegionDTO dto) {
        var existing = repository.findByName(dto.getName()).orElse(null);
        if (existing != null) {
            throw new ConflictException("Region with name '" + dto.getName() + "' already exists");
        }
        var entity = converter.regionDTOToEntity(dto.id(UUID.randomUUID()));
        repository.save(entity);
        return converter.entityToRegionDTO(entity);
    }
}
