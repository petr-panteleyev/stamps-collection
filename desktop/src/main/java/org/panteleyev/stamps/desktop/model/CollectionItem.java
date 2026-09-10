// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.model;

import org.panteleyev.stamps.dto.BlockDTO;
import org.panteleyev.stamps.dto.CouplingDTO;
import org.panteleyev.stamps.dto.IssueDTO;
import org.panteleyev.stamps.dto.IssueItemDTO;
import org.panteleyev.stamps.dto.StampDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;
import static org.panteleyev.stamps.desktop.util.DtoUtils.getCouplingDescription;
import static org.panteleyev.stamps.desktop.util.DtoUtils.normalize;
import static org.panteleyev.stamps.desktop.util.StringUtil.DATE_FORMATTER;

public class CollectionItem {
    private static final int YEAR = LocalDate.now().getYear();

    public record YearRangePredicate(int startYear, Integer endYear) implements Predicate<CollectionItem> {
        public YearRangePredicate {
            endYear = endYear == null ? YEAR : endYear;
        }

        @Override
        public boolean test(CollectionItem collectionItem) {
            var year = collectionItem.getYear();
            if (year == 0) year = YEAR;
            return year >= startYear && year <= endYear;
        }
    }

    private final Object data;
    private final IssueDTO parent;

    private final UUID id;
    private final int year;
    private final BigDecimal denomination;
    private final int numberZag;
    private final int numberCfa;
    private final String description;
    private final String comment;
    // Fields updatable directly from the table view
    private boolean hasClean = false;
    private boolean hasCancelled = false;
    private boolean replacementRequired = false;

    public CollectionItem(Object data, IssueDTO parent) {
        this.data = requireNonNull(data);
        this.parent = requireNonNull(parent);

        id = switch (data) {
            case IssueDTO issue -> issue.getId();
            case IssueItemDTO item -> item.getId();
            default -> throw new IllegalArgumentException("Wrong item data class");
        };

        year = parent.getDate().getYear();

        denomination = data instanceof StampDTO stamp ? stamp.getDenomination() : null;

        numberZag = switch (data) {
            case StampDTO stamp -> normalize(stamp.getNumberZag());
            case BlockDTO block -> normalize(block.getNumberZag());
            default -> 0;
        };

        numberCfa = switch (data) {
            case StampDTO stamp -> normalize(stamp.getNumberCfa());
            case BlockDTO block -> normalize(block.getNumberCfa());
            default -> 0;
        };

        description = switch (data) {
            case IssueDTO issue -> DATE_FORMATTER.format(issue.getDate()) + " - " + issue.getTitle();
            case StampDTO stamp -> stamp.getDescription();
            case BlockDTO block -> block.getDescription();
            case CouplingDTO coupling -> getCouplingDescription(coupling);
            default -> "";
        };

        comment = data instanceof IssueItemDTO item ? item.getComment() : "";

        switch (data) {
            case IssueItemDTO item -> {
                hasClean = normalize(item.getHasClean());
                hasCancelled = normalize(item.getHasCancelled());
                replacementRequired = normalize(item.getReplacementRequired());
            }
            case IssueDTO issue -> {
                hasClean = issue.getStamps().stream().anyMatch(b -> normalize(b.getHasClean()))
                        || issue.getBlocks().stream().anyMatch(b -> normalize(b.getHasClean()))
                        || issue.getCouplings().stream().anyMatch(c -> normalize(c.getHasClean()));

                hasCancelled = issue.getStamps().stream().anyMatch(b -> normalize(b.getHasCancelled()))
                        || issue.getBlocks().stream().anyMatch(b -> normalize(b.getHasCancelled()))
                        || issue.getCouplings().stream().anyMatch(c -> normalize(c.getHasCancelled()));

                replacementRequired = issue.getStamps().stream().anyMatch(b -> normalize(b.getReplacementRequired()))
                        || issue.getBlocks().stream().anyMatch(b -> normalize(b.getReplacementRequired()))
                        || issue.getCouplings().stream().anyMatch(c -> normalize(c.getReplacementRequired()));
            }
            default -> {
            }
        }
    }

    public UUID getId() {
        return id;
    }

    public int getYear() {
        return year;
    }

    public BigDecimal getDenomination() {
        return denomination;
    }

    public int getNumberZag() {
        return numberZag;
    }

    public int getNumberCfa() {
        return numberCfa;
    }

    public String getDescription() {
        return description;
    }

    public String getComment() {
        return comment;
    }

    public boolean isIssue() {
        return data instanceof IssueDTO;
    }

    public boolean isStamp() {
        return data instanceof StampDTO;
    }

    public boolean isBlock() {
        return data instanceof BlockDTO;
    }

    public boolean isCoupling() {
        return data instanceof CouplingDTO;
    }

    public boolean getHasClean() {
        return hasClean;
    }

    public void setHasClean(boolean hasClean) {
        this.hasClean = hasClean;
    }

    public boolean getHasCancelled() {
        return hasCancelled;
    }

    public void setHasCancelled(boolean hasCancelled) {
        this.hasCancelled = hasCancelled;
    }

    public boolean getReplacementRequired() {
        return replacementRequired;
    }

    public void setReplacementRequired(boolean replacementRequired) {
        this.replacementRequired = replacementRequired;
    }

    public Object getData() {
        return data;
    }

    public IssueDTO getParent() {
        return parent;
    }
}
