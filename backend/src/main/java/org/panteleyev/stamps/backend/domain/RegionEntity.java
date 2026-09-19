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
    private Integer startYear;
    private Integer endYear;

    public RegionEntity() {
    }

    public RegionEntity(String name, Integer startYear, Integer endYear) {
        this.name = name;
        this.startYear = startYear;
        this.endYear = endYear;
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
    public Integer getStartYear() {
        return startYear;
    }

    public RegionEntity setStartYear(Integer startYear) {
        this.startYear = startYear;
        return this;
    }

    public Integer getEndYear() {
        return endYear;
    }

    public RegionEntity setEndYear(Integer endYear) {
        this.endYear = endYear;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RegionEntity that)) return false;
        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(startYear, that.startYear)
                && Objects.equals(endYear, that.endYear);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, startYear, endYear);
    }
}
