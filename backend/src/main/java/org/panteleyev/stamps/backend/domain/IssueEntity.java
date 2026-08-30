// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity(name = "Issue")
@Table(name = "issue")
public class IssueEntity {
    private UUID id;
    private RegionEntity region;
    private Integer year;
    private LocalDate date;
    private String title;
    private List<IssueItemEntity> items = new ArrayList<>();

    public IssueEntity() {
    }

    @Id
    public UUID getId() {
        return id;
    }

    public IssueEntity setId(UUID id) {
        this.id = id;
        return this;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "region_id", referencedColumnName = "id")
    public RegionEntity getRegion() {
        return region;
    }

    public IssueEntity setRegion(RegionEntity region) {
        this.region = region;
        return this;
    }

    @Column(name = "issue_year", nullable = false)
    public Integer getYear() {
        return year;
    }

    public IssueEntity setYear(Integer year) {
        this.year = year;
        return this;
    }

    @Column(name = "issue_date")
    public LocalDate getDate() {
        return date;
    }

    public IssueEntity setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    @Column(nullable = false)
    public String getTitle() {
        return title;
    }

    public IssueEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    @OneToMany(mappedBy = "issue", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public List<IssueItemEntity> getItems() {
        return items;
    }

    public IssueEntity setItems(List<IssueItemEntity> items) {
        this.items = items == null ? new ArrayList<>() : items;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof IssueEntity that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
