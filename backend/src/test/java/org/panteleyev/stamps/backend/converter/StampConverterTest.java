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
import org.panteleyev.stamps.dto.StampDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.argumentSet;
import static org.panteleyev.stamps.backend.test.TestObjectFactory.randomId;

public class StampConverterTest {
    private static final UUID ISSUE_ENTITY_ID = UUID.randomUUID();
    private static final UUID ISSUE_ITEM_ENTITY_ID = UUID.randomUUID();

    private static final TagEntity TAG_1 = new TagEntity().setId(randomId()).setName("Tag1");
    private static final TagEntity TAG_2 = new TagEntity().setId(randomId()).setName("Tag2");
    private static final TagEntity TAG_3 = new TagEntity().setId(randomId()).setName("Tag3");

    private final StampConverter converter = new StampConverter();

    private static List<Arguments> testStampDTOToEntityArguments() {
        return List.of(
                argumentSet("All fields",
                        new StampDTO()
                                .id(ISSUE_ITEM_ENTITY_ID)
                                .numberZag(1000)
                                .numberCfa(2000)
                                .denomination(BigDecimal.TEN)
                                .comment("Comment")
                                .description("Description")
                                .hasCancelled(true)
                                .hasClean(true)
                                .replacementRequired(true)
                                .tags(List.of("Tag1", "Tag3")),
                        new IssueEntity().setId(ISSUE_ENTITY_ID).setYear(2026),
                        Set.of(TAG_1, TAG_2, TAG_3),
                        new IssueItemEntity()
                                .setId(ISSUE_ITEM_ENTITY_ID)
                                .setType(IssueItemType.STAMP)
                                .setNumberZag(1000)
                                .setNumberCfa(2000)
                                .setYear(2026)
                                .setDenomination(BigDecimal.TEN)
                                .setComment("Comment")
                                .setDescription("Description")
                                .setHasCancelled(true)
                                .setHasClean(true)
                                .setReplacementRequired(true)
                                .setTags(Set.of(TAG_1, TAG_3))
                )
        );
    }

    @ParameterizedTest
    @MethodSource("testStampDTOToEntityArguments")
    public void testStampDTOToEntity(StampDTO dto, IssueEntity issue, Set<TagEntity> tags, IssueItemEntity expected) {
        var actual = converter.stampDTOToEntity(dto, issue, tags);

        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getIssue().getId()).isEqualTo(issue.getId());

        assertThat(actual.getType()).isEqualTo(expected.getType());
        assertThat(actual.getNumberZag()).isEqualTo(expected.getNumberZag());
        assertThat(actual.getNumberCfa()).isEqualTo(expected.getNumberCfa());
        assertThat(actual.getYear()).isEqualTo(expected.getYear());
        assertThat(actual.getDenomination()).isEqualTo(expected.getDenomination());
        assertThat(actual.getComment()).isEqualTo(expected.getComment());
        assertThat(actual.getDescription()).isEqualTo(expected.getDescription());

        assertThat(actual.getHasClean()).isEqualTo(expected.getHasClean());
        assertThat(actual.getHasCancelled()).isEqualTo(expected.getHasCancelled());
        assertThat(actual.getReplacementRequired()).isEqualTo(expected.getReplacementRequired());

        assertThat(actual.getTags()).containsExactlyInAnyOrderElementsOf(expected.getTags());

        assertThat(actual.getBlockNumber()).isNull();
        assertThat(actual.getCouplingItems()).isNull();
    }

    private static List<Arguments> testEntityToStampDTOArguments() {
        return List.of(
                argumentSet("All fields",
                        new IssueItemEntity()
                                .setId(ISSUE_ITEM_ENTITY_ID)
                                .setType(IssueItemType.STAMP)
                                .setNumberZag(1000)
                                .setNumberCfa(2000)
                                .setYear(2026)
                                .setDenomination(BigDecimal.TEN)
                                .setComment("Comment")
                                .setDescription("Description")
                                .setHasCancelled(true)
                                .setHasClean(true)
                                .setReplacementRequired(true)
                                .setTags(Set.of(TAG_1, TAG_3)),
                        new StampDTO()
                                .id(ISSUE_ITEM_ENTITY_ID)
                                .numberZag(1000)
                                .numberCfa(2000)
                                .denomination(BigDecimal.TEN)
                                .comment("Comment")
                                .description("Description")
                                .hasCancelled(true)
                                .hasClean(true)
                                .replacementRequired(true)
                                .tags(List.of("Tag1", "Tag3"))
                )
        );
    }

    @ParameterizedTest
    @MethodSource("testEntityToStampDTOArguments")
    public void testEntityToStampDTO(IssueItemEntity entity, StampDTO expected) {
        var actual = converter.entityToStampDTO(entity);
        assertThat(actual).isEqualTo(expected);
    }
}
