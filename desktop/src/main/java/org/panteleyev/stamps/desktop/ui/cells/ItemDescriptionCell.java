// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui.cells;

import javafx.scene.control.TreeTableCell;
import org.panteleyev.stamps.dto.BlockDTO;
import org.panteleyev.stamps.dto.CouplingDTO;
import org.panteleyev.stamps.dto.IssueDTO;
import org.panteleyev.stamps.dto.StampDTO;

import java.util.stream.Collectors;

import static org.panteleyev.stamps.desktop.ui.Styles.CSS_ISSUE_TITLE;
import static org.panteleyev.stamps.desktop.util.DtoUtils.getCouplingDescription;
import static org.panteleyev.stamps.desktop.util.StringUtil.DATE_FORMATTER;

public class ItemDescriptionCell extends TreeTableCell<Object, Object> {
    @Override
    protected void updateItem(Object item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);
        getStyleClass().remove(CSS_ISSUE_TITLE);

        if (item == null || empty) return;

        var text = switch (item) {
            case IssueDTO issue -> {
                getStyleClass().add(CSS_ISSUE_TITLE);
                yield DATE_FORMATTER.format(issue.getDate()) + " - " + issue.getTitle();
            }
            case StampDTO stamp -> stamp.getDescription();
            case BlockDTO block -> block.getDescription();
            case CouplingDTO coupling -> getCouplingDescription(coupling);
            default -> null;
        };

        setText(text);
    }
}
