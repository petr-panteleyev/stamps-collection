// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.client;

import org.panteleyev.functional.Either;
import org.panteleyev.stamps.client.openapi.api.BlocksV1Api;
import org.panteleyev.stamps.client.openapi.api.CouplingsV1Api;
import org.panteleyev.stamps.client.openapi.api.ImagesV1Api;
import org.panteleyev.stamps.client.openapi.api.IssuesV1Api;
import org.panteleyev.stamps.client.openapi.api.RegionsV1Api;
import org.panteleyev.stamps.client.openapi.api.StampsV1Api;
import org.panteleyev.stamps.client.openapi.api.TagsV1Api;
import org.panteleyev.stamps.client.openapi.invoker.ApiException;
import org.panteleyev.stamps.client.openapi.invoker.Configuration;
import org.panteleyev.stamps.dto.IssueDTO;
import org.panteleyev.stamps.dto.RegionDTO;
import org.panteleyev.stamps.dto.TagDTO;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

public class StampsClient {
    @FunctionalInterface
    private interface ApiCaller<R> {
        R call() throws ApiException;
    }

    private static final String CONTEXT_ROOT = "/stamps";

    private final ImagesV1Api imagesV1Api;
    private final RegionsV1Api regionsV1Api;
    private final TagsV1Api tagsV1Api;
    private final IssuesV1Api issuesV1Api;
    private final StampsV1Api stampsV1Api;
    private final BlocksV1Api blocksV1Api;
    private final CouplingsV1Api couplingsV1Api;

    /**
     * Client builder.
     */
    public static class Builder {
        private String serverUrl;
        private Duration timeout = Duration.ofMillis(1000);

        /**
         * Defines server URL.
         *
         * @param serverUrl server URL
         * @return this
         */
        public Builder withServerUrl(String serverUrl) {
            this.serverUrl = serverUrl;
            return this;
        }

        /**
         * Defines HTTP connect timeout.
         *
         * @param timeout timeout
         * @return this
         */
        public Builder withConnectTimeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public StampsClient build() {
            Objects.requireNonNull(serverUrl, "Server URL cannot be null");
            return new StampsClient(serverUrl, timeout);
        }
    }

    private StampsClient(String serverUrl, Duration connectTimeout) {
        var defaultClient = Configuration.getDefaultApiClient();
        defaultClient.updateBaseUri(serverUrl);
        defaultClient.setBasePath(CONTEXT_ROOT);
        defaultClient.setConnectTimeout(connectTimeout);

        imagesV1Api = new ImagesV1Api(defaultClient);
        regionsV1Api = new RegionsV1Api(defaultClient);
        tagsV1Api = new TagsV1Api(defaultClient);
        issuesV1Api = new IssuesV1Api(defaultClient);
        stampsV1Api = new StampsV1Api(defaultClient);
        blocksV1Api = new BlocksV1Api(defaultClient);
        couplingsV1Api = new CouplingsV1Api(defaultClient);
    }

    /* Regions */

    public Either<ClientError, List<RegionDTO>> getRegions() {
        return call(regionsV1Api::getRegions);
    }

    /* Tags */

    public Either<ClientError, List<TagDTO>> getTags() {
        return call(tagsV1Api::getTags);
    }

    /* Issues */

    public Either<ClientError, List<IssueDTO>> getIssues(String region, int yearStart, int yearEnd) {
        return call(() -> issuesV1Api.getIssues(region, yearStart, yearEnd, null));
    }

    //

    private static <R> Either<ClientError, R> call(ApiCaller<R> caller) {
        try {
            return Either.right(caller.call());
        } catch (ApiException ex) {
            return Either.left(new ClientError(ex.getCode(), ex.getMessage()));
        }
    }

    static void main() {
        var serverUrl = "http://localhost:1705";
        var timeOut = Duration.ofSeconds(10);

        var client = new StampsClient(serverUrl, timeOut);

        System.out.println(client.getRegions().right().orElseThrow());
        System.out.println(client.getTags().right().orElseThrow());
        System.out.println(client.getIssues("СССР", 1918, 1991).right().orElseThrow());
    }
}
