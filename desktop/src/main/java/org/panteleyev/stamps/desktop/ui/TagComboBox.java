// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui;

import org.controlsfx.control.CheckComboBox;
import org.panteleyev.stamps.dto.TagDTO;

import java.util.Collection;

public class TagComboBox extends CheckComboBox<String> {
    public TagComboBox() {
    }

    public void setTags(Collection<TagDTO> tags) {
        getItems().setAll(tags.stream()
                .map(TagDTO::getName)
                .sorted()
                .toList()
        );
    }
}
