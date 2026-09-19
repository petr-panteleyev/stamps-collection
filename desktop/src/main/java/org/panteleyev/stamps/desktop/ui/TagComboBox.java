// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui;

import org.controlsfx.control.CheckComboBox;
import org.panteleyev.stamps.desktop.model.CollectionTag;

import java.util.Collection;

public class TagComboBox extends CheckComboBox<String> {
    public TagComboBox() {
    }

    public void setTags(Collection<CollectionTag> tags) {
        getItems().setAll(tags.stream()
                .map(CollectionTag::name)
                .sorted()
                .toList()
        );
    }
}
