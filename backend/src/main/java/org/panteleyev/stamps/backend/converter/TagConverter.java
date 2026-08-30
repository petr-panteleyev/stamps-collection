// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend.converter;

import org.panteleyev.stamps.backend.domain.TagEntity;
import org.panteleyev.stamps.dto.TagDTO;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TagConverter {

    public TagEntity tagDTOToEntity(TagDTO dto) {
        return new TagEntity()
                .setId(dto.getId())
                .setName(dto.getName());
    }

    public TagDTO entityToTagDTO(TagEntity entity) {
        return new TagDTO()
                .id(entity.getId())
                .name(entity.getName());
    }
}
