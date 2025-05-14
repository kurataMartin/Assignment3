package kurata.tourguide;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.net.URL;

public class TourController {
    private final VBox sidePanel;

    public TourController(VBox sidePanel) {
        this.sidePanel = sidePanel;
    }

    public void updateSidePanel(Hotspot hotspot) {
        sidePanel.getChildren().clear(); // Clear existing content

        // Create a VBox for the hotspot details (for layout)
        VBox vbox = new VBox(10); // Set spacing between elements
        vbox.setStyle("-fx-padding: 10;"); // Add padding to the VBox
        vbox.getStyleClass().add("panel"); // Optionally add panel style class here

        // Add title
        vbox.getChildren().add(createTitle("Hotspot Details"));

        // Add description
        vbox.getChildren().add(createDescriptionTextArea(hotspot));

        // Image (optional - if you have an imagePath in the Hotspot class)
        if (hotspot.getImagePath() != null) {
            ImageView imageView = createImageView(hotspot.getImagePath());
            if (imageView != null) {
                vbox.getChildren().add(new Label("Image:"));
                vbox.getChildren().add(imageView);
            }
        }
        // Audio
        if (hotspot.getAudioPath() != null) {
            URL audioURL = getClass().getClassLoader().getResource(hotspot.getAudioPath());
            if (audioURL != null) {
                Media audio = new Media(audioURL.toExternalForm());
                MediaPlayer audioPlayer = new MediaPlayer(audio);
                Button playAudio = new Button("▶ Play Audio");
                playAudio.getStyleClass().addAll("btn", "btn-secondary");

                playAudio.setOnAction(e -> {
                    MediaPlayer.Status status = audioPlayer.getStatus();
                    if (status == MediaPlayer.Status.PLAYING) {
                        audioPlayer.pause();
                        playAudio.setText("▶ Play Audio");
                    } else {
                        audioPlayer.play();
                        playAudio.setText("⏸ Pause Audio");
                    }
                });

                vbox.getChildren().add(new Label("Audio Guide:"));
                vbox.getChildren().add(playAudio);
            } else {
                System.err.println("Audio not found: " + hotspot.getAudioPath());
            }
        }

        // Video
        if (hotspot.getVideoPath() != null) {
            URL videoURL = getClass().getClassLoader().getResource(hotspot.getVideoPath());
            if (videoURL != null) {
                Media video = new Media(videoURL.toExternalForm());
                MediaPlayer videoPlayer = new MediaPlayer(video);
                MediaView mediaView = new MediaView(videoPlayer);
                mediaView.setFitWidth(280);
                mediaView.setPreserveRatio(true);

                Button playVideo = new Button("▶ Play Video");
                playVideo.getStyleClass().addAll("btn", "btn-secondary");

                playVideo.setOnAction(e -> {
                    MediaPlayer.Status status = videoPlayer.getStatus();
                    if (status == MediaPlayer.Status.PLAYING) {
                        videoPlayer.pause();
                        playVideo.setText("▶ Play Video");
                    } else {
                        videoPlayer.play();
                        playVideo.setText("⏸ Pause Video");
                    }
                });

                vbox.getChildren().add(new Label("Video Tour:"));
                vbox.getChildren().add(mediaView);
                vbox.getChildren().add(playVideo);
            } else {
                System.err.println("Video not found: " + hotspot.getVideoPath());
            }
        }

        // Quiz
        VBox quizSection = createQuizSection(hotspot);
        if (!quizSection.getChildren().isEmpty()) {
            vbox.getChildren().add(quizSection); // Only add if quiz exists
        }

        // Add the populated VBox to the side panel
        sidePanel.getChildren().add(vbox);
    }

    private Label createTitle(String titleText) {
        Label title = new Label(titleText);
        title.getStyleClass().add("panel-title");
        return title;
    }

    private TextArea createDescriptionTextArea(Hotspot hotspot) {
        TextArea info = new TextArea("Location: " + hotspot.getName() + "\n\n" + hotspot.getDescription());
        info.setWrapText(true);
        info.setEditable(false);
        info.getStyleClass().add("form-control");
        return info;
    }

    private VBox createQuizSection(Hotspot hotspot) {
        VBox quizBox = new VBox(10);
        quizBox.setStyle("-fx-padding: 10;");
        quizBox.getStyleClass().add("card");

        Label quizTitle = new Label("Quiz:");
        quizTitle.getStyleClass().add("card-title");
        quizBox.getChildren().add(quizTitle);

        for (Hotspot.Quiz quiz : hotspot.getQuizzes()) {
            VBox qBox = createQuizQuestionBox(quiz);
            quizBox.getChildren().add(qBox);
        }

        return quizBox;
    }

    private VBox createQuizQuestionBox(Hotspot.Quiz quiz) {
        VBox qBox = new VBox(5);
        qBox.getStyleClass().add("card-body");

        Label qLabel = new Label(quiz.getQuestion());
        qLabel.getStyleClass().add("card-text");

        ToggleGroup group = new ToggleGroup();
        for (int i = 0; i < quiz.getChoices().length; i++) {
            RadioButton option = new RadioButton(quiz.getChoices()[i]);
            option.setUserData(i);
            option.setToggleGroup(group);
            option.getStyleClass().add("form-check-input");
            qBox.getChildren().add(option);
        }

        Button checkBtn = new Button("Check Answer");
        checkBtn.getStyleClass().addAll("btn", "btn-primary");

        Label feedback = new Label();
        feedback.getStyleClass().add("card-text");

        checkBtn.setOnAction(e -> checkAnswer(group, quiz, feedback));

        qBox.getChildren().addAll(qLabel, checkBtn, feedback);
        return qBox;
    }

    private void checkAnswer(ToggleGroup group, Hotspot.Quiz quiz, Label feedback) {
        Toggle selected = group.getSelectedToggle();
        if (selected != null) {
            int selectedIndex = (int) selected.getUserData();
            if (selectedIndex == quiz.getCorrectIndex()) {
                feedback.setText("✅ Correct!");
                feedback.setStyle("-fx-text-fill: green;");
            } else {
                feedback.setText("❌ Incorrect. Try again.");
                feedback.setStyle("-fx-text-fill: red;");
            }
        } else {
            feedback.setText("Please select an answer.");
            feedback.setStyle("-fx-text-fill: orange;");
        }
    }

    private ImageView createImageView(String imagePath) {
        ImageView imageView = null;
        try {
            URL imageURL = getClass().getClassLoader().getResource(imagePath);
            if (imageURL != null) {
                Image image = new Image(imageURL.toExternalForm());
                imageView = new ImageView(image);
                imageView.setFitWidth(280);
                imageView.setPreserveRatio(true);
            } else {
                System.err.println("Image not found: " + imagePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageView;
    }

}
