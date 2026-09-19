module stamps.desktop {
    exports org.panteleyev.stamps.desktop;
    exports org.panteleyev.stamps.desktop.model;

    requires java.xml;
    requires org.panteleyev.freedesktop;
    requires org.panteleyev.commons;
    requires org.panteleyev.fx;
    requires javafx.graphics;
    requires javafx.swing;
    requires stamps.client;
    requires org.controlsfx.controls;
    requires java.logging;
}