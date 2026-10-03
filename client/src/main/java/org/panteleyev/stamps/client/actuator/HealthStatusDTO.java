// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.client.actuator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record HealthStatusDTO(String status) {
    @JsonCreator
    public HealthStatusDTO(@JsonProperty("status") String status) {
        this.status = status;
    }
}
