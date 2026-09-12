// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend.rest;

import org.panteleyev.stamps.backend.exception.BadRequestException;
import org.panteleyev.stamps.backend.openapi.api.AlbumsV1ApiDelegate;
import org.panteleyev.stamps.backend.service.AlbumService;
import org.panteleyev.stamps.dto.AlbumDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class AlbumsV1ApiImpl implements AlbumsV1ApiDelegate {
    private final AlbumService service;

    public AlbumsV1ApiImpl(AlbumService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<List<AlbumDTO>> getAlbums() {
        return ResponseEntity.ok(service.getAlbums());
    }

    @Override
    public ResponseEntity<AlbumDTO> postAlbum(AlbumDTO dto) {
        if (dto.getId() != null) {
            throw new BadRequestException("ID must be null");
        }
        return ResponseEntity.ok(service.postAlbum(dto));
    }

    @Override
    public ResponseEntity<AlbumDTO> putAlbum(UUID id, AlbumDTO dto) {
        if (!Objects.equals(id, dto.getId())) {
            throw new BadRequestException("ID is not valid");
        }
        return ResponseEntity.ok(service.putAlbum(dto));
    }
}
