// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend.test;

import java.util.Random;
import java.util.UUID;

public final class TestObjectFactory {
    public static final Random RANDOM = new Random(System.currentTimeMillis());

    public static UUID randomId() {
        return UUID.randomUUID();
    }

    public static String randomString() {
        return UUID.randomUUID().toString();
    }

    public static int randomInt() {
        return randomInt(Integer.MAX_VALUE);
    }

    public static int randomInt(int bound) {
        return RANDOM.nextInt(bound);
    }

    private TestObjectFactory() {
    }
}
