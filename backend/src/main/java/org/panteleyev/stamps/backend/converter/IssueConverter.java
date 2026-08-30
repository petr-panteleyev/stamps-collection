// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend.converter;

import org.panteleyev.stamps.backend.domain.IssueEntity;
import org.panteleyev.stamps.backend.domain.IssueItemEntity;
import org.panteleyev.stamps.backend.domain.IssueItemType;
import org.panteleyev.stamps.backend.domain.RegionEntity;
import org.panteleyev.stamps.backend.domain.TagEntity;
import org.panteleyev.stamps.dto.BlockDTO;
import org.panteleyev.stamps.dto.IssueDTO;
import org.panteleyev.stamps.dto.StampDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.util.Objects.requireNonNull;

@Component
public class IssueConverter {
    public static final Comparator<IssueDTO> ISSUE_BY_DATE = (o1, o2) -> {
        assert o1 != null && o1.getDate() != null;
        assert o2 != null && o2.getDate() != null;
        return o1.getDate().compareTo(o2.getDate());
    };

    static final Comparator<StampDTO> STAMP_BY_NUMBER_ZAG = Comparator.comparingInt(
            value -> value == null || value.getNumberZag() == null ? 0 : value.getNumberZag());
    private static final Comparator<BlockDTO> BLOCK_BY_NUMBER_ZAG = Comparator.comparingInt(
            value -> value == null || value.getNumberZag() == null ? 0 : value.getNumberZag());

    private final TagConverter tagConverter;
    private final StampConverter stampConverter;
    private final BlockConverter blockConverter;
    private final CouplingConverter couplingConverter;

    public IssueConverter(TagConverter tagConverter, StampConverter stampConverter, BlockConverter blockConverter,
            CouplingConverter couplingConverter)
    {
        this.tagConverter = requireNonNull(tagConverter);
        this.stampConverter = requireNonNull(stampConverter);
        this.blockConverter = requireNonNull(blockConverter);
        this.couplingConverter = requireNonNull(couplingConverter);
    }

    //

    public IssueEntity issueDtoToEntity(IssueDTO dto, RegionEntity regionEntity, Set<TagEntity> tagEntities) {
        assert dto.getStamps() != null;
        assert dto.getBlocks() != null;
        assert dto.getCouplings() != null;

        var entity = new IssueEntity();

        entity.setId(dto.getId());
        entity.setRegion(regionEntity);
        entity.setTitle(dto.getTitle());
        entity.setYear(dto.getDate().getYear());
        entity.setDate(dto.getDate());

        var entityStamps = dto.getStamps().stream()
                .map(stampDTO -> stampConverter.stampDTOToEntity(stampDTO, entity, tagEntities))
                .toList();
        var items = new ArrayList<>(entityStamps);

        dto.getBlocks().stream()
                .map(blockDTO -> blockConverter.blockDTOToEntity(blockDTO, entity, tagEntities))
                .forEach(items::addAll);

        dto.getCouplings().stream()
                .map(couplingDTO -> {
                    var stamps = entityStamps.stream().filter(
                            s -> couplingDTO.getStampNumbers().contains(s.getNumberZag())).toList();
                    return couplingConverter.couplingDTOToEntity(couplingDTO, entity, stamps);
                }).forEach(items::add);

        entity.setItems(items);

        return entity;
    }

    //

    public IssueDTO entityToDTO(IssueEntity entity, List<IssueItemEntity> items) {
        var stamps = items.stream()
                .filter(i -> i.getType() == IssueItemType.STAMP)
                .filter(i -> i.getBlockNumber() == null)
                .map(stampConverter::entityToStampDTO)
                .sorted(STAMP_BY_NUMBER_ZAG)
                .toList();

        var blocks = items.stream()
                .filter(i -> i.getType() == IssueItemType.BLOCK)
                .map(block -> blockConverter.entityToBlockDTO(block, items.stream().filter(
                                i -> Objects.equals(i.getBlockNumber(), block.getNumberZag()))
                        .toList()))
                .sorted(BLOCK_BY_NUMBER_ZAG)
                .toList();

        var couplings = items.stream()
                .filter(i -> i.getType() == IssueItemType.COUPLING)
                .map(couplingConverter::entityToCouplingDTO)
                .toList();

        return new IssueDTO()
                .id(entity.getId())
                .region(entity.getRegion().getName())
                .date(entity.getDate())
                .title(entity.getTitle())
                .stamps(stamps)
                .blocks(blocks)
                .couplings(couplings);
    }
}
