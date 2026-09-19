// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui.tags;

import org.panteleyev.stamps.desktop.model.CollectionTag;
import org.panteleyev.stamps.dto.AlbumDTO;

import java.util.Collection;

public record TagEditorResult(Collection<CollectionTag> toAdd, Collection<CollectionTag> toUpdate, Collection<CollectionTag> toDelete) {
}
