// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.panteleyev.stamps.backend.BaseSpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class TagRepositoryIT extends BaseSpringBootTest {
    @Autowired
    private TagRepository repository;

    @Test
    @DisplayName("should find tags by names")
    @Sql("/sql/initTags.sql")
    public void testFindByNameIn() {
        var set1 = repository.findByNameIn(Set.of("Космос", "Живопись", "Спорт"));
        assertThat(set1).hasSize(3);

        var set2 = repository.findByNameIn(Set.of("Космос", "Живопись", UUID.randomUUID().toString()));
        assertThat(set2).hasSize(2);

        var emptySet = repository.findByNameIn(Set.of());
        assertThat(emptySet).isEmpty();
    }
}
