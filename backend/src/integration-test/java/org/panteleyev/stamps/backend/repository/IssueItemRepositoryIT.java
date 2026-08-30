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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.argumentSet;

public class IssueItemRepositoryIT extends BaseSpringBootTest {
    @Autowired
    private IssueItemRepository repository;

    @Test
    @Sql("/sql/initRegions.sql")
    @Sql("/sql/initTags.sql")
    @Sql("/sql/IssueItemRepositoryIT/testFindByRegionAndYearBetween.sql")
    @Transactional
    public void testFindByRegionAndYearBetween() {
        var items = repository.findByRegionAndYearBetween(REGION_USSR, 1982, 1982).toList();
        assertThat(items).hasSize(6);
    }


    private static List<Arguments> testFindByRegionAndYearBetweenWithTagsArguments() {
        return List.of(
                argumentSet("No tags", Set.of(), List.of()),
                argumentSet("Unknown tag", Set.of("---"), List.of()),
                argumentSet("1 tag", Set.of("Космос"), List.of(5280, 5282, 5284)),
                argumentSet("2 tags", Set.of("Космос", "Живопись"), List.of(5280, 5281, 5282, 5284))
        );
    }

    @ParameterizedTest
    @MethodSource("testFindByRegionAndYearBetweenWithTagsArguments")
    @Sql("/sql/initRegions.sql")
    @Sql("/sql/initTags.sql")
    @Sql("/sql/IssueItemRepositoryIT/testFindByRegionAndYearBetween.sql")
    @Transactional
    public void testFindByRegionAndYearBetweenWithTags(Set<String> tags, List<Integer> expectedNumbers) {
        var actualNumbers = repository.findByRegionAndYearBetweenWithTags(REGION_USSR, 1982, 1982, tags)
                .map(IssueItemEntity::getNumberZag)
                .toList();
        assertThat(actualNumbers).containsExactlyInAnyOrderElementsOf(expectedNumbers);
    }

    @Test
    @Sql("/sql/initRegions.sql")
    @Sql("/sql/initTags.sql")
    @Sql("/sql/IssueItemRepositoryIT/testFindByRegionAndNumberZag.sql")
    @Transactional
    public void testFindByRegionAndNumberZag() {
        // 1 match
        var items = repository.findByRegionAndNumberZag(REGION_USSR, 5282).toList();
        assertThat(items).hasSize(1);
        assertThat(items.getFirst().getNumberZag()).isEqualTo(5282);

        // No match
        var emptyList = repository.findByRegionAndNumberZag(REGION_USSR, NUMBER_DOES_NOT_EXIST).toList();
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
