// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.ToolBar;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.panteleyev.stamps.desktop.profiles.ConnectDialog;
import org.panteleyev.stamps.desktop.profiles.ConnectionProfile;
import org.panteleyev.stamps.desktop.profiles.ConnectionProfileManager;
import org.panteleyev.stamps.dto.IssueDTO;
import org.panteleyev.stamps.dto.RegionDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.ListResourceBundle;
import java.util.Optional;
import java.util.ResourceBundle;

import static javafx.scene.control.Alert.AlertType.CONFIRMATION;
import static javafx.scene.control.ButtonType.CANCEL;
import static javafx.scene.control.ButtonType.OK;
import static org.panteleyev.fx.factories.ComboBoxFactory.comboBox;
import static org.panteleyev.fx.factories.ComboBoxFactory.comboBoxListCell;
import static org.panteleyev.fx.factories.MenuFactory.menu;
import static org.panteleyev.fx.factories.MenuFactory.menuBar;
import static org.panteleyev.fx.factories.MenuFactory.menuItem;
import static org.panteleyev.stamps.desktop.GlobalContext.stampsService;
import static org.panteleyev.stamps.desktop.settings.Settings.settings;
import static org.panteleyev.stamps.desktop.util.DtoUtils.ISSUE_COMPARATOR_BY_DATE;
import static org.panteleyev.stamps.desktop.util.DtoUtils.REGION_COMPATAOR_BY_NAME;
import static org.panteleyev.stamps.desktop.util.DtoUtils.copy;

public class MainWindowController extends BaseController {
    public static final ResourceBundle UI = new ListResourceBundle() {
        @Override
        protected Object[][] getContents() {
            return new Object[][]{
                    {"button.Cancel", "Отмена"}
            };
        }
    };

    // Menu items enable flags
    private final BooleanProperty editDisabled = new SimpleBooleanProperty(true);
    private final BooleanProperty deleteDisabled = new SimpleBooleanProperty(true);

    private final ConnectionProfileManager profileManager = new ConnectionProfileManager();

    private final CollectionTreeView view = new CollectionTreeView();

    private final ComboBox<RegionDTO> regionComboBox = comboBox(List.of(),
            _ -> comboBoxListCell("-", RegionDTO::getName));

    private static final int CURRENT_YEAR = LocalDate.now().getYear();

    private final Spinner<Integer> startYearSpinner = new Spinner<>(CURRENT_YEAR, CURRENT_YEAR, CURRENT_YEAR);
    private final Spinner<Integer> endYearSpinner = new Spinner<>(CURRENT_YEAR, CURRENT_YEAR, CURRENT_YEAR);

    public MainWindowController(Stage stage) {
        super(stage, settings().getMainCssFilePath());

        profileManager.loadProfiles();

        var center = new BorderPane(view);
        center.setTop(createToolBar());

        var content = new BorderPane(center);
        content.setTop(createMenuBar());

        setupWindow(content);

        regionComboBox.setOnAction(_ -> onRegionChange());
        startYearSpinner.setEditable(true);
        startYearSpinner.getValueFactory().valueProperty().addListener((_, _, _) -> loadData());
        endYearSpinner.setEditable(true);
        endYearSpinner.getValueFactory().valueProperty().addListener((_, _, _) -> loadData());

        view.getSelectionModel().selectedItemProperty().addListener((_, _, newValue) -> onSelectedRow(newValue));

        settings().loadStageDimensions(this);
    }

    @Override
    public String getTitle() {
        return "Коллекция марок";
    }

    @Override
    protected void onWindowHiding() {
        super.onWindowHiding();
        settings().saveWindowsSettings();
    }

    private MenuBar createMenuBar() {
        var connectMenuItem = menuItem("Соединение...", this::onConnect);
        var fileMenu = menu("Файл",
                connectMenuItem,
                new SeparatorMenuItem(),
                menuItem("Выход", _ -> onExit()));

        var newIssueMenuItem = menuItem("Новый выпуск...", this::onNewIssue);
        newIssueMenuItem.setAccelerator(Shortcuts.SHORTCUT_N);
        newIssueMenuItem.disableProperty().bind(stampsService().connectedProperty().not());
        var editIssueMenuItem = menuItem("Редактировать...", this::onEditIssue);
        editIssueMenuItem.setAccelerator(Shortcuts.SHORTCUT_E);
        editIssueMenuItem.disableProperty().bind(editDisabled);
        var deleteIssueMenuItem = menuItem("Удалить...", this::onDeleteIssue);
        deleteIssueMenuItem.disableProperty().bind(deleteDisabled);
        var editMenu = menu("Правка",
                newIssueMenuItem,
                editIssueMenuItem,
                new SeparatorMenuItem(),
                deleteIssueMenuItem
        );

        var profilesMenuItem = menuItem("Профили", this::onProfiles);
        var serviceMenu = menu("Сервис", profilesMenuItem);

        return menuBar(fileMenu, editMenu, serviceMenu);
    }

