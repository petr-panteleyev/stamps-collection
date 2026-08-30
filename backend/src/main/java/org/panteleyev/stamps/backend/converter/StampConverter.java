// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend.converter;

import org.panteleyev.stamps.backend.domain.IssueEntity;
import org.panteleyev.stamps.backend.domain.IssueItemEntity;
import org.panteleyev.stamps.backend.domain.IssueItemType;
import org.panteleyev.stamps.backend.domain.TagEntity;
import org.panteleyev.stamps.dto.StampDTO;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.panteleyev.stamps.backend.converter.ConverterUtils.convertBoolean;
import static org.panteleyev.stamps.backend.converter.ConverterUtils.convertString;

@Component
public class StampConverter {

    public IssueItemEntity stampDTOToEntity(StampDTO dto, IssueEntity issue, Set<TagEntity> tags) {
        var entity = new IssueItemEntity();
        entity.setId(dto.getId() == null ? UUID.randomUUID() : dto.getId());

        entity.setType(IssueItemType.STAMP);
        entity.setIssue(issue);
        entity.setYear(issue.getYear());
        entity.setNumberZag(dto.getNumberZag());
        entity.setNumberCfa(dto.getNumberCfa());
        entity.setDenomination(dto.getDenomination());
        entity.setDescription(convertString(dto.getDescription()));
        entity.setBlockNumber(null);
        entity.setCouplingItems(null);
        entity.setHasClean(convertBoolean(dto.getHasClean()));
        entity.setHasCancelled(convertBoolean(dto.getHasCancelled()));
        entity.setReplacementRequired(convertBoolean(dto.getReplacementRequired()));
        entity.setComment(convertString(dto.getComment()));
        entity.setTags(tags.stream().filter(
                tag -> dto.getTags().contains(tag.getName())
        ).collect(Collectors.toSet()));

        return entity;
    }

    public StampDTO entityToStampDTO(IssueItemEntity entity) {
        assert entity.getType() == IssueItemType.STAMP;

        var tags = entity.getTags().stream()
                .map(TagEntity::getName)
                .sorted()
                .toList();
        return new StampDTO()
                .id(entity.getId())
                .numberZag(entity.getNumberZag())
                .numberCfa(entity.getNumberCfa())
                .denomination(entity.getDenomination())
                .description(entity.getDescription())
                .hasClean(entity.getHasClean())
                .hasCancelled(entity.getHasCancelled())
                .replacementRequired(entity.getReplacementRequired())
                .comment(entity.getComment())
                .tags(tags);
    }

}
