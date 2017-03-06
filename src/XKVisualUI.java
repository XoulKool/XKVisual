/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import static java.lang.Math.floor;
import static java.lang.Math.random;
import static java.lang.String.format;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;

import javafx.application.Application;
import static javafx.application.Platform.runLater;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
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
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Jason
 */
public class XKVisualUI extends Application {

    MediaPlayer mediaPlayer;
    private Label time;
    Duration duration;
    Button playButton, pauseButton, stopButton, fileOpenButton, fullScreenButton;
    Scene scene;
    Media media;
    double width;
    double height;
    MediaView mediaView;
    Group root;
    
    @Override
    public void start(Stage primaryStage) {

        startXKVisual(primaryStage);

    }

    public void restart(Stage stage) {
        startXKVisual(stage);
    }

    public void startXKVisual(Stage stage) {
        initializeMedia(0);

        BorderPane borderPane = new BorderPane();

        root = new Group();
        animation(root);
        borderPane.setCenter(root);
        borderPane.setBottom(addToolBar());
        
        System.out.println(borderPane.getCenter().getBoundsInParent());

        borderPane.setStyle("-fx-background-color: Black");

        scene = new Scene(borderPane, 1000, 800);
        scene.setFill(Color.BLACK);

        /*scene.setOnMousePressed(new EventHandler<MouseEvent>() {
         @Override
         public void handle(MouseEvent event) {
         rippler.setGeneratorCenterX(event.getSceneX());
         rippler.setGeneratorCenterY(event.getSceneY());
         rippler.createRipple();
         rippler.startGenerating();
         }
         });

         scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
         @Override
         public void handle(MouseEvent event) {
         rippler.setGeneratorCenterX(event.getSceneX());
         rippler.setGeneratorCenterY(event.getSceneY());
         }
         });

         scene.setOnMouseReleased(new EventHandler<MouseEvent>() {
         @Override
         public void handle(MouseEvent event) {
         rippler.stopGenerating();
         }
         });*/
        stage.setTitle("XKVisual");
        stage.setScene(scene);
        fullScreenButton.setOnAction((ActionEvent e) -> {
            if (stage.isFullScreen()) {
                stage.setFullScreen(false);
            } else {
                stage.setFullScreen(true);
            }
        });
        
        stage.show();

        DropShadow dropshadow = new DropShadow();
        dropshadow.setOffsetY(5.0);
        dropshadow.setOffsetX(5.0);
        dropshadow.setColor(Color.WHITE);

        mediaView.setEffect(dropshadow);

        fileOpenButton.setOnAction((ActionEvent e) -> {
            mediaPlayer.stop();
            restart(stage);
        });
    }

    private HBox addToolBar() {
        HBox toolBar = new HBox();
        toolBar.setPadding(new Insets(20));
        toolBar.setAlignment(Pos.CENTER);
        toolBar.alignmentProperty().isBound();
        toolBar.setSpacing(5);
        toolBar.setStyle("-fx-background-color: Black");

        playButton = new Button();
        pauseButton = new Button();
        stopButton = new Button();
        fileOpenButton = new Button();
        fullScreenButton = new Button();

        XKButtonSetup(playButton, "/Icons/Play.png");
        XKButtonSetup(pauseButton, "/Icons/Pause.png");
        XKButtonSetup(stopButton, "/Icons/Stop.png");
        XKButtonSetup(fileOpenButton, "/Icons/OpenFile.png");
        XKButtonSetup(fullScreenButton, "/Icons/FullScreen.png");

        buttonFunctionality();

        toolBar.getChildren().addAll(playButton, pauseButton, stopButton, fileOpenButton, time, fullScreenButton);

        return toolBar;
    }

