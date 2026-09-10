// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend.rest;

import org.panteleyev.stamps.backend.exception.BadRequestException;
import org.panteleyev.stamps.backend.openapi.api.ImagesV1ApiDelegate;
import org.panteleyev.stamps.backend.service.ImageService;
import org.panteleyev.stamps.dto.ImageUploadDTO;
import org.panteleyev.stamps.dto.ImageUploadResponseDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.UUID;

@Service
public class ImagesV1ApiImpl implements ImagesV1ApiDelegate {
    private final ImageService service;

    public ImagesV1ApiImpl(ImageService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<ImageUploadResponseDto> postImage(ImageUploadDTO dto) {
        if (dto.getRegion() == null || dto.getNumberZag() == null) {
            throw new BadRequestException("Region and number are mandatory");
        }
        var id = service.saveImage(dto);
        return ResponseEntity.ok(new ImageUploadResponseDto().id(id));
    }

    @Override
    public ResponseEntity<ImageUploadResponseDto> putImage(UUID id, ImageUploadDTO dto) {
        return ResponseEntity.ok(new ImageUploadResponseDto().id(service.putImage(id, dto)));
    }

    @Override
    public ResponseEntity<StreamingResponseBody> getImageBytes(UUID id) {
        var bytes = service.getImageBytes(id).orElse(null);
        if (bytes == null) return ResponseEntity.notFound().build();

        var body = (StreamingResponseBody) outputStream -> outputStream.write(bytes);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(body);
    }
}
