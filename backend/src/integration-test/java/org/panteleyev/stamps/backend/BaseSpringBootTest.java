// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.panteleyev.stamps.backend.Profiles.IT;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(IT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class BaseSpringBootTest {
    public static final String REGION_USSR = "СССР";
    public static final int NUMBER_DOES_NOT_EXIST = 100000;
}
