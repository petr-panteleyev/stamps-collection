// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend.service;

import org.panteleyev.stamps.backend.converter.IssueConverter;
import org.panteleyev.stamps.backend.domain.IssueItemEntity;
import org.panteleyev.stamps.backend.exception.RegionNotFoundException;
import org.panteleyev.stamps.backend.repository.IssueItemRepository;
import org.panteleyev.stamps.backend.repository.IssueRepository;
import org.panteleyev.stamps.backend.repository.RegionRepository;
import org.panteleyev.stamps.backend.repository.TagRepository;
import org.panteleyev.stamps.dto.IssueDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class IssueService {
    private final IssueRepository repository;
    private final IssueItemRepository issueItemRepository;
    private final RegionRepository regionRepository;
    private final TagRepository tagRepository;
    private final IssueConverter converter;

    public IssueService(IssueRepository repository, IssueItemRepository issueItemRepository,
            RegionRepository regionRepository, TagRepository tagRepository, IssueConverter converter)
    {
        this.repository = repository;
        this.issueItemRepository = issueItemRepository;
        this.regionRepository = regionRepository;
        this.tagRepository = tagRepository;
        this.converter = converter;
    }

    @Transactional(readOnly = true)
    public List<IssueDTO> getIssues(String region, Integer yearStart, Integer yearEnd, String tags) {
        List<IssueItemEntity> issueItems;

        if (tags != null && !tags.isBlank()) {
            var setOfTags = Arrays.stream(tags.split(","))
                    .map(String::trim)
                    .collect(Collectors.toSet());
            issueItems = issueItemRepository.findByRegionAndYearBetweenWithTags(region, yearStart, yearEnd, setOfTags)
                    .toList();
        } else {
            issueItems = issueItemRepository.findByRegionAndYearBetween(region, yearStart, yearEnd).toList();
        }

        var grouped = issueItems.stream()
                .collect(Collectors.groupingBy(IssueItemEntity::getIssue));

        return grouped.entrySet().stream()
                .map(e -> converter.entityToDTO(e.getKey(), e.getValue()))
                .sorted(Comparator.comparing(IssueDTO::getDate))
                .toList();
    }

    @Transactional
    public void postIssue(IssueDTO dto) {
        dto.setId(UUID.randomUUID());
        for (var stamp : dto.getStamps()) {
            stamp.setId(UUID.randomUUID());
        }
        for (var block : dto.getBlocks()) {
            block.setId(UUID.randomUUID());
            for (var stamp : block.getStamps()) {
                stamp.setId(UUID.randomUUID());
            }
        }
        for (var coupling : dto.getCouplings()) {
            coupling.setId(UUID.randomUUID());
        }
        putIssue(dto);
    }

    @Transactional
    public IssueDTO putIssue(IssueDTO dto) {
        var region = regionRepository.findByName(dto.getRegion())
                .orElseThrow(() -> new RegionNotFoundException(dto.getRegion()));

        var tagNames = new HashSet<String>();
        dto.getStamps().stream()
                .flatMap(s -> s.getTags().stream())
                .forEach(tagNames::add);
        dto.getBlocks().stream()
                .flatMap(b -> b.getTags().stream())
                .forEach(tagNames::add);

        var tagEntities = tagRepository.findByNameIn(tagNames);

        var entity = converter.issueDtoToEntity(dto, region, tagEntities);
        repository.save(entity);
        return dto;
    }
}
