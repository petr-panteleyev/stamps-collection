// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend.repository;

import org.panteleyev.stamps.backend.domain.IssueItemEntity;
import org.panteleyev.stamps.dto.ItemPatchDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

public interface IssueItemRepository extends JpaRepository<IssueItemEntity, UUID> {
    @Query("""
                SELECT DISTINCT i FROM IssueItem i
                WHERE i.issue.region.name = :region
                    AND i.year BETWEEN :yearStart AND :yearEnd
            """)
    Stream<IssueItemEntity> findByRegionAndYearBetween(String region, int yearStart, int yearEnd);

    @Query("""
                SELECT DISTINCT i FROM IssueItem i
                    LEFT JOIN FETCH i.tags t
                WHERE i.issue.region.name = :region
                    AND i.year BETWEEN :yearStart AND :yearEnd
                    AND t.name IN :tagNames
            """)
    Stream<IssueItemEntity> findByRegionAndYearBetweenWithTags(String region, int yearStart, int yearEnd,
            Set<String> tagNames);

    @Query("""
            SELECT i FROM IssueItem i
                JOIN i.issue iss
            WHERE i.numberZag = :numberZag
                AND iss.region.name = :region
            """)
    Stream<IssueItemEntity> findByRegionAndNumberZag(String region, Integer numberZag);

    @Query("SELECT i FROM IssueItem i JOIN i.issue iss WHERE iss.id = :id")
    Stream<IssueItemEntity> findByIssueId(UUID id);

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
