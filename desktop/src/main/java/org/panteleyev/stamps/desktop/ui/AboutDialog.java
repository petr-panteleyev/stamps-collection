// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui;

import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import org.panteleyev.fx.BaseDialog;
import org.panteleyev.fx.Controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.TimeZone;

import static org.panteleyev.fx.factories.BoxFactory.vBox;
import static org.panteleyev.fx.factories.LabelFactory.label;
import static org.panteleyev.stamps.desktop.ui.MainWindowController.APP_TITLE;
import static org.panteleyev.stamps.desktop.ui.Styles.ABOUT_DIALOG_STYLE_SHEET;
import static org.panteleyev.stamps.desktop.ui.Styles.BIG_SPACING;
import static org.panteleyev.stamps.desktop.ui.Styles.SMALL_SPACING;
import static org.panteleyev.stamps.desktop.ui.Styles.STYLE_ABOUT_LABEL;

public final class AboutDialog extends BaseDialog<Object> {
    private record BuildInformation(String version, String timestamp) {
        static BuildInformation load() {
            var bundle = ResourceBundle.getBundle("buildInfo");
            return new BuildInformation(
                    bundle.getString("version"),
                    bundle.getString("timestamp")
            );
        }
    }

    private static final String YEAR = Integer.toString(LocalDate.now().getYear());

    private static final String RUNTIME = System.getProperty("java.vm.version") + " " + System.getProperty("os.arch");
    private static final String VM = System.getProperty("java.vm.name") + " by " + System.getProperty("java.vm.vendor");
    private static final BuildInformation BUILD = BuildInformation.load();

    private static final ZoneId LOCAL_TIME_ZONE = TimeZone.getDefault().toZoneId();
    private static final DateTimeFormatter TIMESTAMP_PARSER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssVV");
    private static final DateTimeFormatter LOCAL_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    public AboutDialog(Controller owner) {
        super(owner, ABOUT_DIALOG_STYLE_SHEET);

        setHeaderText(APP_TITLE);
        setGraphic(new ImageView(Picture.STAMP.getImage()));

        setTitle("About " + APP_TITLE);

        var aboutLabel = label(APP_TITLE + " " + BUILD.version());
        aboutLabel.getStyleClass().add(STYLE_ABOUT_LABEL);

        var timestamp = ZonedDateTime.parse(BUILD.timestamp(), TIMESTAMP_PARSER)
                .withZoneSameInstant(LOCAL_TIME_ZONE);

        var box = vBox(BIG_SPACING,
                vBox(SMALL_SPACING,
                        aboutLabel,
                        label("Сборка от " + LOCAL_FORMATTER.format(timestamp))
                ),
                vBox(SMALL_SPACING,
                        label("Runtime version: " + RUNTIME),
                        label("VM: " + VM)
                ),
                vBox(SMALL_SPACING,
                        label("Copyright © " + YEAR + " Petr Panteleyev")
                )
        );

        getDialogPane().setContent(box);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK);
    }
}
