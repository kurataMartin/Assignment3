package kurata.tourguide;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Main layout
        BorderPane root = new BorderPane();

        // Side panel (right)
        VBox sidePanel = new VBox(10);
        sidePanel.setStyle("-fx-padding: 10;");
        sidePanel.setPrefWidth(320);
        sidePanel.getStyleClass().add("card");

        // Scrollable container for side panel
        ScrollPane scrollPane = new ScrollPane(sidePanel);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefWidth(320);
        root.setRight(scrollPane);

        // WebView in the center
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.load(getClass().getResource("/kurata/tourguide/map.html").toExternalForm());
        root.setCenter(webView);

        // Controller for handling hotspot updates
        TourController tourController = new TourController(sidePanel);

        // Handle JS alert messages sent from map.html
        webEngine.setOnAlert(event -> {
            String data = event.getData();
            if (data.startsWith("hotspot:")) {
                String id = data.substring("hotspot:".length());
                Hotspot hotspot = Hotspot.fromId(id);
                if (hotspot != null) {
                    tourController.updateSidePanel(hotspot);
                } else {
                    System.out.println("Unknown hotspot ID: " + id);
                }
            }
        });

        // Set up the JavaFX scene
        Scene scene = new Scene(root, 1000, 600);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());

        primaryStage.setTitle("Lesotho Tour Guide");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
