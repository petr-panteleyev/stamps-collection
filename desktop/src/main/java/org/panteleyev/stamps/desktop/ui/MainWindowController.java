// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: BSD-2-Clause
package org.panteleyev.stamps.desktop.ui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.panteleyev.fx.FxAction;
import org.panteleyev.stamps.desktop.model.CollectionItem;
import org.panteleyev.stamps.desktop.profiles.ConnectDialog;
import org.panteleyev.stamps.desktop.profiles.ConnectionProfile;
import org.panteleyev.stamps.desktop.profiles.ConnectionProfileManager;
import org.panteleyev.stamps.desktop.ui.albums.AlbumsEditor;
import org.panteleyev.stamps.desktop.ui.tags.TagsEditor;
import org.panteleyev.stamps.dto.AlbumDTO;
import org.panteleyev.stamps.dto.ImageUploadDTO;
import org.panteleyev.stamps.dto.IssueDTO;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.ListResourceBundle;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

import static javafx.scene.control.Alert.AlertType.CONFIRMATION;
import static javafx.scene.control.ButtonType.CANCEL;
import static javafx.scene.control.ButtonType.OK;
import static org.panteleyev.functional.Scope.apply;
import static org.panteleyev.fx.FxAction.ACTION_SEPARATOR;
import static org.panteleyev.fx.FxAction.fxAction;
import static org.panteleyev.fx.factories.FileChooserFactory.fileChooser;
import static org.panteleyev.fx.factories.MenuFactory.menu;
import static org.panteleyev.fx.factories.MenuFactory.menuBar;
import static org.panteleyev.fx.factories.MenuFactory.menuItem;
import static org.panteleyev.stamps.desktop.GlobalContext.stampsService;
import static org.panteleyev.stamps.desktop.settings.Settings.settings;
import static org.panteleyev.stamps.desktop.ui.Shortcuts.SHORTCUT_ALT_B;
import static org.panteleyev.stamps.desktop.ui.Shortcuts.SHORTCUT_ALT_C;
import static org.panteleyev.stamps.desktop.ui.Shortcuts.SHORTCUT_ALT_I;
import static org.panteleyev.stamps.desktop.ui.Shortcuts.SHORTCUT_E;
import static org.panteleyev.stamps.desktop.ui.Shortcuts.SHORTCUT_N;
import static org.panteleyev.stamps.desktop.ui.Styles.TOOLTIP_BLOCK_IMAGE_SIZE;
import static org.panteleyev.stamps.desktop.ui.Styles.TOOLTIP_STAMP_IMAGE_SIZE;
import static org.panteleyev.stamps.desktop.util.DtoUtils.ALBUM_COMPARATOR_BY_NAME;
import static org.panteleyev.stamps.desktop.util.DtoUtils.ISSUE_COMPARATOR_BY_DATE;
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

    private static final FileChooser.ExtensionFilter IMAGE_EXTENSION_FILTER =
            new FileChooser.ExtensionFilter("Изображения", List.of("*.png", "*.jpg", "*.jpeg"));

    private final ConnectionProfileManager profileManager = new ConnectionProfileManager();

    private final CollectionTableView view = new CollectionTableView();

    private final RadioButton showAllRadio = new RadioButton("Все");
    private final RadioButton showPresentRadio = new RadioButton("В наличии");
    private final RadioButton showMissingRadio = new RadioButton("Манколист");
    private final RadioButton showReplacementRadio = new RadioButton("На замену");

    // Action enable flags
    private final BooleanProperty newIssueEnabled = new SimpleBooleanProperty(false);
    private final BooleanProperty editIssueEnabled = new SimpleBooleanProperty(false);
    private final BooleanProperty deleteIssueEnabled = new SimpleBooleanProperty(false);
    private final BooleanProperty uploadImageEnabled = new SimpleBooleanProperty(false);
    private final BooleanProperty tagsEditorEnabled = new SimpleBooleanProperty(false);
    private final BooleanProperty editAlbumsEnabled = new SimpleBooleanProperty(false);

    // Actions
    private final FxAction newIssueAction = fxAction("Новый выпуск...")
            .onAction(this::onNewIssue).accelerator(SHORTCUT_N).disableBinding(newIssueEnabled.not());
    private final FxAction editIssueAction = fxAction("Редактировать...")
            .onAction(this::onEditIssue).accelerator(SHORTCUT_E).disableBinding(editIssueEnabled.not());
    private final FxAction deleteIssueAction = fxAction("Удалить...")
            .onAction(this::onDeleteIssue).disableBinding(deleteIssueEnabled.not());
    private final FxAction uploadImageAction = fxAction("Загрузить изображение...")
            .onAction(this::onUploadImage).accelerator(SHORTCUT_ALT_I).disableBinding(uploadImageEnabled.not());
    private final FxAction editTagsAction = fxAction("Теги")
            .onAction(this::onEditTags).disableBinding(tagsEditorEnabled.not());

    private final AtomicReference<String> imageDirectory = new AtomicReference<>(null);

    private AlbumDTO currentAlbum = null;

    private final Menu albumMenu = menu("Альбомы");

    public MainWindowController(Stage stage) {
        super(stage, settings().getMainCssFilePath());

        profileManager.loadProfiles();

        var center = new BorderPane(view);
        center.setTop(createToolBar());

        var content = new BorderPane(center);
        content.setTop(createMenuBar());

        setupWindow(content);

        view.getSelectionModel().selectedItemProperty().addListener((_, _, newValue) -> onSelectedRow(newValue));
        view.setContextMenu(createContextMenu());

        settings().loadStageDimensions(this);
    }

    @Override
    public String getTitle() {
        var title = "Коллекция марок";
        if (currentAlbum != null) {
            title += " - " + currentAlbum.getName();
        }
        return title;
    }

    @Override
    protected void onWindowHiding() {
        super.onWindowHiding();
        settings().saveWindowsSettings();
    }

    private MenuBar createMenuBar() {
        var connectMenuItem = menuItem("Соединение...", this::onConnect);
        connectMenuItem.setAccelerator(SHORTCUT_ALT_C);
        var fileMenu = menu("Файл",
                connectMenuItem,
                new SeparatorMenuItem(),
                menuItem("Выход", _ -> onExit()));

        var editMenu = menu("Правка",
                newIssueAction.createMenuItem(),
                editIssueAction.createMenuItem(),
                new SeparatorMenuItem(),
                deleteIssueAction.createMenuItem(),
                new SeparatorMenuItem(),
                uploadImageAction.createMenuItem(),
                new SeparatorMenuItem(),
                editTagsAction.createMenuItem()
        );

        var editAlbumsMenuItem = menuItem("Редактировать альбомы", this::onEditAlbums);
        editAlbumsMenuItem.disableProperty().bind(editAlbumsEnabled.not());
        editAlbumsMenuItem.setAccelerator(SHORTCUT_ALT_B);
        albumMenu.getItems().add(editAlbumsMenuItem);

        var profilesMenuItem = menuItem("Профили", this::onProfiles);
        var serviceMenu = menu("Сервис", profilesMenuItem);

        return menuBar(fileMenu, editMenu, albumMenu, serviceMenu);
    }

    private ContextMenu createContextMenu() {
        return FxAction.createContextMenu(List.of(
                        newIssueAction, editIssueAction,
                        ACTION_SEPARATOR, deleteIssueAction,
                        ACTION_SEPARATOR, uploadImageAction
                )
        );
    }

    private void buildAlbumMenu() {
        var albumMenuSize = albumMenu.getItems().size();
        for (var index = 1; index < albumMenuSize; index++) {
            albumMenu.getItems().removeLast();
        }

        var albums = stampsService().loadAlbums().stream()
                .sorted(ALBUM_COMPARATOR_BY_NAME)
                .toList();
        if (albums.isEmpty()) return;

        albumMenu.getItems().add(new SeparatorMenuItem());

        for (var album : albums) {
            if (album.getSubalbums().isEmpty()) {
                albumMenu.getItems().add(albumMenuItem(album));
            } else {
                var menu = menu(album.getName());
                albumMenu.getItems().add(menu);
                menu.getItems().addAll(albumMenuItem(album), new SeparatorMenuItem());
                for (var subalbum : album.getSubalbums()) {
                    menu.getItems().add(albumMenuItem(subalbum));
                }
            }
        }
    }

    private MenuItem albumMenuItem(AlbumDTO album) {
        return menuItem(album.getName(), _ -> onAlbum(album));
    }

    private Node createToolBar() {
        var group = new ToggleGroup();
        showAllRadio.setToggleGroup(group);
        showPresentRadio.setToggleGroup(group);
        showMissingRadio.setToggleGroup(group);
        showReplacementRadio.setToggleGroup(group);
        showPresentRadio.setSelected(true);

        showAllRadio.setOnAction(_ -> view.showAll());
        showPresentRadio.setOnAction(_ -> view.showPresent());
        showMissingRadio.setOnAction(_ -> view.showMissing());
        showReplacementRadio.setOnAction(_ -> view.showReplacement());

        return new ToolBar(showPresentRadio, showMissingRadio, showReplacementRadio, showAllRadio);
    }

    private void onAlbum(AlbumDTO album) {
        currentAlbum = album;
        getStage().setTitle(getTitle());
        loadData();
        onSelectedRow(null);
        showPresentRadio.fire();
        view.showPresent();
    }

    private void loadData() {
        view.setIssues(stampsService().loadIssues(currentAlbum));
    }

    private void onNewIssue(ActionEvent ignored) {
        var newIssue = new IssueDTO()
                .region(currentAlbum.getRegion())
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

    private void onUploadImage(ActionEvent ignored) {
        var selected = view.getSelectionModel().getSelectedItem();
        if (selected == null || selected.isIssue()) return;

        var file = apply(fileChooser("Открыть", List.of(IMAGE_EXTENSION_FILTER)), fileChooser -> {
            if (imageDirectory.get() != null) {
                fileChooser.setInitialDirectory(new File(imageDirectory.get()));
            }
        }).showOpenDialog(getStage());
        if (file == null) return;

        imageDirectory.set(file.getParent());

        var dimension = selected.isStamp() ? TOOLTIP_STAMP_IMAGE_SIZE : TOOLTIP_BLOCK_IMAGE_SIZE;

        try (var inputStream = new FileInputStream(file)) {
            var image = new Image(inputStream, dimension, dimension, true, true);

            var bufferedImage = SwingFXUtils.fromFXImage(image, null);
            var outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "PNG", outputStream);
            var bytes = outputStream.toByteArray();
            var encoded = Base64.getEncoder().encodeToString(bytes);

            var dto = new ImageUploadDTO().image(encoded);
            stampsService().uploadImage(selected.getId(), dto);
            view.refresh();

        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    private void onEditTags(ActionEvent ignored) {
        new TagsEditor().showAndWait();
    }

    private void onEditAlbums(ActionEvent ignored) {
        new AlbumsEditor().showAndWait().ifPresent(result -> {
            for (var update : result.toUpdate()) {
                stampsService().updateAlbum(update);
            }
            for (var create : result.toAdd()) {
                stampsService().createAlbum(create);
            }
            buildAlbumMenu();
        });
    }

    private Optional<IssueDTO> getParentIssueDTO(CollectionItem collectionItem) {
        if (collectionItem == null) return Optional.empty();
        return Optional.of(collectionItem.getParent());
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
        buildAlbumMenu();
        onSelectedRow(null);
    }

    private void onExit() {
        getStage().fireEvent(new WindowEvent(getStage(), WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    private void onSelectedRow(CollectionItem selected) {
        tagsEditorEnabled.set(stampsService().connectedProperty().get());
        editAlbumsEnabled.set(stampsService().connectedProperty().get());

        if (!stampsService().connectedProperty().get() || currentAlbum == null) {
            newIssueEnabled.set(false);
            editIssueEnabled.set(false);
            deleteIssueEnabled.set(false);
            uploadImageEnabled.set(false);
        } else {
            newIssueEnabled.set(true);
            editIssueEnabled.set(selected != null);
            deleteIssueEnabled.set(selected != null && selected.isIssue());
            uploadImageEnabled.set(
                    selected != null && !selected.isIssue()
            );
        }
    }
}