    private void XKButtonSetup(Button button, String imagePath) {
        Image buttonImage = new Image(getClass().getResourceAsStream(imagePath));
        button.setGraphic(new ImageView(buttonImage));
        button.setStyle("-fx-background-color: Black");

        button.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            button.setStyle("-fx-background-color: Black");
            button.setStyle("-fx-body-color: Black");
        });
        button.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            button.setStyle("-fx-background-color: Black");
        });
    }

    private void buttonFunctionality() {
        playButton.setOnAction((ActionEvent e) -> {
            mediaPlayer.play();
        });

        pauseButton.setOnAction((ActionEvent e) -> {
            mediaPlayer.pause();
        });

        stopButton.setOnAction((ActionEvent e) -> {
            mediaPlayer.stop();
        });

    }

    private void initializeMedia(int i) {

        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new ExtensionFilter("*.mp3", "*.mp3"));
        File file = fc.showOpenDialog(null);
        String path = file.getAbsolutePath();
        path = path.replace("\\", "/");
        media = new Media(new File(path).toURI().toString());

        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        mediaView = new MediaView(mediaPlayer);
        mediaView.setMediaPlayer(mediaPlayer);
        initializeTimeLabel();
    }

    private void initializeTimeLabel() {
        time = new Label();
        time.setTextFill(Color.YELLOW);
        time.setPrefWidth(80);

        mediaPlayer.currentTimeProperty().addListener((Observable ov) -> {
            updateValues();

        });
        mediaPlayer.setOnReady(() -> {
            duration = mediaPlayer.getMedia().getDuration();
            updateValues();

        });
        mediaPlayer.setOnPlaying(() -> {
            duration = mediaPlayer.getMedia().getDuration();
            updateValues();
        });
        mediaPlayer.setOnStopped(() -> {
            duration = mediaPlayer.getMedia().getDuration();
            updateValues();
        });
    }

    protected void updateValues() {
        if (time != null) {
            runLater(() -> {
                javafx.util.Duration currentTime = mediaPlayer.getCurrentTime();
                time.setText(formatTime(currentTime, duration));
            });
        }
    }

    private String formatTime(Duration elapsed, Duration duration) {
        int intElapsed = (int) floor(elapsed.toSeconds());
        int elapsedHours = intElapsed / (60 * 60);
        if (elapsedHours > 0) {
            intElapsed -= elapsedHours * 60 * 60;
        }
        int elapsedMinutes = intElapsed / 60;
        int elapsedSeconds = intElapsed - elapsedHours * 60 * 60
                - elapsedMinutes * 60;

        if (duration.greaterThan(Duration.ZERO)) {
            int intDuration = (int) floor(duration.toSeconds());
            int durationHours = intDuration / (60 * 60);
            if (durationHours > 0) {
                intDuration -= durationHours * 60 * 60;
            }
            int durationMinutes = intDuration / 60;
            int durationSeconds = intDuration - durationHours * 60 * 60
                    - durationMinutes * 60;
            if (durationHours > 0) {
                return format("%d:%02d:%02d/%d:%02d:%02d",
                        elapsedHours, elapsedMinutes, elapsedSeconds,
                        durationHours, durationMinutes, durationSeconds);
            } else {
                return format("%02d:%02d/%02d:%02d",
                        elapsedMinutes, elapsedSeconds, durationMinutes,
                        durationSeconds);
            }
        } else {
            if (elapsedHours > 0) {
                return format("%d:%02d:%02d", elapsedHours,
                        elapsedMinutes, elapsedSeconds);
            } else {
                return format("%02d:%02d", elapsedMinutes,
                        elapsedSeconds);
            }
        }
    }

    /*class RippleGenerator extends Group {

     private class Ripple extends Circle {

     Timeline animation = new Timeline(
     new KeyFrame(Duration.ZERO, new KeyValue(radiusProperty(), 0)),
     new KeyFrame(Duration.seconds(1), new KeyValue(opacityProperty(), 1)),
     new KeyFrame(Duration.seconds(1), new KeyValue(radiusProperty(), 100)),
     new KeyFrame(Duration.seconds(1), new KeyValue(opacityProperty(), 0))
     );

     private Ripple(double centerX, double centerY) {
     super(centerX, centerY, 0, null);
     setStroke(Color.rgb(200, 200, 255));
     }
     }

     private double generatorCenterX = 100.0;
     private double generatorCenterY = 100.0;

     private Timeline generate = new Timeline(
     new KeyFrame(Duration.seconds(.25), new EventHandler<ActionEvent>() {
     @Override
     public void handle(ActionEvent event) {
     createRipple();
     }
     }
     )
     );

     public RippleGenerator() {
     generate.setCycleCount(Timeline.INDEFINITE);
     this.createRipple();
     generate.setRate(0.5);
     this.startGenerating();
     
     }

     public void createRipple() {
     final Ripple ripple = new Ripple(generatorCenterX, generatorCenterY);
     getChildren().add(ripple);
     ripple.animation.play();

     Timeline remover = new Timeline(
     new KeyFrame(Duration.seconds(10), new EventHandler<ActionEvent>() {
     @Override
     public void handle(ActionEvent event) {
     getChildren().remove(ripple);
     ripple.animation.stop();
     }
     })
     );
     remover.play();
     }

     public void startGenerating() {
     generate.play();
     }

     public void stopGenerating() {
     generate.stop();
     }

     public void setGeneratorCenterX(double generatorCenterX) {
     this.generatorCenterX = generatorCenterX;
     }

     public void setGeneratorCenterY(double generatorCenterY) {
     this.generatorCenterY = generatorCenterY;
     }

     }*/
       
    
    public void animation(Group group) {

        Group circles = new Group();
        for (int i = 0; i < 10; i++) {
            Circle circle = new Circle(50, Color.web("white", 0.05));
            circle.setStrokeType(StrokeType.OUTSIDE);
            circle.setStroke(Color.web("white", 0.16));
            circle.setStrokeWidth(4);
            circles.getChildren().add(circle);
        }

        LinearGradient grad = new LinearGradient(0f, 1f, 1f, 0f, true, CycleMethod.NO_CYCLE, new Stop[]{
            new Stop(0, Color.web("green")),
            new Stop(0.14, Color.web("turquoise")),
            new Stop(0.28, Color.web("violet")),
            new Stop(0.57, Color.web("yellow")),
            new Stop(0.71, Color.web("hotpink")),
            new Stop(1, Color.web("maroon")),});

        Rectangle colors = new Rectangle(400, 400, grad);//safety orange
        //colors.widthProperty().bind(scene.widthProperty());
        //colors.heightProperty().bind(scene.heightProperty());

        /*root.getChildren().add(colors);
         root.getChildren().add(circles);*/
        Group blendModeGroup
                = new Group(new Group(new Rectangle(400, 400,
                                        Color.BLACK), circles), colors);
        colors.setBlendMode(BlendMode.OVERLAY);
        group.getChildren().add(blendModeGroup);
        circles.setEffect(new BoxBlur(10, 10, 3));

        Timeline timeline = new Timeline();
        for (Node circle : circles.getChildren()) {
            timeline.getKeyFrames().addAll(
                    new KeyFrame(Duration.ZERO, // set start position at 0
                            new KeyValue(circle.translateXProperty(), random() * 400),
                            new KeyValue(circle.translateYProperty(), random() * 400)
                    ),
                    new KeyFrame(new Duration(2000), // set end position at 40s
                            new KeyValue(circle.translateXProperty(), random() * 400),
                            new KeyValue(circle.translateYProperty(), random() * 400)
                    )
            );
        }
        
        
        EventHandler onFinished = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                 root.getChildren().remove(circles);
                 animation(root);
            }
        };
        
        KeyFrame keyFrame = new KeyFrame(new Duration(2000), onFinished);
 
        //add the keyframe to the timeline
        timeline.getKeyFrames().add(keyFrame);
        
        // play 40s of animation
        timeline.play();

    }
}
