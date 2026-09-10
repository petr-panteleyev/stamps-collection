// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.backend.rest;

import org.panteleyev.stamps.backend.openapi.api.IssuesV1ApiDelegate;
import org.panteleyev.stamps.backend.service.IssueService;
import org.panteleyev.stamps.dto.IssueDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class IssuesV1ApiImpl implements IssuesV1ApiDelegate {
    private final IssueService service;

    public IssuesV1ApiImpl(IssueService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<List<IssueDTO>> getIssues(String region, Integer yearStart, Integer yearEnd, String tags, String excludedTags) {
        return ResponseEntity.ok(service.getIssues(region, yearStart, yearEnd, tags, excludedTags));
    }

    @Override
    public ResponseEntity<IssueDTO> postIssue(IssueDTO issueDTO) {
        if (issueDTO.getId() != null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(service.postIssue(issueDTO));
    }

    @Override
    public ResponseEntity<IssueDTO> putIssue(UUID id, IssueDTO issueDTO) {
        if (!Objects.equals(id, issueDTO.getId())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(service.putIssue(issueDTO));
    }

    @Override
    public ResponseEntity<Void> deleteIssue(UUID id) {
        service.deleteIssue(id);
        return ResponseEntity.noContent().build();
    }
}
