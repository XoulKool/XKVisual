/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.time.Duration;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author Jason
 */
public class XKVisualUI extends Application {

    MediaPlayer mediaPlayer;
    private Label time;
    Duration duration;
    Button fullScreenButton;
    Button playButton;
    Scene scene;
    Media media;
    double width;
    double height;
    MediaView mediaView;

    @Override
    public void start(Stage primaryStage) {

//The location of your file
        Media media = new Media(new File("/Users/Jason/Music/iTunes/iTunes Media/Music"
                + "/A Tribe Called Quest/We got it from Here... Thank You 4 Your/1-01 The Space Program.mp3").toURI().toString());

        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        MediaView mediaView = new MediaView(mediaPlayer);
        Image playButtonImage = new Image(getClass().getResourceAsStream("Play.png"));
        Button playButton = new Button();
        playButton.setGraphic(new ImageView(playButtonImage));
        playButton.setStyle("-fx-background-color: Black");

        playButton.setOnAction((ActionEvent e) -> {

        });
        playButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            playButton.setStyle("-fx-background-color: Black");
            playButton.setStyle("-fx-body-color: Black");
        });
        playButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            playButton.setStyle("-fx-background-color: Black");
        });

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(mediaView);
        borderPane.setBottom(addToolBar());

        borderPane.setStyle("-fx-background-color: Black");

        Scene scene = new Scene(borderPane, 600, 600);
        scene.setFill(Color.BLACK);

        primaryStage.setTitle("Media Player!");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private HBox addToolBar() {
        HBox toolBar = new HBox();
        toolBar.setPadding(new Insets(20));
        toolBar.setAlignment(Pos.CENTER);
        toolBar.alignmentProperty().isBound();
        toolBar.setSpacing(5);
        toolBar.setStyle("-fx-background-color: Black");

        return toolBar;
    }
}
