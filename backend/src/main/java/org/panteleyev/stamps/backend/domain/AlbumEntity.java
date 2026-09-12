// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity(name = "Album")
@Table(name = "album")
public class AlbumEntity {
    private UUID id;
    private AlbumEntity parent;
    private Integer parentIndex;
    private String name;
    private RegionEntity region;
    private List<AlbumEntity> subalbums;
    private Integer startYear;
    private Integer endYear;
    private Boolean noTags;
    private Set<TagEntity> tags = new HashSet<>();
    private Set<TagEntity> excludedTags = new HashSet<>();

    public AlbumEntity() {
    }

    @Id
    @Column(nullable = false)
    public UUID getId() {
        return id;
    }

    public AlbumEntity setId(UUID id) {
        this.id = id;
        return this;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id", referencedColumnName = "id", nullable = true)
    public AlbumEntity getParent() {
        return parent;
    }

    public AlbumEntity setParent(AlbumEntity parent) {
        this.parent = parent;
        return this;
    }

    @Column(nullable = false)
    public Integer getParentIndex() {
        return parentIndex;
    }

    public AlbumEntity setParentIndex(Integer parentIndex) {
        this.parentIndex = parentIndex;
        return this;
    }

    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public AlbumEntity setName(String name) {
        this.name = name;
        return this;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "region_id", referencedColumnName = "id", nullable = false)
    public RegionEntity getRegion() {
        return region;
    }

    public AlbumEntity setRegion(RegionEntity region) {
        this.region = region;
        return this;
    }

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public List<AlbumEntity> getSubalbums() {
        return subalbums;
    }

    public AlbumEntity setSubalbums(List<AlbumEntity> subalbums) {
        this.subalbums = subalbums == null ? new ArrayList<>() : subalbums;
        return this;
    }

    public Integer getStartYear() {
        return startYear;
    }

    public AlbumEntity setStartYear(Integer startYear) {
        this.startYear = startYear;
        return this;
    }

    public Integer getEndYear() {
        return endYear;
    }

    public AlbumEntity setEndYear(Integer endYear) {
        this.endYear = endYear;
        return this;
    }

    public Boolean getNoTags() {
        return noTags;
    }

    public AlbumEntity setNoTags(Boolean noTags) {
        this.noTags = noTags;
        return this;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "album_tag_link",
            joinColumns = @JoinColumn(name = "album_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    public Set<TagEntity> getTags() {
        return tags;
    }

    public AlbumEntity setTags(Set<TagEntity> tags) {
        this.tags = tags;
        return this;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "album_excluded_tag_link",
            joinColumns = @JoinColumn(name = "album_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    public Set<TagEntity> getExcludedTags() {
        return excludedTags;
    }

    public AlbumEntity setExcludedTags(Set<TagEntity> excludedTags) {
        this.excludedTags = excludedTags;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AlbumEntity that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
