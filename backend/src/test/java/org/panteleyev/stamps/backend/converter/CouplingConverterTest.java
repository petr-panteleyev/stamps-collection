// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend.converter;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.panteleyev.stamps.backend.domain.IssueEntity;
import org.panteleyev.stamps.backend.domain.IssueItemEntity;
import org.panteleyev.stamps.backend.domain.IssueItemType;
import org.panteleyev.stamps.backend.domain.TagEntity;
import org.panteleyev.stamps.dto.CouplingDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.argumentSet;
import static org.panteleyev.stamps.backend.test.TestObjectFactory.randomId;

public class CouplingConverterTest {
    private static final UUID ISSUE_ENTITY_ID = UUID.randomUUID();
    private static final UUID ISSUE_ITEM_ENTITY_ID = UUID.randomUUID();

    private static final TagEntity TAG_1 = new TagEntity().setId(randomId()).setName("Tag1");
    private static final TagEntity TAG_2 = new TagEntity().setId(randomId()).setName("Tag2");
    private static final TagEntity TAG_3 = new TagEntity().setId(randomId()).setName("Tag3");

    private final CouplingConverter converter = new CouplingConverter();

    private static List<Arguments> testCouplingDTOToEntityArguments() {
        return List.of(
                argumentSet("Null values, no tags",
                        new CouplingDTO()
                                .id(ISSUE_ITEM_ENTITY_ID)
                                .stampNumbers(List.of(1000, 1001, 1002)),
                        new IssueEntity().setId(ISSUE_ENTITY_ID),
                        List.of(new IssueItemEntity().setNumberZag(1000),
                                new IssueItemEntity().setNumberZag(1001),
                                new IssueItemEntity().setNumberZag(1002)),
                        new IssueItemEntity()
                                .setId(ISSUE_ITEM_ENTITY_ID)
                                .setType(IssueItemType.COUPLING)
                                .setNumberZag(1000)
                                .setNumberCfa(null)
                                .setComment("")
                                .setHasCancelled(false)
                                .setHasClean(false)
                                .setReplacementRequired(false)
                                .setCouplingItems("1000,1001,1002")
                ),
                argumentSet("Full set, tags",
                        new CouplingDTO()
                                .id(ISSUE_ENTITY_ID)
                                .comment("Comment")
                                .hasCancelled(true)
                                .hasClean(true)
                                .replacementRequired(true)
                                .stampNumbers(List.of(1000, 1001, 1002)),
                        new IssueEntity().setId(ISSUE_ENTITY_ID),
                        List.of(new IssueItemEntity().setNumberZag(1000).setTags(Set.of(TAG_1, TAG_2)),
                                new IssueItemEntity().setNumberZag(1001).setTags(Set.of(TAG_3)),
                                new IssueItemEntity().setNumberZag(1002)),
                        new IssueItemEntity()
                                .setId(ISSUE_ENTITY_ID)
                                .setType(IssueItemType.COUPLING)
                                .setNumberZag(1000)
                                .setNumberCfa(null)
                                .setComment("Comment")
                                .setHasCancelled(true)
                                .setHasClean(true)
                                .setReplacementRequired(true)
                                .setCouplingItems("1000,1001,1002")
                                .setTags(Set.of(TAG_1, TAG_2, TAG_3))
                )
        );
    }

    @ParameterizedTest
    @MethodSource("testCouplingDTOToEntityArguments")
    public void testCouplingDTOToEntity(CouplingDTO dto, IssueEntity issue, List<IssueItemEntity> stamps,
            IssueItemEntity expected)
    {
        var actual = converter.couplingDTOToEntity(dto, issue, stamps);
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getIssue().getId()).isEqualTo(issue.getId());

        assertThat(actual.getType()).isEqualTo(expected.getType());
        assertThat(actual.getNumberZag()).isEqualTo(expected.getNumberZag());
        assertThat(actual.getNumberCfa()).isNull();
        assertThat(actual.getComment()).isEqualTo(expected.getComment());

        assertThat(actual.getCouplingItems()).isEqualTo(expected.getCouplingItems());

        assertThat(actual.getHasClean()).isEqualTo(expected.getHasClean());
        assertThat(actual.getHasCancelled()).isEqualTo(expected.getHasCancelled());
        assertThat(actual.getReplacementRequired()).isEqualTo(expected.getReplacementRequired());

        assertThat(actual.getBlockNumber()).isNull();
        assertThat(actual.getDescription()).isEmpty();
        assertThat(actual.getDenomination()).isEqualTo(BigDecimal.ZERO);

        assertThat(actual.getTags()).containsExactlyInAnyOrderElementsOf(expected.getTags());
    }

    private static List<Arguments> testEntityToCouplingDTOArguments() {
        return List.of(
                argumentSet("All fields",
                        new IssueItemEntity()
                                .setType(IssueItemType.COUPLING)
                                .setComment("Comment")
                                .setHasCancelled(true)
                                .setHasClean(true)
                                .setReplacementRequired(true)
                                .setCouplingItems("1000,1001,1002"),
                        new CouplingDTO()
                                .comment("Comment")
                                .hasCancelled(true)
                                .hasClean(true)
                                .replacementRequired(true)
                                .stampNumbers(List.of(1000, 1001, 1002))
                )
        );
    }

    @ParameterizedTest
    @MethodSource("testEntityToCouplingDTOArguments")
    public void testEntityToCouplingDTO(IssueItemEntity entity, CouplingDTO expected) {
        var actual = converter.entityToCouplingDTO(entity);
        assertThat(actual).isEqualTo(expected);
    }
}
