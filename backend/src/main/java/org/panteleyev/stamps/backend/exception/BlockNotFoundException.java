// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend.exception;

import org.panteleyev.stamps.backend.domain.IssueItemType;

import java.util.UUID;

public class BlockNotFoundException extends IssueItemNotFoundException {
    public BlockNotFoundException(UUID id) {
        super(id, IssueItemType.BLOCK);
    }
}
