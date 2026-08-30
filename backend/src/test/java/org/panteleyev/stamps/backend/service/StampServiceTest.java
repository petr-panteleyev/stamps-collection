// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.panteleyev.stamps.backend.converter.StampConverter;
import org.panteleyev.stamps.backend.domain.IssueItemEntity;
import org.panteleyev.stamps.backend.domain.IssueItemType;
import org.panteleyev.stamps.backend.exception.StampNotFoundException;
import org.panteleyev.stamps.backend.repository.IssueItemRepository;
import org.panteleyev.stamps.dto.ItemPatchDTO;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.panteleyev.stamps.backend.test.TestObjectFactory.randomId;

public class StampServiceTest {
    private static final UUID STAMP_ID = randomId();

    private final IssueItemRepository repository = mock();
    private final StampService service = new StampService(repository, new StampConverter());

    @Test
    @DisplayName("should successfully patch stamp")
    public void testPatchStamp() {
        var patch = new ItemPatchDTO();

        when(repository.findById(STAMP_ID)).thenReturn(Optional.of(
                new IssueItemEntity()
                        .setType(IssueItemType.STAMP)
                        .setHasClean(false)
                        .setHasCancelled(false)
                        .setReplacementRequired(false)
                        .setId(STAMP_ID)
        ));
        when(repository.patchItem(STAMP_ID, patch)).thenReturn(1);

        service.patchStamp(STAMP_ID, patch);
        verify(repository).patchItem(STAMP_ID, patch);
    }

    @Test
    @DisplayName("should throw on invalid id")
    public void testPatchStampThrow() {
        var patch = new ItemPatchDTO();
        when(repository.patchItem(STAMP_ID, patch)).thenReturn(0);

        assertThrows(StampNotFoundException.class, () -> service.patchStamp(STAMP_ID, patch));
    }
}
