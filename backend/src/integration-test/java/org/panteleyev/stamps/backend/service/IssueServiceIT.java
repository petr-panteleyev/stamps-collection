// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend.service;

import org.junit.jupiter.api.Test;
import org.panteleyev.stamps.backend.BaseSpringBootTest;
import org.panteleyev.stamps.dto.BlockDTO;
import org.panteleyev.stamps.dto.CouplingDTO;
import org.panteleyev.stamps.dto.IssueDTO;
import org.panteleyev.stamps.dto.StampDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.panteleyev.stamps.backend.converter.ConverterUtils.convertBoolean;

public class IssueServiceIT extends BaseSpringBootTest {

    @Autowired
    private IssueService service;

    @Test
    @Sql("/sql/initRegions.sql")
    @Sql("/sql/initTags.sql")
    @Sql("/sql/IssueServiceIT/testPutIssue.sql")
    public void testPutIssue() {
        var issueId = UUID.randomUUID();
        var issueDate = LocalDate.now();
        var issueRegion = REGION_USSR;

        var issueDto = new IssueDTO()
                .id(issueId)
                .region(issueRegion)
                .title("Название")
                .date(issueDate)
                .stamps(List.of(
                        new StampDTO()
                                .numberZag(1000)
                                .numberCfa(2000)
                                .denomination(BigDecimal.ONE)
                                .description("Описание 1")
                                .comment("")
                                .hasClean(true)
                                .tags(List.of("Космос", "Живопись")),
                        new StampDTO()
                                .numberZag(1001)
                                .denomination(BigDecimal.TEN)
                                .description("Описание 2")
                                .comment("")
                                .hasCancelled(true)
                                .replacementRequired(true)
                                .tags(List.of("Космос", "Спорт")),
                        new StampDTO()
                                .numberZag(1002)
                                .denomination(BigDecimal.ZERO)
                                .description("Описание 3")
                                .comment("")
                ))
                .blocks(List.of(
                        new BlockDTO()
                                .numberZag(1000)
                                .description("Описание блока 1")
                                .comment("")
                                .hasClean(true)
                                .stamps(List.of(
                                        new StampDTO()
                                                .numberZag(1010)
                                                .denomination(BigDecimal.TEN)
                                                .description("Описание марки в блоке 1")
                                                .comment("")
                                                .hasClean(true),
                                        new StampDTO()
                                                .numberZag(1011)
                                                .denomination(BigDecimal.ZERO)
                                                .description("Описание марки в блоке 2")
                                                .comment("")
                                                .hasClean(true)
                                ))
                                .tags(List.of("Спорт"))
                ))
                .couplings(List.of(
                        new CouplingDTO()
                                .comment("Комментарий к сцепке")
                                .stampNumbers(List.of(1001, 1002))
                ));

        service.putIssue(issueDto);

        var actualList = service.getIssues(issueRegion, issueDate.getYear(), issueDate.getYear(), null);
        assertThat(actualList).hasSize(1);

        var actual = actualList.getFirst();
        assertThat(actual.getTitle()).isEqualTo(issueDto.getTitle());
        assertThat(actual.getDate()).isEqualTo(issueDto.getDate());

        assertThat(actual.getStamps()).hasSize(issueDto.getStamps().size());
        for (int i = 0; i < actual.getStamps().size(); i++) {
            assertStampDTO(actual.getStamps().get(i), issueDto.getStamps().get(i));
        }

        assertThat(actual.getBlocks()).hasSize(issueDto.getBlocks().size());
        for (int i = 0; i < actual.getBlocks().size(); i++) {
            assertBlockDTO(actual.getBlocks().get(i), issueDto.getBlocks().get(i));
        }

        assertThat(actual.getCouplings()).hasSize(issueDto.getCouplings().size());
        for (int i = 0; i < actual.getCouplings().size(); i++) {
            assertCouplingDTO(actual.getCouplings().get(i), issueDto.getCouplings().get(i));
        }
    }

    private static void assertStampDTO(StampDTO actual, StampDTO expected) {
        assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
        assertThat(actual.getDenomination()).isEqualTo(expected.getDenomination());
        assertThat(actual.getComment()).isEqualTo(expected.getComment());
        assertThat(actual.getNumberZag()).isEqualTo(expected.getNumberZag());
        assertThat(actual.getNumberCfa()).isEqualTo(expected.getNumberCfa());
        assertThat(actual.getTags()).containsExactlyInAnyOrderElementsOf(expected.getTags());
        assertThat(actual.getHasClean()).isEqualTo(convertBoolean(expected.getHasClean()));
        assertThat(actual.getHasCancelled()).isEqualTo(convertBoolean(expected.getHasCancelled()));
        assertThat(actual.getReplacementRequired()).isEqualTo(convertBoolean(expected.getReplacementRequired()));
    }

    private static void assertBlockDTO(BlockDTO actual, BlockDTO expected) {
        assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
        assertThat(actual.getComment()).isEqualTo(expected.getComment());
        assertThat(actual.getNumberZag()).isEqualTo(expected.getNumberZag());
        assertThat(actual.getNumberCfa()).isEqualTo(expected.getNumberCfa());
        assertThat(actual.getTags()).containsExactlyInAnyOrderElementsOf(expected.getTags());
        assertThat(actual.getHasClean()).isEqualTo(convertBoolean(expected.getHasClean()));
        assertThat(actual.getHasCancelled()).isEqualTo(convertBoolean(expected.getHasCancelled()));
        assertThat(actual.getReplacementRequired()).isEqualTo(convertBoolean(expected.getReplacementRequired()));

        assertThat(actual.getStamps()).hasSize(expected.getStamps().size());
        for (int i = 0; i < actual.getStamps().size(); i++) {
            assertStampDTO(actual.getStamps().get(i), expected.getStamps().get(i));
        }
    }

    private static void assertCouplingDTO(CouplingDTO actual, CouplingDTO expected) {
        assertThat(actual.getComment()).isEqualTo(expected.getComment());
        assertThat(actual.getHasClean()).isEqualTo(convertBoolean(expected.getHasClean()));
        assertThat(actual.getHasCancelled()).isEqualTo(convertBoolean(expected.getHasCancelled()));
        assertThat(actual.getReplacementRequired()).isEqualTo(convertBoolean(expected.getReplacementRequired()));
        assertThat(actual.getStampNumbers()).containsExactlyInAnyOrderElementsOf(expected.getStampNumbers());
    }
}
