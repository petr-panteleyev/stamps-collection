// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop;

public final class GlobalContext {
    private static final StampsService stampsService = new StampsService();

    public static StampsService stampsService() {
        return stampsService;
    }

    private GlobalContext(){}
}
