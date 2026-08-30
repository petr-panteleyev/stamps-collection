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
import org.panteleyev.stamps.dto.BlockDTO;
import org.panteleyev.stamps.dto.StampDTO;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.argumentSet;
import static org.panteleyev.stamps.backend.test.TestObjectFactory.randomId;

public class BlockConverterTest {
    private static final UUID ISSUE_ENTITY_ID = UUID.randomUUID();
    private static final UUID BLOCK_ENTITY_ID = UUID.randomUUID();

    private static final TagEntity TAG_1 = new TagEntity().setId(randomId()).setName("Tag1");
    private static final TagEntity TAG_2 = new TagEntity().setId(randomId()).setName("Tag2");
    private static final TagEntity TAG_3 = new TagEntity().setId(randomId()).setName("Tag3");

    private final BlockConverter converter = new BlockConverter(new StampConverter());

    private static List<Arguments> testBlockDTOToEntityArguments() {
        return List.of(
                argumentSet("All fields",
                        new BlockDTO()
                                .id(BLOCK_ENTITY_ID)
                                .description("Block description")
                                .comment("Block comment")
                                .hasCancelled(true)
                                .hasClean(true)
                                .replacementRequired(true)
                                .numberZag(1000)
                                .numberCfa(2000)
                                .stamps(List.of(
                                        new StampDTO()
                                                .id(randomId())
                                                .numberZag(3000),
                                        new StampDTO()
                                                .id(randomId())
                                                .numberZag(3001),
                                        new StampDTO()
                                                .id(randomId())
                                                .numberZag(3002)
                                ))
                                .tags(List.of("Tag1", "Tag3")),
                        new IssueEntity().setId(ISSUE_ENTITY_ID).setYear(2026),
                        Set.of(TAG_1, TAG_2, TAG_3),
                        List.of(
                                new IssueItemEntity()
                                        .setId(BLOCK_ENTITY_ID)
                                        .setType(IssueItemType.BLOCK)
                                        .setDescription("Block description")
                                        .setComment("Block comment")
                                        .setYear(2026)
                                        .setNumberZag(1000)
                                        .setNumberCfa(2000)
                                        .setDenomination(BigDecimal.ZERO)
                                        .setHasCancelled(true)
                                        .setHasClean(true)
                                        .setReplacementRequired(true)
                                        .setTags(Set.of(TAG_1, TAG_3)),
                                new IssueItemEntity()
                                        .setType(IssueItemType.STAMP)
                                        .setBlockNumber(1000)
                                        .setNumberZag(3000),
                                new IssueItemEntity()
                                        .setType(IssueItemType.STAMP)
                                        .setBlockNumber(1000)
                                        .setNumberZag(3001),
                                new IssueItemEntity()
                                        .setType(IssueItemType.STAMP)
                                        .setBlockNumber(1000)
                                        .setNumberZag(3002)
                        )
                )
        );
    }

    @ParameterizedTest
    @MethodSource("testBlockDTOToEntityArguments")
    public void testBlockDTOToEntity(BlockDTO dto, IssueEntity issue, Set<TagEntity> tags,
            List<IssueItemEntity> expected)
    {
        var actual = converter.blockDTOToEntity(dto, issue, tags);

        // Entity for block
        var blockEntityList = actual.stream()
                .filter(e -> e.getType() == IssueItemType.BLOCK)
                .toList();
        assertThat(blockEntityList).hasSize(1);

        var blockEntity = blockEntityList.getFirst();
        assertThat(blockEntity.getId()).isEqualTo(dto.getId());
        assertThat(blockEntity.getIssue().getId()).isEqualTo(issue.getId());

        var expectedBlockEntity = expected.getFirst();

        assertThat(blockEntity.getNumberCfa()).isEqualTo(expectedBlockEntity.getNumberCfa());
        assertThat(blockEntity.getNumberZag()).isEqualTo(expectedBlockEntity.getNumberZag());

        assertThat(blockEntity.getYear()).isEqualTo(expectedBlockEntity.getYear());
        assertThat(blockEntity.getDenomination()).isEqualTo(expectedBlockEntity.getDenomination());
        assertThat(blockEntity.getComment()).isEqualTo(expectedBlockEntity.getComment());
        assertThat(blockEntity.getDescription()).isEqualTo(expectedBlockEntity.getDescription());

        assertThat(blockEntity.getHasClean()).isEqualTo(expectedBlockEntity.getHasClean());
        assertThat(blockEntity.getHasCancelled()).isEqualTo(expectedBlockEntity.getHasCancelled());
        assertThat(blockEntity.getReplacementRequired()).isEqualTo(expectedBlockEntity.getReplacementRequired());

        assertThat(blockEntity.getTags()).containsExactlyInAnyOrderElementsOf(expectedBlockEntity.getTags());

        // Entities for stamps

        var stampEntityList = actual.stream()
                .filter(e -> e.getType() == IssueItemType.STAMP)
                .sorted((o1, o2) -> Comparator.comparingInt(IssueItemEntity::getNumberZag).compare(o1, o2))
                .toList();
        var expectedStampEntityList = expected.stream()
                .filter(e -> e.getType() == IssueItemType.STAMP)
                .sorted((o1, o2) -> Comparator.comparingInt(IssueItemEntity::getNumberZag).compare(o1, o2))
                .toList();

        assertThat(stampEntityList).hasSize(expectedStampEntityList.size());

        for (int i = 0; i < stampEntityList.size(); i++) {
            assertStampEntity(stampEntityList.get(i), expectedStampEntityList.get(i), blockEntity);
        }
    }

    private static void assertStampEntity(IssueItemEntity actual, IssueItemEntity expected, IssueItemEntity block) {
        assertThat(actual.getNumberZag()).isEqualTo(expected.getNumberZag());
        assertThat(actual.getBlockNumber()).isEqualTo(expected.getBlockNumber());

        assertThat(actual.getIssue().getId()).isEqualTo(block.getIssue().getId());

        assertThat(actual.getHasClean()).isEqualTo(block.getHasClean());
        assertThat(actual.getHasCancelled()).isEqualTo(block.getHasCancelled());
        assertThat(actual.getReplacementRequired()).isEqualTo(block.getReplacementRequired());
    }
}
