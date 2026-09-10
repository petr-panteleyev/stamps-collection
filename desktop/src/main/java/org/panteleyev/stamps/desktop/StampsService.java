// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.image.Image;
import org.panteleyev.stamps.client.StampsClient;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.panteleyev.stamps.desktop.util.DtoUtils.ISSUE_COMPARATOR_BY_DATE;

public class StampsService {
    private final Lock lock = new ReentrantLock();

    private StampsClient client = null;

    private final List<RegionDTO> regions = new CopyOnWriteArrayList<>();
    private final List<TagDTO> tags = new CopyOnWriteArrayList<>();
    private final Map<UUID, IssueDTO> issues = new HashMap<>();

    private final BooleanProperty connectedProperty = new SimpleBooleanProperty(false);

    private final Map<UUID, Image> imageCache = new ConcurrentHashMap<>();

    public void init(String serverUrl) {
        lock.lock();
        try {
            var client = new StampsClient.Builder()
                    .withServerUrl(serverUrl)
                    .withConnectTimeout(Duration.ofSeconds(1))
                    .build();

            regions.clear();
            client.getRegions().onRight(regions::addAll).onLeft(error -> {
                throw new RuntimeException(error.toString());
            });

            tags.clear();
            client.getTags().onRight(tags::addAll).onLeft(error -> {
                throw new RuntimeException(error.toString());
            });

            issues.clear();
            client.getIssues()
                    .onRight(list -> list.forEach(item -> issues.put(item.getId(), item)))
                    .onLeft(error -> {
                        throw new RuntimeException(error.toString());
                    });
            this.client = client;
            connectedProperty.set(true);
        } finally {
            lock.unlock();
        }
    }

    public ReadOnlyBooleanProperty connectedProperty() {
        return connectedProperty;
    }

    public List<TagDTO> getTags() {
        return tags;
    }

    public List<RegionDTO> getRegions() {
        return regions;
    }

    public List<IssueDTO> getIssues(String region) {
        lock.lock();
        try {
            return issues.values().stream()
                    .filter(i -> Objects.equals(i.getRegion(), region))
                    .sorted(ISSUE_COMPARATOR_BY_DATE)
                    .toList();
        } finally {
            lock.unlock();
        }
    }

    public IssueDTO createIssue(IssueDTO issue) {
        lock.lock();
        try {
            var created = client.postIssue(issue).right().orElseThrow();
            issues.put(created.getId(), created);
            return created;
        } finally {
            lock.unlock();
        }
    }

    public IssueDTO updateIssue(IssueDTO issue) {
        lock.lock();
        try {
            var updated = client.putIssue(issue).right().orElseThrow();
            issues.put(updated.getId(), updated);
            return updated;
        } finally {
            lock.unlock();
        }
    }

    public void deleteIssue(IssueDTO issue) {
        lock.lock();
        try {
            var result = client.deleteIssue(issue.getId());
            if (result.isLeft()) throw new RuntimeException();
            issues.remove(issue.getId());
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
}
