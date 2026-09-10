// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.panteleyev.stamps.backend.BaseSpringBootTest;
import org.panteleyev.stamps.backend.domain.IssueItemEntity;
import org.panteleyev.stamps.dto.ItemPatchDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.argumentSet;
import static org.panteleyev.stamps.backend.domain.IssueItemSpecifications.doesNotHaveTags;
import static org.panteleyev.stamps.backend.domain.IssueItemSpecifications.hasNumberZag;
import static org.panteleyev.stamps.backend.domain.IssueItemSpecifications.hasRegion;
import static org.panteleyev.stamps.backend.domain.IssueItemSpecifications.hasTags;
import static org.panteleyev.stamps.backend.domain.IssueItemSpecifications.hasYearBetween;

public class IssueItemRepositoryIT extends BaseSpringBootTest {
    @Autowired
    private IssueItemRepository repository;

    private static List<Arguments> testSpecificationsArguments() {
        return List.of(
                argumentSet("No conditions",
                        null, null, null, null, null,
                        List.of(
                                161, 5280, 5281, 5282, 5283, 5284,
                                1232, 1233, 1234, 1235, 1236
                        )
                ),
                argumentSet("Region",
                        "СССР", null, null, null, null,
                        List.of(161, 5280, 5281, 5282, 5283, 5284)
                ),
                argumentSet("Region and start year",
                        "СССР", 1983, null, null, null,
                        List.of(5281, 5282, 5283, 5284)
                ),
                argumentSet("Region and end year",
                        "СССР", null, 1983, null, null,
                        List.of(161, 5280, 5281)
                ),
                argumentSet("Region and both years",
                        "СССР", 1983, 1985, null, null,
                        List.of(5281, 5282, 5283)
                ),
                argumentSet("Region and tags",
                        "СССР", null, null, Set.of("Космос", "Спорт"), null,
                        List.of(5280, 5282, 5283, 5284)
                ),
                argumentSet("Tags only",
                        null, null, null, Set.of("Космос"), null,
                        List.of(5280, 5282, 5284, 1232, 1235, 1236)
                ),
                argumentSet("Excluded tags only",
                        null, null, null, null, Set.of("Космос"),
                        List.of(5281, 5283, 1233, 1234, 161)
                ),
                argumentSet("No tags",
                        null, null, null, Set.of(), null,
                        List.of(161)
                )
        );
    }

    @ParameterizedTest
    @MethodSource("testSpecificationsArguments")
    @Sql("/sql/initRegions.sql")
    @Sql("/sql/initTags.sql")
    @Sql("/sql/IssueItemRepositoryIT/testSpecifications.sql")
    @Transactional
    public void testSpecifications(String region, Integer startYear, Integer endYear,
            Collection<String> tags, Collection<String> excludedTags,
            List<Integer> expected)
    {
        var actual = repository.findAll(
                Stream.of(hasRegion(region), hasYearBetween(startYear, endYear),
                                hasTags(tags), doesNotHaveTags(excludedTags))
                        .reduce(Specification::and)
                        .orElse(null)
        );
        assertThat(actual.stream().map(IssueItemEntity::getNumberZag).toList()).
                containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    @Sql("/sql/initRegions.sql")
    @Sql("/sql/initTags.sql")
    @Sql("/sql/IssueItemRepositoryIT/testFindByRegionAndNumberZag.sql")
    @Transactional
    public void testFindByRegionAndNumberZag() {
        // 1 match
        var items = repository.findAll(
                Stream.of(hasRegion(REGION_USSR), hasNumberZag(5282))
                        .reduce(Specification::and)
                        .orElse(null)
        );
        assertThat(items).hasSize(1);
        assertThat(items.getFirst().getNumberZag()).isEqualTo(5282);

        // No match
        var emptyList = repository.findAll(
                Stream.of(hasRegion(REGION_USSR), hasNumberZag(NUMBER_DOES_NOT_EXIST))
                        .reduce(Specification::and)
                        .orElse(null)
        );
        assertThat(emptyList).isEmpty();
    }

    private static List<Arguments> testPatchItemArguments() {
        return List.of(
                argumentSet("All columns",
                        new ItemPatchDTO()
                                .hasClean(true)
                                .hasCancelled(true)
                                .replacementRequired(true),
                        new IssueItemEntity()
                                .setHasClean(true)
                                .setHasCancelled(true)
                                .setReplacementRequired(true)
                ),
                argumentSet("No columns",
                        new ItemPatchDTO(),
                        new IssueItemEntity()
                                .setHasClean(false)
                                .setHasCancelled(false)
                                .setReplacementRequired(false)
                ),
                argumentSet("One column",
                        new ItemPatchDTO().replacementRequired(true),
                        new IssueItemEntity()
                                .setHasClean(false)
                                .setHasCancelled(false)
                                .setReplacementRequired(true)
                )
        );
    }

    @ParameterizedTest
    @MethodSource("testPatchItemArguments")
    @Sql("/sql/initRegions.sql")
    @Sql("/sql/IssueItemRepositoryIT/testPatchItem.sql")
    @Transactional
    public void testPatchItem(ItemPatchDTO patch, IssueItemEntity expected) {
        var id = UUID.fromString("c977f05c-03aa-4cf3-af2d-6a5e093b653b");

        var count = repository.patchItem(id, patch);
        assertThat(count).isEqualTo(1);

        var actual = repository.findById(id).orElseThrow();
        assertThat(actual.getHasClean()).isEqualTo(expected.getHasClean());
        assertThat(actual.getHasCancelled()).isEqualTo(expected.getHasCancelled());
        assertThat(actual.getReplacementRequired()).isEqualTo(expected.getReplacementRequired());
    }
}
