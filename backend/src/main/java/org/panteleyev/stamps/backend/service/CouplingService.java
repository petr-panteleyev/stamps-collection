// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend.service;

import org.panteleyev.stamps.backend.converter.CouplingConverter;
import org.panteleyev.stamps.backend.exception.CouplingNotFoundException;
import org.panteleyev.stamps.backend.repository.IssueItemRepository;
import org.panteleyev.stamps.dto.CouplingDTO;
import org.panteleyev.stamps.dto.ItemPatchDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CouplingService {
    private final IssueItemRepository repository;
    private final CouplingConverter converter;

    public CouplingService(IssueItemRepository repository, CouplingConverter converter) {
        this.repository = repository;
        this.converter = converter;
    }

    @Transactional(readOnly = true)
    public CouplingDTO getCoupling(UUID id) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new CouplingNotFoundException(id));
        if (!entity.isCoupling()) throw new CouplingNotFoundException(id);
        return converter.entityToCouplingDTO(entity);
    }


    @Transactional
    public CouplingDTO patchCoupling(UUID id, ItemPatchDTO patch) {
        if (repository.patchItem(id, patch) != 1) throw new CouplingNotFoundException(id);
        return getCoupling(id);
    }
}
