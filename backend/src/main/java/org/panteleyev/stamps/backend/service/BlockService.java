// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend.service;

import org.panteleyev.stamps.backend.converter.BlockConverter;
import org.panteleyev.stamps.backend.domain.IssueItemEntity;
import org.panteleyev.stamps.backend.exception.BlockNotFoundException;
import org.panteleyev.stamps.backend.repository.IssueItemRepository;
import org.panteleyev.stamps.dto.BlockDTO;
import org.panteleyev.stamps.dto.ItemPatchDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

import static org.panteleyev.stamps.backend.domain.IssueItemSpecifications.hasIssue;

@Service
public class BlockService {
    private final IssueItemRepository repository;
    private final BlockConverter converter;

    public BlockService(IssueItemRepository repository, BlockConverter converter) {
        this.repository = repository;
        this.converter = converter;
    }

    @Transactional(readOnly = true)
    public BlockDTO getBlock(UUID id) {
        var entity = repository.findById(id).orElseThrow(() -> new BlockNotFoundException(id));
        if (!entity.isBlock()) throw new BlockNotFoundException(id);
        var blockStamps = repository.findAll(hasIssue(entity.getIssue())).stream()
                .filter(IssueItemEntity::isStamp)
                .filter(s -> Objects.equals(s.getBlockNumber(), entity.getNumberZag()))
                .toList();
        return converter.entityToBlockDTO(entity, blockStamps);
    }

    @Transactional
    public BlockDTO patchBlock(UUID id, ItemPatchDTO patch) {
        if (repository.patchItem(id, patch) != 1) throw new BlockNotFoundException(id);
        return getBlock(id);
    }
}
