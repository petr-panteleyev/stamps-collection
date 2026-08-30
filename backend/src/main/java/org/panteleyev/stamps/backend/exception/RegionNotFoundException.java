// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend.exception;

public class RegionNotFoundException extends RuntimeException {
    private final String region;

    public RegionNotFoundException(String region) {
        this.region = region;
    }

    @Override
    public String getMessage() {
        return "Region '" + region + "' not found";
    }
}
