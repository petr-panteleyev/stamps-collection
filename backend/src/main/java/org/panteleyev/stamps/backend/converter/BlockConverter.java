// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend.converter;

import org.panteleyev.stamps.backend.domain.IssueEntity;
import org.panteleyev.stamps.backend.domain.IssueItemEntity;
import org.panteleyev.stamps.backend.domain.IssueItemType;
import org.panteleyev.stamps.backend.domain.TagEntity;
import org.panteleyev.stamps.dto.BlockDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.panteleyev.stamps.backend.converter.ConverterUtils.convertBoolean;
import static org.panteleyev.stamps.backend.converter.ConverterUtils.convertString;
import static org.panteleyev.stamps.backend.converter.IssueConverter.STAMP_BY_NUMBER_ZAG;

@Component
public class BlockConverter {

    private final StampConverter stampConverter;

    public BlockConverter(StampConverter stampConverter) {
        this.stampConverter = stampConverter;
    }

    public BlockDTO entityToBlockDTO(IssueItemEntity entity, Collection<IssueItemEntity> blockStamps) {
        var tags = entity.getTags().stream().map(TagEntity::getName).toList();
        return new BlockDTO()
                .id(entity.getId())
                .year(entity.getYear())
                .numberZag(entity.getNumberZag())
                .numberCfa(entity.getNumberCfa())
                .description(entity.getDescription())
                .hasClean(entity.getHasClean())
                .hasCancelled(entity.getHasCancelled())
                .replacementRequired(entity.getReplacementRequired())
                .comment(entity.getComment())
                .stamps(blockStamps.stream()
                        .map(stampConverter::entityToStampDTO)
                        .sorted(STAMP_BY_NUMBER_ZAG)
                        .toList())
                .tags(tags);
    }

    public List<IssueItemEntity> blockDTOToEntity(BlockDTO dto, IssueEntity issue, Set<TagEntity> tags) {
        var result = new ArrayList<IssueItemEntity>();

        var entity = new IssueItemEntity();
        entity.setId(dto.getId() == null ? UUID.randomUUID() : dto.getId());

        entity.setType(IssueItemType.BLOCK);
        entity.setIssue(issue);

        var blockTags = tags.stream()
                .filter(tag -> dto.getTags().contains(tag.getName()))
                .collect(Collectors.toSet());

        entity.setYear(issue.getYear());
        entity.setNumberZag(dto.getNumberZag());
        entity.setNumberCfa(dto.getNumberCfa());
        entity.setDenomination(BigDecimal.ZERO); // Номинал блока определяется марками
        entity.setDescription(convertString(dto.getDescription()));
        entity.setHasClean(convertBoolean(dto.getHasClean()));
        entity.setHasCancelled(convertBoolean(dto.getHasCancelled()));
        entity.setReplacementRequired(convertBoolean(dto.getReplacementRequired()));
        entity.setComment(convertString(dto.getComment()));
        entity.setTags(blockTags);

        result.add(entity);

        dto.getStamps().stream()
                .map(s -> {
                    var stampEntity = stampConverter.stampDTOToEntity(s, issue, blockTags);
                    stampEntity.setBlockNumber(entity.getNumberZag())
                            .setHasClean(convertBoolean(entity.getHasClean()))
                            .setHasCancelled(convertBoolean(entity.getHasCancelled()))
                            .setReplacementRequired(convertBoolean(entity.getReplacementRequired()));
                    return stampEntity;
                })
                .forEach(result::add);

        return result;
    }
}
