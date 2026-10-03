// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.panteleyev.stamps.client.actuator.HealthDTO;
import org.panteleyev.stamps.client.actuator.HealthStatusDTO;
import org.panteleyev.stamps.client.openapi.invoker.ApiClient;
import org.panteleyev.stamps.client.openapi.invoker.ApiException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

class ServerManagementApi {
    private static final String ACTUATOR_ENDPOINT = "/actuator";
    private static final String SHUTDOWN_ENDPONT = ACTUATOR_ENDPOINT + "/shutdown";
    private static final String HEALTH_ENDPONT = ACTUATOR_ENDPOINT + "/health";
    private static final String READINESS_ENDPONT = HEALTH_ENDPONT + "/readiness";

    private final ApiClient apiClient;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    public ServerManagementApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public HealthDTO health() throws ApiException {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(apiClient.getBaseUri() + HEALTH_ENDPONT))
                .GET()
                .build();

        try {
            var response = apiClient.getHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return OBJECT_MAPPER.readValue(response.body(), HealthDTO.class);
            } else {
                throw new ApiException(response.statusCode(), "Health call failed with " + response.statusCode());
            }
        } catch (IOException | InterruptedException ex) {
            throw new ApiException(0, ex.getMessage());
        }
    }

    public HealthStatusDTO readiness() throws ApiException {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(apiClient.getBaseUri() + READINESS_ENDPONT))
                .GET()
                .build();

        try {
            var body = apiClient.getHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body();
            return OBJECT_MAPPER.readValue(body, HealthStatusDTO.class);
        } catch (Exception ex) {
            throw new ApiException(ex);
        }
    }

    public void shutdown() throws ApiException {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(apiClient.getBaseUri() + SHUTDOWN_ENDPONT))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        try {
            var response = apiClient.getHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new ApiException(response.statusCode(), "Shutdown call failed with " + response.statusCode());
            }
        } catch (IOException | InterruptedException ex) {
            throw new ApiException(0, ex.getMessage());
        }
    }
}
