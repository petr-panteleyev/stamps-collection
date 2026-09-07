// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop;

import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;

abstract public class BaseUnitTest {
    private static final Random RANDOM = new Random(System.currentTimeMillis());

    public static String randomString() {
        return UUID.randomUUID().toString();
    }

    public static boolean randomBoolean() {
        return RANDOM.nextBoolean();
    }

    public static int randomInt() {
        return RANDOM.nextInt(Integer.MAX_VALUE);
    }

    public static BigDecimal randomDecimal() {
        return BigDecimal.valueOf(RANDOM.nextLong(Long.MAX_VALUE));
    }
}
