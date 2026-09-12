// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.image.Image;
import org.panteleyev.stamps.client.StampsClient;
import org.panteleyev.stamps.dto.AlbumDTO;
import org.panteleyev.stamps.dto.BlockDTO;
import org.panteleyev.stamps.dto.CouplingDTO;
import org.panteleyev.stamps.dto.ImageUploadDTO;
import org.panteleyev.stamps.dto.IssueDTO;
import org.panteleyev.stamps.dto.ItemPatchDTO;
import org.panteleyev.stamps.dto.RegionDTO;
import org.panteleyev.stamps.dto.StampDTO;
import org.panteleyev.stamps.dto.TagDTO;

import java.io.ByteArrayInputStream;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.panteleyev.stamps.desktop.util.DtoUtils.normalize;

public class StampsService {
    private final Lock lock = new ReentrantLock();

    private StampsClient client = null;

    private final BooleanProperty connectedProperty = new SimpleBooleanProperty(false);

    private final Map<UUID, Image> imageCache = new ConcurrentHashMap<>();

    public void init(String serverUrl) {
        lock.lock();
        try {
            this.client = new StampsClient.Builder()
                    .withServerUrl(serverUrl)
                    .withConnectTimeout(Duration.ofSeconds(1))
                    .build();
            connectedProperty.set(true);
        } finally {
            lock.unlock();
        }
    }

    public ReadOnlyBooleanProperty connectedProperty() {
        return connectedProperty;
    }

    public List<RegionDTO> loadRegions() {
        lock.lock();
        try {
            return client.getRegions().right().orElseThrow();
        } finally {
            lock.unlock();
        }
    }

    public List<TagDTO> loadTags() {
        lock.lock();
        try {
            return client.getTags().right().orElseThrow();
        } finally {
            lock.unlock();
        }
    }

    public TagDTO createTag(TagDTO tag) {
        lock.lock();
        try {
            return client.postTag(tag).right().orElseThrow();
        } finally {
            lock.unlock();
        }
    }

    public TagDTO updateTag(TagDTO tag) {
        lock.lock();
        try {
            return client.putTag(tag).right().orElseThrow();
        } finally {
            lock.unlock();
        }
    }

    public List<IssueDTO> loadIssues(AlbumDTO album) {
        lock.lock();

        try {
            var tags = "";
            if (!normalize(album.getNoTags())) {
                if (album.getTags() == null || album.getTags().isEmpty()) {
                    tags = null;
                } else {
                    tags = String.join(",", album.getTags());
                }
            }
            var excludedTags = String.join(",", normalize(album.getExcludedTags()));

            return client.getIssues(album.getRegion(), album.getStartYear(), album.getEndYear(), tags, excludedTags)
                    .right().orElseThrow();
        } finally {
            lock.unlock();
        }
    }

    public IssueDTO createIssue(IssueDTO issue) {
        lock.lock();
        try {
            return client.postIssue(issue).right().orElseThrow();
        } finally {
            lock.unlock();
        }
    }

    public IssueDTO updateIssue(IssueDTO issue) {
        lock.lock();
        try {
            return client.putIssue(issue).right().orElseThrow();
        } finally {
            lock.unlock();
        }
    }

    public void deleteIssue(IssueDTO issue) {
        lock.lock();
        try {
            var result = client.deleteIssue(issue.getId());
            if (result.isLeft()) throw new RuntimeException();
        } finally {
            lock.unlock();
        }
    }

    public Object patchItem(Object item, ItemPatchDTO patch) {
        lock.lock();
        try {
            var result = switch (item) {
                case StampDTO stamp -> client.patchStamp(stamp.getId(), patch);
                case CouplingDTO coupling -> client.patchCoupling(coupling.getId(), patch);
                case BlockDTO block -> client.patchBlock(block.getId(), patch);
                default -> throw new IllegalArgumentException("Invalid class " + item.getClass().getSimpleName());
            };
            return result.right().orElseThrow();
        } finally {
            lock.unlock();
        }
    }

    public Image getImage(UUID uuid) {
        lock.lock();
        try {
            return imageCache.computeIfAbsent(uuid, key -> {
                var bytes = client.getImage(key).right().orElse(null);
                return bytes == null ? null : new Image(new ByteArrayInputStream(bytes));
            });
        } finally {
            lock.unlock();
        }
    }

    public void uploadImage(UUID id, ImageUploadDTO dto) {
        lock.lock();
        try {
            var result = client.uploadImage(id, dto).right().orElseThrow();
            imageCache.remove(result.getId());
        } finally {
            lock.unlock();
        }
    }

    public List<AlbumDTO> loadAlbums() {
        lock.lock();
        try {
            return client.getAlbums().onLeft(error -> {
                throw new RuntimeException(error.toString());
            }).right().orElseThrow();
        } finally {
            lock.unlock();
        }
    }

    public AlbumDTO createAlbum(AlbumDTO album) {
        lock.lock();
        try {
            return client.postAlbum(album).onLeft(error -> {
                throw new RuntimeException(error.toString());
            }).right().orElseThrow();
        } finally {
            lock.unlock();
        }
    }

    public AlbumDTO updateAlbum(AlbumDTO album) {
        lock.lock();
        try {
            return client.putAlbum(album).onLeft(error -> {
                throw new RuntimeException(error.toString());
            }).right().orElseThrow();
        } finally {
            lock.unlock();
        }
    }
}
