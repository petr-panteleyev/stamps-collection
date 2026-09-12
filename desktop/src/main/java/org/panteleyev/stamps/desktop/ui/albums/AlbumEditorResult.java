// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui.albums;

import org.panteleyev.stamps.dto.AlbumDTO;

import java.util.Collection;
import java.util.List;

public record AlbumEditorResult(Collection<AlbumDTO> toAdd, Collection<AlbumDTO> toUpdate, Collection<AlbumDTO> toDelete) {
}
