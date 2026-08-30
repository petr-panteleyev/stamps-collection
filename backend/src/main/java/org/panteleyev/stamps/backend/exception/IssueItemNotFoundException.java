// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend.exception;

import org.panteleyev.stamps.backend.domain.IssueItemType;

import java.util.UUID;

public class IssueItemNotFoundException extends RuntimeException {
    private final UUID id;
    private final IssueItemType type;

    public IssueItemNotFoundException(UUID id, IssueItemType type) {
        this.id = id;
        this.type = type;
    }

    @Override
    public String getMessage() {
        var prefix = switch (type) {
            case IssueItemType.STAMP -> "Stamp";
            case IssueItemType.BLOCK -> "Block";
            case IssueItemType.COUPLING -> "Coupling";
        };

        return prefix + " " + id + " not found";
    }
}
