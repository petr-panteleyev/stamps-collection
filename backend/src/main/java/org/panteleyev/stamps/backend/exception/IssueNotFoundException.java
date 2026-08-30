// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend.exception;

import java.util.UUID;

public class IssueNotFoundException extends RuntimeException {
    private final UUID id;

    public IssueNotFoundException(UUID id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return "Issue " + id + " not found";
    }
}
