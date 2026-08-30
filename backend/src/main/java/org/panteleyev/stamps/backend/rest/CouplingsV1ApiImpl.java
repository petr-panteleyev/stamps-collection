// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend.rest;

import org.panteleyev.stamps.backend.openapi.api.CouplingsV1ApiDelegate;
import org.panteleyev.stamps.backend.service.CouplingService;
import org.panteleyev.stamps.dto.CouplingDTO;
import org.panteleyev.stamps.dto.ItemPatchDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CouplingsV1ApiImpl implements CouplingsV1ApiDelegate {
    private final CouplingService service;

    public CouplingsV1ApiImpl(CouplingService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<CouplingDTO> getCoupling(UUID id) {
        return ResponseEntity.ok(service.getCoupling(id));
    }

    @Override
    public ResponseEntity<CouplingDTO> patchCoupling(UUID id, ItemPatchDTO patch) {
        return ResponseEntity.ok(service.patchCoupling(id, patch));
    }
}
