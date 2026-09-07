// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend.repository;

import org.jspecify.annotations.NullMarked;
import org.panteleyev.stamps.backend.domain.IssueItemEntity;
import org.panteleyev.stamps.dto.ItemPatchDTO;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface IssueItemRepository extends JpaRepository<IssueItemEntity, UUID>, JpaSpecificationExecutor<IssueItemEntity> {
    @Override
    @NullMarked
    @EntityGraph(attributePaths = {"issue", "tags", "issue.region"})
    List<IssueItemEntity> findAll(Specification<IssueItemEntity> spec);

    @Query(value = """
            UPDATE issue_item SET
                has_clean = COALESCE(:#{#patch.hasClean}, has_clean),
                has_cancelled = COALESCE(:#{#patch.hasCancelled}, has_cancelled),
                repl_required = COALESCE(:#{#patch.replacementRequired}, repl_required)
            WHERE id = :id
            """,
            nativeQuery = true
    )
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    int patchItem(@Param("id") UUID id, @Param("patch") ItemPatchDTO patch);
}
