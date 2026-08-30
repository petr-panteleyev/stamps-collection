// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend.service;

import jakarta.persistence.EntityNotFoundException;
import org.panteleyev.stamps.backend.converter.StampConverter;
import org.panteleyev.stamps.backend.exception.StampNotFoundException;
import org.panteleyev.stamps.backend.repository.IssueItemRepository;
import org.panteleyev.stamps.dto.ItemPatchDTO;
import org.panteleyev.stamps.dto.StampDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class StampService {
    private final IssueItemRepository repository;
    private final StampConverter converter;

    public StampService(IssueItemRepository repository, StampConverter converter) {
        this.repository = repository;
        this.converter = converter;
    }

    @Transactional(readOnly = true)
    public StampDTO getStamp(UUID id) {
        var entity = repository.findById(id).orElseThrow(() -> new StampNotFoundException(id));
        if (!entity.isStamp()) throw new StampNotFoundException(id);
        return converter.entityToStampDTO(entity);
    }

    @Transactional
    public StampDTO patchStamp(UUID id, ItemPatchDTO patch) {
        if (repository.patchItem(id, patch) != 1) throw new StampNotFoundException(id);
        return getStamp(id);
    }
}
