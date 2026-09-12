// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.util;

import org.panteleyev.stamps.dto.AlbumDTO;
import org.panteleyev.stamps.dto.BlockDTO;
import org.panteleyev.stamps.dto.CouplingDTO;
import org.panteleyev.stamps.dto.IssueDTO;
import org.panteleyev.stamps.dto.RegionDTO;
import org.panteleyev.stamps.dto.StampDTO;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public final class DtoUtils {
    public static final Comparator<RegionDTO> REGION_COMPATAOR_BY_NAME = Comparator.comparing(RegionDTO::getName);

    public static final Comparator<IssueDTO> ISSUE_COMPARATOR_BY_DATE = Comparator.comparing(IssueDTO::getDate);

    public static final Comparator<StampDTO> STAMP_COMPARATOR_BY_NUMBER_ZAG = Comparator.comparingInt(
            StampDTO::getNumberZag);

    public static final Comparator<AlbumDTO> ALBUM_COMPARATOR_BY_NAME = Comparator.comparing(AlbumDTO::getName);

    public static boolean negate(Boolean value) {
        return value == null || !value;
    }

    public static int normalize(Integer value) {
        return value == null ? 0 : value;
    }

    public static boolean normalize(Boolean value) {
        return value != null && value;
    }

    public static <T> List<T> normalize(List<T> list) {
        return list == null ? List.of() : list;
    }

    /// Makes a copy of [StampDTO].
    ///
    /// @param stamp stamp to copy
    /// @return copy of the stamp
    public static StampDTO copy(StampDTO stamp) {
        if (stamp == null) return null;
        return new StampDTO()
                .id(stamp.getId())
                .year(stamp.getYear())
                .numberZag(stamp.getNumberZag())
                .numberCfa(stamp.getNumberCfa())
                .denomination(stamp.getDenomination())
                .description(stamp.getDescription())
                .hasClean(stamp.getHasClean())
                .hasCancelled(stamp.getHasCancelled())
                .replacementRequired(stamp.getReplacementRequired())
                .blockNumber(stamp.getBlockNumber())
                .comment(stamp.getComment())
                .tags(new ArrayList<>(stamp.getTags()));
    }

    /// Makes a copy of [BlockDTO].
    ///
    /// @param block block to copy
    /// @return copy of the block
    public static BlockDTO copy(BlockDTO block) {
        if (block == null) return null;
        return new BlockDTO()
                .id(block.getId())
                .year(block.getYear())
                .numberZag(block.getNumberZag())
                .numberCfa(block.getNumberCfa())
                .description(block.getDescription())
                .hasClean(block.getHasClean())
                .hasCancelled(block.getHasCancelled())
                .replacementRequired(block.getReplacementRequired())
                .comment(block.getComment())
                .stamps(new ArrayList<>(block.getStamps().stream().map(DtoUtils::copy).toList()))
                .tags(new ArrayList<>(block.getTags()));
    }

    /// Makes a copy of [CouplingDTO].
    ///
    /// @param coupling coupling to copy
    /// @return copy of the coupling
    public static CouplingDTO copy(CouplingDTO coupling) {
        if (coupling == null) return null;
        return new CouplingDTO()
                .id(coupling.getId())
                .hasClean(coupling.getHasClean())
                .hasCancelled(coupling.getHasCancelled())
                .replacementRequired(coupling.getReplacementRequired())
                .stampNumbers(new ArrayList<>(coupling.getStampNumbers()))
                .comment(coupling.getComment());
    }

    /// Makes a copy of [IssueDTO].
    ///
    /// @param issue issue to copy
    /// @return copy of the issue
    public static IssueDTO copy(IssueDTO issue) {
        if (issue == null) return null;
        return new IssueDTO()
                .id(issue.getId())
                .region(issue.getRegion())
                .date(issue.getDate())
                .title(issue.getTitle())
                .stamps(new ArrayList<>(issue.getStamps().stream().map(DtoUtils::copy).toList()))
                .blocks(new ArrayList<>(issue.getBlocks().stream().map(DtoUtils::copy).toList()))
                .couplings(new ArrayList<>(issue.getCouplings().stream().map(DtoUtils::copy).toList()));
    }

    public static String getCouplingDescription(CouplingDTO coupling) {
        return "Сцепка марок " + coupling.getStampNumbers().stream()
                .map(n -> Integer.toString(n))
                .collect(Collectors.joining(","));
    }

    private DtoUtils() {
    }
}
