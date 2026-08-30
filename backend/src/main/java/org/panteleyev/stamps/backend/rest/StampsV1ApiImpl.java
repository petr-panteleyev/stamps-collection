// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend.rest;

import org.panteleyev.stamps.backend.openapi.api.StampsV1ApiDelegate;
import org.panteleyev.stamps.backend.service.StampService;
import org.panteleyev.stamps.dto.ItemPatchDTO;
import org.panteleyev.stamps.dto.StampDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StampsV1ApiImpl implements StampsV1ApiDelegate {
    private final StampService service;

    public StampsV1ApiImpl(StampService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<StampDTO> getStamp(UUID id) {
        return ResponseEntity.ok(service.getStamp(id));
    }

    @Override
    public ResponseEntity<StampDTO> patchStamp(UUID id, ItemPatchDTO patch) {
        return ResponseEntity.ok(service.patchStamp(id, patch));
    }
}
