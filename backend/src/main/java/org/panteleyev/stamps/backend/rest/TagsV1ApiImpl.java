// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend.rest;

import org.panteleyev.stamps.backend.openapi.api.TagsV1ApiDelegate;
import org.panteleyev.stamps.backend.service.TagService;
import org.panteleyev.stamps.dto.TagDTO;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.ErrorResponseException;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class TagsV1ApiImpl implements TagsV1ApiDelegate {
    private final TagService service;

    public TagsV1ApiImpl(TagService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<List<TagDTO>> getTags() {
        return ResponseEntity.ok(service.getTags());
    }

    @Override
    public ResponseEntity<TagDTO> getTagById(UUID id) {
        return ResponseEntity.ok(service.getTag(id));
    }

    @Override
    public ResponseEntity<TagDTO> postTag(TagDTO dto) {
        if (dto.getId() != null) {
            throw new ErrorResponseException(HttpStatusCode.valueOf(400));
        }

        return ResponseEntity.ok(service.postTag(dto));
    }

    @Override
    public ResponseEntity<TagDTO> putTag(UUID id, TagDTO dto) {
        if (dto.getId() == null || !Objects.equals(id, dto.getId())) {
            throw new ErrorResponseException(HttpStatusCode.valueOf(400));
        }

        return ResponseEntity.ok(service.putTag(dto));
    }
}
