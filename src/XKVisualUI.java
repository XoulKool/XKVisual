/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import static java.lang.Math.floor;
import static java.lang.Math.random;
import static java.lang.String.format;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PathTransition;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import static javafx.scene.paint.Color.color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.Circle;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathBuilder;
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
        Rectangle rect = new Rectangle(1000, 600);
        rect.setFill(Color.BLUE);

        ConcentricGenerator conCircles = new ConcentricGenerator();
        BouncyCircles bc = new BouncyCircles();
        //animation(conCircles);
        
        circlePath(conCircles);
        //blendWithGrad(conCircles);
        blendWithGrad(bc);

        Pane pane = new Pane();
        pane.getChildren().addAll(root);

        borderPane.setCenter(pane);
        borderPane.setBottom(addToolBar());

        borderPane.setStyle("-fx-background-color: Black");

        scene = new Scene(borderPane, 1000, 800);
        scene.setFill(Color.BLACK);

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

    public void animation(Group group) {

        Group circles = new Group();
        for (int i = 0; i < 100; i++) {
            Circle circle = new Circle(25, Color.web("white", 0.05));
            circle.setStrokeType(StrokeType.OUTSIDE);
            circle.setStroke(Color.web("white", 0.16));
            circle.setStrokeWidth(4);
            circle.setCenterX(500);
            circle.setCenterY(300);
            circles.getChildren().add(circle);
        }


        /*root.getChildren().add(colors);
         root.getChildren().add(circles);*/

        Timeline timeline = new Timeline();
         for (Node circle : circles.getChildren()) {
         timeline.getKeyFrames().addAll(
         new KeyFrame(Duration.ZERO, // set start position at 0
         new KeyValue(circle.translateXProperty(), 0),
         new KeyValue(circle.translateYProperty(), 0)
         ),
         new KeyFrame(new Duration(2000), // set end position at 40s
         new KeyValue(circle.translateXProperty(), random() * 1000),
         new KeyValue(circle.translateYProperty(), random() * 1000)
         )
         );
         }
        /*EventHandler onFinished = new EventHandler<ActionEvent>() {
         public void handle(ActionEvent t) {
         group.getChildren().remove(blendModeGroup);
         animation(group);
         }
         };*/
        //KeyFrame keyFrame = new KeyFrame(new Duration(4000), onFinished);
        //add the keyframe to the timeline
        //timeline.getKeyFrames().add(keyFrame);
        // play 40s of animation
        //timeline.play();
    }

    public void circlePath(Node node) {

        Path path = createEllipsePath(1400, 400, 700, 300, 0);

        //path.getElements().add(new MoveTo(50, 50));
        //path.getElements().add(arcTo);
        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(4000));
        pathTransition.setPath(path);
        pathTransition.setNode(node);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setCycleCount(Timeline.INDEFINITE);
        pathTransition.setInterpolator(Interpolator.LINEAR);
        pathTransition.setAutoReverse(false);
        pathTransition.setRate(.5);
        pathTransition.play();
    }

    private Path createEllipsePath(double centerX, double centerY, double radiusX, double radiusY, double rotate) {
        ArcTo arcTo = new ArcTo();
        arcTo.setX(centerX - radiusX + 1); // to simulate a full 360 degree celcius circle.
        arcTo.setY(centerY - radiusY);
        arcTo.setSweepFlag(false);
        arcTo.setLargeArcFlag(true);
        arcTo.setRadiusX(radiusX);
        arcTo.setRadiusY(radiusY);
        arcTo.setXAxisRotation(rotate);

        Path path = PathBuilder.create()
                .elements(
                        new MoveTo(centerX - radiusX, centerY - radiusY),
                        arcTo,
                        new ClosePath()) // close 1 px gap.
                .build();
        return path;
    }

    public void blendWithGrad(Group group) {
        LinearGradient grad = new LinearGradient(0f, 1f, 1f, 0f, true, CycleMethod.NO_CYCLE, new Stop[]{
            new Stop(0, Color.web("green")),
            new Stop(0.14, Color.web("turquoise")),
            new Stop(0.28, Color.web("violet")),
            new Stop(0.57, Color.web("yellow")),
            new Stop(0.71, Color.web("hotpink")),
            new Stop(1, Color.web("maroon")),});

        Rectangle colors = new Rectangle(1600, 1200, grad);

        Group blendModeGroup
                = new Group(new Group(new Rectangle(1600, 1200,
                                        Color.BLACK), group), colors);
        colors.setBlendMode(BlendMode.OVERLAY);
        root.getChildren().add(blendModeGroup);
        group.setEffect(new BoxBlur(3, 3, 3));
    }

}
