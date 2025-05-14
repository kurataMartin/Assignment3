module kurata.tourguide {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires jdk.jsobject;
    requires javafx.web;
    requires javafx.media;

    opens kurata.tourguide to javafx.fxml;
    exports kurata.tourguide;
}