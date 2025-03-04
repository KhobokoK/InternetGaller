package com.example.richinternetimagegallery;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class internetgallery extends Application {
    private List<Image> images = new ArrayList<>();
    private int currentIndex = 0;

    @Override
    public void start(Stage primaryStage) {
        loadImages();

        BorderPane root = new BorderPane();
        GridPane thumbnailGrid = createThumbnailGrid(primaryStage);

        ScrollPane scrollPane = new ScrollPane(thumbnailGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.getStyleClass().add("scroll-pane");

        root.setCenter(scrollPane);

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/styles/gallery.css").toExternalForm());

        primaryStage.setTitle("Image Gallery");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadImages() {
        File folder = new File("images/");
        File[] imageFiles = folder.listFiles();
        if (imageFiles != null) {
            for (File file : imageFiles) {
                if (file.getName().endsWith(".jpg") || file.getName().endsWith(".png")) {
                    images.add(new Image(file.toURI().toString()));
                }
            }
        }
    }

    private GridPane createThumbnailGrid(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.getStyleClass().add("grid-pane");

        int col = 0, row = 0;
        for (int i = 0; i < images.size(); i++) {
            ImageView thumbnail = new ImageView(images.get(i));
            thumbnail.setFitWidth(100);
            thumbnail.setFitHeight(100);
            thumbnail.setPreserveRatio(true);

            Button button = new Button("", thumbnail);
            button.getStyleClass().add("thumbnail-button");
            final int index = i;
            button.setOnAction(e -> openFullImage(primaryStage, index));

            grid.add(button, col, row);
            col++;
            if (col == 4) {
                col = 0;
                row++;
            }
        }
        return grid;
    }

    private void openFullImage(Stage primaryStage, int index) {
        currentIndex = index;
        Stage fullImageStage = new Stage();
        fullImageStage.initModality(Modality.APPLICATION_MODAL);
        fullImageStage.initOwner(primaryStage);

        ImageView fullImageView = new ImageView(images.get(index));
        fullImageView.setPreserveRatio(true);
        fullImageView.setFitWidth(800);

        Button prevButton = new Button("Previous");
        Button nextButton = new Button("Next");
        Button closeButton = new Button("Close");

        prevButton.setOnAction(e -> {
            if (currentIndex > 0) {
                currentIndex--;
                fullImageView.setImage(images.get(currentIndex));
            }
        });

        nextButton.setOnAction(e -> {
            if (currentIndex < images.size() - 1) {
                currentIndex++;
                fullImageView.setImage(images.get(currentIndex));
            }
        });

        closeButton.setOnAction(e -> fullImageStage.close());

        HBox controls = new HBox(20, prevButton, closeButton, nextButton);
        controls.setStyle("-fx-alignment: center;");

        VBox layout = new VBox(10, fullImageView, controls);
        layout.setStyle("-fx-alignment: center;");

        Scene fullImageScene = new Scene(new StackPane(layout), 900, 700);
        fullImageStage.setScene(fullImageScene);
        fullImageStage.setTitle("Full Image View");
        fullImageStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
