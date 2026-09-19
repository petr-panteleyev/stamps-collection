// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend.service;

import org.panteleyev.stamps.backend.domain.ImageEntity;
import org.panteleyev.stamps.backend.repository.ImageRepository;
import org.panteleyev.stamps.dto.ImageUploadDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImageService {
    private final ImageRepository repository;

    public ImageService(ImageRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public UUID putImage(UUID id, ImageUploadDTO dto) {
        var entity = new ImageEntity();
        entity.setId(id);
        entity.setBytes(Base64.getDecoder().decode(dto.getImage()));
        repository.save(entity);
        return entity.getId();
    }

    public Optional<byte[]> getImageBytes(UUID uuid) {
        return repository.findById(uuid).map(ImageEntity::getBytes);
    }
}
