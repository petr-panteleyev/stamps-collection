// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;
import java.util.UUID;

@Entity(name = "Region")
@Table(name = "region")
public class RegionEntity {
    private UUID id;
    private String name;
    private Integer yearStart;
    private Integer yearEnd;

    public RegionEntity() {
    }

    public RegionEntity(String name, Integer yearStart, Integer yearEnd) {
        this.name = name;
        this.yearStart = yearStart;
        this.yearEnd = yearEnd;
    }

    @Id
    @Column(nullable = false)
    public UUID getId() {
        return id;
    }

    public RegionEntity setId(UUID id) {
        this.id = id;
        return this;
    }

    @Column(nullable = false, unique = true)
    public String getName() {
        return name;
    }

    public RegionEntity setName(String name) {
        this.name = name;
        return this;
    }

    @Column(nullable = false)
    public Integer getYearStart() {
        return yearStart;
    }

    public RegionEntity setYearStart(Integer yearStart) {
        this.yearStart = yearStart;
        return this;
    }

    public Integer getYearEnd() {
        return yearEnd;
    }

    public RegionEntity setYearEnd(Integer yearEnd) {
        this.yearEnd = yearEnd;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RegionEntity that)) return false;
        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(yearStart, that.yearStart)
                && Objects.equals(yearEnd, that.yearEnd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, yearStart, yearEnd);
    }
}
