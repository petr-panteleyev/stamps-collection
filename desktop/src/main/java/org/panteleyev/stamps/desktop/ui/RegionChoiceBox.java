// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui;

import javafx.scene.control.ChoiceBox;
import org.panteleyev.stamps.desktop.model.CollectionRegion;
import org.panteleyev.stamps.dto.RegionDTO;

import java.util.Collection;

public class RegionChoiceBox extends ChoiceBox<String> {
    public RegionChoiceBox() {
    }

    public void setRegions(Collection<CollectionRegion> regions) {
        getItems().setAll(
                regions.stream()
                        .map(CollectionRegion::name)
                        .sorted()
                        .toList()
        );
    }

}
