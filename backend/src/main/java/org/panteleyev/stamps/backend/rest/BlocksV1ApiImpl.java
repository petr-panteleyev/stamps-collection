// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend.rest;

import org.panteleyev.stamps.backend.openapi.api.BlocksV1ApiDelegate;
import org.panteleyev.stamps.backend.service.BlockService;
import org.panteleyev.stamps.dto.BlockDTO;
import org.panteleyev.stamps.dto.ItemPatchDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BlocksV1ApiImpl implements BlocksV1ApiDelegate {
    private final BlockService service;

    public BlocksV1ApiImpl(BlockService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<BlockDTO> getBlock(UUID id) {
        return ResponseEntity.ok(service.getBlock(id));
    }

    @Override
    public ResponseEntity<BlockDTO> patchBlock(UUID id, ItemPatchDTO patch) {
        return ResponseEntity.ok(service.patchBlock(id, patch));
    }
}
