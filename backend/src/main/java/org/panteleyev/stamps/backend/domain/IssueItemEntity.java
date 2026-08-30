// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity(name = "IssueItem")
@Table(name = "issue_item")
public class IssueItemEntity {
    private UUID id;
    private IssueItemType type;
    private Integer year;
    private Integer numberZag;
    private Integer numberCfa;
    private BigDecimal denomination;
    private String description;
    private Integer blockNumber;
    private String couplingItems;
    private Boolean hasClean;
    private Boolean hasCancelled;
    private Boolean replacementRequired;
    private String comment;
    private IssueEntity issue;
    private Set<TagEntity> tags = new HashSet<>();

    @Id
    public UUID getId() {
        return id;
    }

    public IssueItemEntity setId(UUID id) {
        this.id = id;
        return this;
    }

    @Column(name = "issue_type", nullable = false)
    @Enumerated(EnumType.STRING)
    public IssueItemType getType() {
        return type;
    }

    public IssueItemEntity setType(IssueItemType type) {
        this.type = type;
        return this;
    }

    @Column(name = "issue_year", nullable = false)
    public Integer getYear() {
        return year;
    }

    public IssueItemEntity setYear(Integer year) {
        this.year = year;
        return this;
    }

    @Column(nullable = false)
    public Integer getNumberZag() {
        return numberZag;
    }

    @Column(nullable = false)
    public IssueItemEntity setNumberZag(Integer numberZag) {
        this.numberZag = numberZag;
        return this;
    }

    public Integer getNumberCfa() {
        return numberCfa;
    }

    public IssueItemEntity setNumberCfa(Integer numberCfa) {
        this.numberCfa = numberCfa;
        return this;
    }

    @Column(nullable = false)
    public BigDecimal getDenomination() {
        return denomination;
    }

    public IssueItemEntity setDenomination(BigDecimal denomination) {
        this.denomination = denomination;
        return this;
    }

    @Column(nullable = false)
    public String getDescription() {
        return description;
    }

    public IssueItemEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    @Column(name = "block_num")
    public Integer getBlockNumber() {
        return blockNumber;
    }

    public IssueItemEntity setBlockNumber(Integer blockNumber) {
        this.blockNumber = blockNumber;
        return this;
    }

    public String getCouplingItems() {
        return couplingItems;
    }

    public IssueItemEntity setCouplingItems(String couplingItems) {
        this.couplingItems = couplingItems;
        return this;
    }

    @Column(nullable = false)
    public Boolean getHasClean() {
        return hasClean;
    }

    public IssueItemEntity setHasClean(Boolean hasClean) {
        this.hasClean = hasClean;
        return this;
    }

    @Column(nullable = false)
    public Boolean getHasCancelled() {
        return hasCancelled;
    }

    public IssueItemEntity setHasCancelled(Boolean hasCancelled) {
        this.hasCancelled = hasCancelled;
        return this;
    }

    @Column(name = "repl_required", nullable = false)
    public Boolean getReplacementRequired() {
        return replacementRequired;
    }

    public IssueItemEntity setReplacementRequired(Boolean replacementRequired) {
        this.replacementRequired = replacementRequired;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public IssueItemEntity setComment(String comment) {
        this.comment = comment;
        return this;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "issue_id", nullable = false, referencedColumnName = "id")
    public IssueEntity getIssue() {
        return issue;
    }

    public IssueItemEntity setIssue(IssueEntity issue) {
        this.issue = issue;
        return this;
    }


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tag_link",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    public Set<TagEntity> getTags() {
        return tags;
    }

    public IssueItemEntity setTags(Set<TagEntity> tags) {
        this.tags = tags;
        return this;
    }

    // Helper methods

    @Transient
    public boolean isStamp() {
        return type == IssueItemType.STAMP;
    }

    @Transient
    public boolean isBlock() {
        return type == IssueItemType.BLOCK;
    }

    @Transient
    public boolean isCoupling() {
        return type == IssueItemType.COUPLING;
    }
}
