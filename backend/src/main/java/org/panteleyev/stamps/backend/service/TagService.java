// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend.service;

import org.panteleyev.stamps.backend.converter.TagConverter;
import org.panteleyev.stamps.backend.exception.ConflictException;
import org.panteleyev.stamps.backend.exception.TagNotFoundException;
import org.panteleyev.stamps.backend.repository.TagRepository;
import org.panteleyev.stamps.dto.TagDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class TagService {
    private final TagRepository repository;
    private final TagConverter converter;

    public TagService(TagRepository repository, TagConverter converter) {
        this.repository = repository;
        this.converter = converter;
    }

    @Transactional(readOnly = true)
    public List<TagDTO> getTags() {
        return repository.findAll().stream()
                .map(converter::entityToTagDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public TagDTO getTag(UUID id) {
        var entity = repository.findById(id).orElseThrow(() -> new TagNotFoundException(id));
        return converter.entityToTagDTO(entity);
    }

    @Transactional
    public TagDTO postTag(TagDTO dto) {
        repository.findByName(dto.getName()).ifPresent(t -> {
            throw new ConflictException("Tag with name '" + t.getName() + "' already exists");
        });

        return putTag(dto.id(UUID.randomUUID()));
    }

    @Transactional
    public TagDTO putTag(TagDTO dto) {
        var entity = converter.tagDTOToEntity(dto);
        repository.save(entity);
        return converter.entityToTagDTO(entity);
    }
}
