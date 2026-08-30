// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend.exception;

import org.panteleyev.stamps.backend.domain.IssueItemType;

import java.util.UUID;

public class CouplingNotFoundException extends IssueItemNotFoundException {
    public CouplingNotFoundException(UUID id) {
        super(id, IssueItemType.COUPLING);
    }
}
