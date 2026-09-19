// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.converter;

import org.panteleyev.stamps.desktop.model.CollectionTag;
import org.panteleyev.stamps.dto.TagDTO;

public final class TagConverter {
    public static CollectionTag dtoToModel(TagDTO dto) {
        return new CollectionTag(dto.getId(), dto.getName());
    }

    public static TagDTO modelToDto(CollectionTag model) {
        return new TagDTO()
                .id(model.id())
                .name(model.name());
    }

    private TagConverter() {
    }
}
