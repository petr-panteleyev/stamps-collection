// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend.converter;

import org.panteleyev.stamps.backend.domain.AlbumEntity;
import org.panteleyev.stamps.backend.domain.RegionEntity;
import org.panteleyev.stamps.backend.domain.TagEntity;
import org.panteleyev.stamps.dto.AlbumDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.panteleyev.stamps.backend.converter.ConverterUtils.convert;
import static org.panteleyev.stamps.backend.converter.ConverterUtils.convertBoolean;

@Component
public class AlbumConverter {
    public AlbumConverter() {
    }

    public AlbumEntity dtoToEntity(AlbumDTO dto, AlbumEntity parent, Collection<RegionEntity> regionEntities,
            Collection<TagEntity> tagEntities)
    {
        var dtoTags = convert(dto.getTags());
        var tags = tagEntities.stream().filter(e -> dtoTags.contains(e.getName())).collect(Collectors.toSet());

        var dtoExcludedTags = convert(dto.getExcludedTags());
        var excludedTags = tagEntities.stream().filter(e -> dtoExcludedTags.contains(e.getName())).collect(
                Collectors.toSet());

        var albumEntity = new AlbumEntity()
                .setId(dto.getId())
                .setParent(parent)
                .setParentIndex(0)
                .setName(dto.getName())
                .setRegion(regionEntities.stream()
                        .filter(r -> Objects.equals(r.getName(), dto.getRegion()))
                        .findAny().orElseThrow())
                .setStartYear(dto.getStartYear())
                .setEndYear(dto.getEndYear())
                .setNoTags(convertBoolean(dto.getNoTags()))
                .setTags(tags)
                .setExcludedTags(excludedTags);

        var dtoSubalbums = convert(dto.getSubalbums());
        var subalbums = new ArrayList<AlbumEntity>(dtoSubalbums.size());
        for (int i = 0; i < dtoSubalbums.size(); i++) {
            subalbums.add(
                    dtoToEntity(dtoSubalbums.get(i), albumEntity, regionEntities, tagEntities).setParentIndex(i)
            );
        }
        albumEntity.setSubalbums(subalbums);
        return albumEntity;
    }

    public AlbumDTO entityToDTO(AlbumEntity entity) {
        return new AlbumDTO()
                .id(entity.getId())
                .name(entity.getName())
                .region(entity.getRegion().getName())
                .startYear(entity.getStartYear())
                .endYear(entity.getEndYear())
                .noTags(convertBoolean(entity.getNoTags()))
                .subalbums(entity.getSubalbums().stream().map(this::entityToDTO).toList())
                .tags(entity.getTags().stream().map(TagEntity::getName).toList())
                .excludedTags(entity.getExcludedTags().stream().map(TagEntity::getName).toList());
    }
}
