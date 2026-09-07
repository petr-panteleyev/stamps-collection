// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend.service;

import jakarta.persistence.EntityNotFoundException;
import org.panteleyev.stamps.backend.domain.ImageEntity;
import org.panteleyev.stamps.backend.repository.ImageRepository;
import org.panteleyev.stamps.backend.repository.IssueItemRepository;
import org.panteleyev.stamps.dto.ImageUploadDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.panteleyev.stamps.backend.domain.IssueItemSpecifications.hasNumberZag;
import static org.panteleyev.stamps.backend.domain.IssueItemSpecifications.hasRegion;
import static org.panteleyev.stamps.backend.domain.IssueItemType.BLOCK;
import static org.panteleyev.stamps.backend.domain.IssueItemType.STAMP;

@Service
public class ImageService {
    private static final Logger LOG = LoggerFactory.getLogger(ImageService.class);

    private final IssueItemRepository itemRepository;
    private final ImageRepository repository;

    public ImageService(IssueItemRepository itemRepository, ImageRepository repository) {
        this.itemRepository = itemRepository;
        this.repository = repository;
    }

    @Transactional
    public UUID saveImage(ImageUploadDTO dto) {
        var type = Objects.equals(dto.getIsBlock(), true) ? BLOCK : STAMP;

        var item = itemRepository.findAll(
                        Stream.of(hasRegion(dto.getRegion()), hasNumberZag(dto.getNumberZag()))
                                .reduce(Specification::and)
                                .orElse(null)
                ).stream()
                .filter(i -> i.getType() == type)
                .findAny()
                .orElseThrow(EntityNotFoundException::new);
        LOG.debug("Item found by {} and {}: {}", dto.getRegion(), dto.getNumberZag(), item.getId());

        var entity = new ImageEntity();
        entity.setId(item.getId());
        entity.setBytes(Base64.getDecoder().decode(dto.getImage()));
        repository.save(entity);
        return entity.getId();
    }

    public Optional<byte[]> getImageBytes(UUID uuid) {
        return repository.findById(uuid).map(ImageEntity::getBytes);
    }
}
