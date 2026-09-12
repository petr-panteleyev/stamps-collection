// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend.service;

import org.panteleyev.stamps.backend.converter.AlbumConverter;
import org.panteleyev.stamps.backend.domain.AlbumEntity;
import org.panteleyev.stamps.backend.repository.AlbumRepository;
import org.panteleyev.stamps.backend.repository.RegionRepository;
import org.panteleyev.stamps.backend.repository.TagRepository;
import org.panteleyev.stamps.dto.AlbumDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class AlbumService {
    private final AlbumRepository repository;
    private final RegionRepository regionRepository;
    private final TagRepository tagRepository;
    private final AlbumConverter converter;

    public AlbumService(AlbumRepository repository, RegionRepository regionRepository, TagRepository tagRepository,
            AlbumConverter converter)
    {
        this.repository = repository;
        this.regionRepository = regionRepository;
        this.tagRepository = tagRepository;
        this.converter = converter;
    }

    public List<AlbumDTO> getAlbums() {
        return repository.findAll().stream()
                .filter(e -> e.getParent() == null)
                .sorted(Comparator.comparingInt(AlbumEntity::getParentIndex))
                .map(converter::entityToDTO)
                .toList();
    }

    @Transactional
    public AlbumDTO postAlbum(AlbumDTO dto) {
        dto.setId(UUID.randomUUID());
        for (var album : dto.getSubalbums()) {
            album.setId(UUID.randomUUID());
        }
        return putAlbum(dto);
    }

    @Transactional
    public AlbumDTO putAlbum(AlbumDTO dto) {
        var regionEntities = regionRepository.findAll();
        var tagEntities = tagRepository.findAll();

        var entity = converter.dtoToEntity(dto, null, regionEntities, tagEntities);
        repository.save(entity);
        return dto;
    }
}