    private Node createToolBar() {
        return new ToolBar(regionComboBox, new Separator(), startYearSpinner, endYearSpinner);
    }

    private void loadData() {
        var region = regionComboBox.getSelectionModel().getSelectedItem();
        var startYear = startYearSpinner.getValue();
        var endYear = endYearSpinner.getValue();

        var issues = stampsService().getIssues(region.getName(), startYear, endYear).stream()
                .sorted(ISSUE_COMPARATOR_BY_DATE)
                .toList();
        view.setIssues(issues);
    }

    private void onNewIssue(ActionEvent ignored) {
        var region = regionComboBox.getSelectionModel().getSelectedItem();

        var newIssue = new IssueDTO()
                .region(region.getName())
                .title("Новый выпуск")
                .date(LocalDate.now());

        new IssueDialog(newIssue).showAndWait().ifPresent(issue -> {
            stampsService().createIssue(issue);
            loadData();
        });
    }

    private void onEditIssue(ActionEvent ignored) {
        var issue = getParentIssueDTO(view.getSelectionModel().getSelectedItem()).orElse(null);
        if (issue == null) return;

        new IssueDialog(copy(issue)).showAndWait().ifPresent(updated -> {
            stampsService().updateIssue(updated);
            loadData();
        });
    }

    private void onDeleteIssue(ActionEvent ignored) {
        var issue = getParentIssueDTO(view.getSelectionModel().getSelectedItem()).orElse(null);
        if (issue == null) return;

        new Alert(CONFIRMATION, "Вы уверены?", OK, CANCEL)
                .showAndWait()
                .filter(response -> response == OK)
                .ifPresent(_ -> {
                    stampsService().deleteIssue(issue);
                    loadData();
                });
    }

    private Optional<IssueDTO> getParentIssueDTO(TreeItem<?> treeItem) {
        while (treeItem != null) {
            if (treeItem.getValue() instanceof IssueDTO issue) return Optional.of(issue);
            treeItem = treeItem.getParent();
        }
        return Optional.empty();
    }

    private void onProfiles(ActionEvent ignored) {
        profileManager.getEditor().showAndWait();
    }

    private void onConnect(ActionEvent ignored) {
        new ConnectDialog(profileManager).showAndWait()
                .ifPresent(this::open);
    }

    private void open(ConnectionProfile profile) {
        stampsService().init(profile.serverUrl());
        regionComboBox.getItems().addAll(
                stampsService().getRegions().stream()
                        .sorted(REGION_COMPATAOR_BY_NAME)
                        .toList()
        );
        if (!regionComboBox.getItems().isEmpty()) {
            regionComboBox.getSelectionModel().selectLast();
        }
    }

    private void onExit() {
        getStage().fireEvent(new WindowEvent(getStage(), WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    private void onRegionChange() {
        var region = regionComboBox.getValue();
        if (region == null) return;

        var endYear = region.getYearEnd() == null ? CURRENT_YEAR : region.getYearEnd();

        if (startYearSpinner.getValueFactory() instanceof SpinnerValueFactory.IntegerSpinnerValueFactory factory) {
            factory.setMin(region.getYearStart());
            factory.setMax(endYear);
            factory.setValue(region.getYearStart());
        }

        if (endYearSpinner.getValueFactory() instanceof SpinnerValueFactory.IntegerSpinnerValueFactory factory) {
            factory.setMin(region.getYearStart());
            factory.setMax(endYear);
            factory.setValue(endYear);
        }

        loadData();
    }

    private void onSelectedRow(TreeItem<?> selected) {
        if (!stampsService().connectedProperty().get()) {
            editDisabled.set(true);
            deleteDisabled.set(true);
        } else {
            editDisabled.set(selected == null);
            deleteDisabled.set(selected == null || !(selected.getValue() instanceof IssueDTO));
        }
    }
}
