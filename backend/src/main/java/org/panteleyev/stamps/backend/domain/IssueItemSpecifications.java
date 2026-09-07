// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend.domain;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;

public final class IssueItemSpecifications {
    public static Specification<IssueItemEntity> hasRegion(String region) {
        return (root, _, cb) -> region == null ? cb.conjunction() :
                cb.equal(root.get("issue").get("region").get("name"), region);
    }

    public static Specification<IssueItemEntity> hasYearBetween(Integer yearStart, Integer yearEnd) {
        return (root, _, cb) -> {
            Predicate predicate = cb.conjunction();
            if (yearStart != null) predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("year"), yearStart));
            if (yearEnd != null) predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("year"), yearEnd));
            return predicate;
        };
    }

    public static Specification<IssueItemEntity> hasTags(Collection<String> tags) {
        return (root, _, cb) -> {
            if (tags == null) return cb.conjunction();
            return tags.isEmpty() ? cb.isEmpty(root.get("tags")) : root.join("tags").get("name").in(tags);
        };
    }

    public static Specification<IssueItemEntity> hasNumberZag(Integer numberZag) {
        return (root, _, cb) -> {
            if (numberZag == null) return cb.conjunction();
            return cb.equal(root.get("numberZag"), numberZag);
        };
    }

    public static Specification<IssueItemEntity> hasIssue(IssueEntity issue) {
        return (root, _, cb) -> {
            if (issue == null) return cb.conjunction();
            return cb.equal(root.get("issue").get("id"), issue.getId());
        };
    }

    private IssueItemSpecifications() {
    }
}
