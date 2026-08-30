// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend.repository;

import org.panteleyev.stamps.backend.domain.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface TagRepository extends JpaRepository<TagEntity, UUID> {
    Set<TagEntity> findByNameIn(Set<String> names);

    Optional<TagEntity> findByName(String name);
}
