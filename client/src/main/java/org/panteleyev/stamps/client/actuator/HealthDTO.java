// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.client.actuator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record HealthDTO(List<String> groups, String status) {
    @JsonCreator
    public HealthDTO(@JsonProperty("groups") List<String> groups, @JsonProperty("status") String status) {
        this.groups = groups;
        this.status = status;
    }
}
