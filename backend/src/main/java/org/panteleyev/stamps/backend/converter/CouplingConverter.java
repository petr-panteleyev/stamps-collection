// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend.converter;

import org.panteleyev.stamps.backend.domain.IssueEntity;
import org.panteleyev.stamps.backend.domain.IssueItemEntity;
import org.panteleyev.stamps.backend.domain.IssueItemType;
import org.panteleyev.stamps.dto.CouplingDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static org.panteleyev.stamps.backend.converter.ConverterUtils.convertBoolean;
import static org.panteleyev.stamps.backend.converter.ConverterUtils.convertString;

@Component
public class CouplingConverter {

    public IssueItemEntity couplingDTOToEntity(CouplingDTO dto, IssueEntity issue, List<IssueItemEntity> stamps) {
        var issueNumbers = stamps.stream().map(IssueItemEntity::getNumberZag).sorted().toList();

        var entity = new IssueItemEntity();
        entity.setId(dto.getId() == null ? UUID.randomUUID() : dto.getId());

        entity.setType(IssueItemType.COUPLING);
        entity.setIssue(issue);
        entity.setYear(issue.getYear());
        entity.setNumberZag(issueNumbers.isEmpty() ? 0 : issueNumbers.getFirst());
        entity.setDenomination(BigDecimal.ZERO);
        entity.setDescription("");
        entity.setHasClean(convertBoolean(dto.getHasClean()));
        entity.setHasCancelled(convertBoolean(dto.getHasCancelled()));
        entity.setReplacementRequired(convertBoolean(dto.getReplacementRequired()));
        entity.setComment(convertString(dto.getComment()));
        entity.setCouplingItems(
                dto.getStampNumbers().stream().map(i -> Integer.toString(i)).collect(Collectors.joining(","))
        );
        entity.setTags(stamps.stream().flatMap(s -> s.getTags().stream()).collect(Collectors.toSet()));

        return entity;
    }

    public CouplingDTO entityToCouplingDTO(IssueItemEntity entity) {
        assert entity.getType() == IssueItemType.COUPLING;

        var items = requireNonNull(entity.getCouplingItems());
        var stamps = Arrays.stream(items.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .toList();
        return new CouplingDTO()
                .id(entity.getId())
                .hasClean(entity.getHasClean())
                .hasCancelled(entity.getHasCancelled())
                .replacementRequired(entity.getReplacementRequired())
                .comment(entity.getComment())
                .stampNumbers(stamps);
    }
}
