// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;
import java.util.UUID;

@Entity(name = "Tag")
@Table(name = "tag")
public class TagEntity {
    private UUID id;
    private String name;

    public TagEntity() {
    }

    public TagEntity(String name) {
        this.name = name;
    }

    @Id
    public UUID getId() {
        return id;
    }

    public TagEntity setId(UUID id) {
        this.id = id;
        return this;
    }

    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public TagEntity setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TagEntity tagEntity)) return false;
        return Objects.equals(name, tagEntity.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
