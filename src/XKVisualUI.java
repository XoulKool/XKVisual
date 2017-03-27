/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import static java.lang.Math.floor;
import static java.lang.String.format;
import java.util.Random;

import javafx.application.Application;
import static javafx.application.Platform.runLater;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.swing.JComboBox;

/**
 *
 * @author Jason
 */
public class XKVisualUI extends Application {

    MediaPlayer mediaPlayer;
    private Label time;
    Duration duration;
    Button playButton, pauseButton, stopButton, fileOpenButton, fullScreenButton;
    MenuButton modeSelect, animationSelect;
    MenuItem runByAudio, runByTime, runByUser, ConcentricAnim, CongregAnim, WaveAnim;

    Scene scene;
    Media media;
    double width;
    double height;
    MediaView mediaView;

    Pane pane;

    Group root, wave, conCircles;

    @Override
    public void start(Stage primaryStage) {

        startXKVisual(primaryStage);

    }

    public void restart(Stage stage) {
        startXKVisual(stage);
    }

    public void startXKVisual(Stage stage) {
        initializeMedia(0);

        UsefulFunctions func = new UsefulFunctions();

        BorderPane borderPane = new BorderPane();

        root = new Group();
        pane = new Pane();

        wave = new Group();
        conCircles = new Group();

        /*mediaPlayer.setAudioSpectrumListener(
                (double timestamp,
                        double duration,
                        float[] magnitudes,
                        float[] phases) -> {
                    wave.getChildren().clear();
                    int i = 0;
                    int x = 20;

                    double y = pane.getHeight() + 10;
                    Random rand = new Random(System.currentTimeMillis());
                    // Build random colored circles
                    for (int j = 0; j < 64; j++) {
                        Circle circle = new Circle(15);
                        circle.setCenterX(x + i);
                        circle.setCenterY(y + ((-1 * (magnitudes[j] + 60) * 4)));
                        circle.setFill(Color.web("white", .3));
                        wave.getChildren().add(circle);
                        i += 21;
                    }
                    i = 0;
                    for (int j = 0; j < 64; j++) {
                        Circle circle = new Circle(15);
                        circle.setCenterX(pane.getWidth() - i - 20);
                        circle.setCenterY((magnitudes[j] + 60) * 4);
                        circle.setFill(Color.web("white", .3));
                        wave.getChildren().add(circle);
                        i += 21;
                    }
                    if ((magnitudes[2] + 60) * 4 > 120) {
                        root.getChildren().add(new CongregatedCircles(30, Color.web("blue", 0.1)));
                    } else if ((magnitudes[0] + 60) * 4 > 120) {
                        root.getChildren().add(new CongregatedCircles(60, Color.web("red", 0.1)));
                        conCircles.getChildren().add(new ConcentricGenerator((magnitudes[0] + 60) * 12));

                    } else if ((magnitudes[40] + 60) * 4 > 30) {
                        root.getChildren().add(new CongregatedCircles(60, Color.web("yellow", 0.1)));
                    }
                }
        );
        func.circlePath(conCircles);*/
        borderPane.setCenter(pane);
        borderPane.setBottom(addToolBar());

        borderPane.setStyle("-fx-background-color: Black");

        scene = new Scene(borderPane, 1000, 800);
        scene.setFill(Color.BLACK);

        stage.setTitle("XKVisual");
        stage.setScene(scene);
        stage.setFullScreen(true);
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

        ModeStateContext modeStateContext = new ModeStateContext();
        AnimationStateContext animationStateContext = new AnimationStateContext();

        runByAudio.setOnAction((ActionEvent e) -> {
            RunByAudioMode runByAudioMode = new RunByAudioMode();
            runByAudioMode.setXKListener(modeStateContext);
        });
        
        ConcentricAnim.setOnAction((ActionEvent e) -> {
            if(!modeStateContext.isMode("RunByUser")){
                System.out.println("You cannot select an animation if you are not in Run By User mode!");
                return;
            }
                
        });

        /*new java.util.Timer().schedule(
                                new java.util.TimerTask() {
                            @Override
                            public void run() {
                                //Do some stuff in another thread
                                Platform.runLater(new Runnable() {
                                    public void run() {
                                        func.blendWithGrad(root, wav);
                                        pane.getChildren().clear();
                                        pane.getChildren().add(root);
                                    }
                                });
                            }
                        },
                                0, 15000
                        );*/
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

        //Setup for First Menu Button
        runByAudio = new MenuItem("Run By Audio");
        runByTime = new MenuItem("Run By Time");
        runByUser = new MenuItem("Run By User");
        modeSelect = new MenuButton("Mode Select", null, runByAudio, runByTime, runByUser);

        //Setup for Second Menu Button
        ConcentricAnim = new MenuItem("Concentric Circle Animation");
        CongregAnim = new MenuItem("Congregated Circles Animation");
        WaveAnim = new MenuItem("Waveform Animation");
        animationSelect = new MenuButton("Animation Select", null, ConcentricAnim, CongregAnim, WaveAnim);

        buttonFunctionality();

        toolBar.getChildren().addAll(playButton, pauseButton, stopButton, fileOpenButton, time, fullScreenButton, modeSelect, animationSelect);

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

    class ModeStateContext {

        private ModeState modeState;
        private String modeName;

        ModeStateContext() {
            modeState = new RunByAudioMode();
            modeState.setXKListener(this);
        }

        public void setMode(ModeState newModeState, String newModeName) {
            this.modeState = newModeState;
            this.modeName = newModeName;
        }
        
        public ModeState getModeState(){
            return modeState;
        }
        
        public boolean isMode(String checkModeName){
            return checkModeName.equalsIgnoreCase(this.modeName);
        }
    }

    class RunByAudioMode implements ModeState {

        public void setXKListener(ModeStateContext modeStateContext) {

            modeStateContext.setMode(this, "RunByAudio");

            pane.getChildren().clear();//Clean slate before Button is hit

            mediaPlayer.setAudioSpectrumListener(
                    (double timestamp,
                            double duration,
                            float[] magnitudes,
                            float[] phases) -> {
                        wave.getChildren().clear();
                        int i = 0;
                        int x = 20;

                        double y = pane.getHeight() + 10;
                        Random rand = new Random(System.currentTimeMillis());
                        // Build random colored circles
                        for (int j = 0; j < 64; j++) {
                            Circle circle = new Circle(15);
                            circle.setCenterX(x + i);
                            circle.setCenterY(y + ((-1 * (magnitudes[j] + 60) * 4)));
                            circle.setFill(Color.web("white", .3));
                            wave.getChildren().add(circle);
                            i += 21;
                        }
                        i = 0;
                        for (int j = 0; j < 64; j++) {
                            Circle circle = new Circle(15);
                            circle.setCenterX(pane.getWidth() - i - 20);
                            circle.setCenterY((magnitudes[j] + 60) * 4);
                            circle.setFill(Color.web("white", .3));
                            wave.getChildren().add(circle);
                            i += 21;
                        }
                        if ((magnitudes[2] + 60) * 4 > 120) {
                            root.getChildren().add(new CongregatedCircles(30, Color.web("blue", 0.1)));
                        } else if ((magnitudes[0] + 60) * 4 > 120) {
                            root.getChildren().add(new CongregatedCircles(60, Color.web("red", 0.1)));
                            conCircles.getChildren().add(new ConcentricGenerator((magnitudes[0] + 60) * 12));

                        } else if ((magnitudes[40] + 60) * 4 > 30) {
                            root.getChildren().add(new CongregatedCircles(60, Color.web("yellow", 0.1)));
                        }
                    }
            );

            UsefulFunctions func = new UsefulFunctions();

            func.circlePath(conCircles);

            func.blendWithGrad(root, conCircles, wave);
            pane.getChildren().add(root);
        }
    }

    class AnimationStateContext {

        private AnimationState animationState;

        AnimationStateContext() {
            animationState = null;
        }

        public void setAnimationState(AnimationState newAnimationState) {
            this.animationState = newAnimationState;
        }
        
        public AnimationState getAnimationState(){
            return animationState;
        }
        
        public boolean isAnimationState(AnimationState someAnimationState){
            return this.animationState.equals(someAnimationState);
        }
    }

    class ConcentricGeneratorAnimation implements AnimationState {

        public void setXKAnimationListener(AnimationStateContext animationStateContext) {
            animationStateContext.setAnimationState(this);

            pane.getChildren().clear();
            //Clean slate before Button is hit

            mediaPlayer.setAudioSpectrumListener(
                    (double timestamp,
                            double duration,
                            float[] magnitudes,
                            float[] phases) -> {
                        wave.getChildren().clear();
                        int i = 0;
                        int x = 20;

                        double y = pane.getHeight() + 10;
                        Random rand = new Random(System.currentTimeMillis());
                        // Build random colored circles
                        for (int j = 0; j < 64; j++) {
                            Circle circle = new Circle(15);
                            circle.setCenterX(x + i);
                            circle.setCenterY(y + ((-1 * (magnitudes[j] + 60) * 4)));
                            circle.setFill(Color.web("white", .3));
                            wave.getChildren().add(circle);
                            i += 21;
                        }
                        i = 0;
                        for (int j = 0; j < 64; j++) {
                            Circle circle = new Circle(15);
                            circle.setCenterX(pane.getWidth() - i - 20);
                            circle.setCenterY((magnitudes[j] + 60) * 4);
                            circle.setFill(Color.web("white", .3));
                            wave.getChildren().add(circle);
                            i += 21;
                        }
                        if ((magnitudes[0] + 60) * 4 > 120) {
                            conCircles.getChildren().add(new ConcentricGenerator((magnitudes[0] + 60) * 12));
                        }
                    }
            );

            UsefulFunctions func = new UsefulFunctions();

            func.blendWithGrad(root, conCircles);

            pane.getChildren().add(root);

        }
    }

    class WaveformAnimation implements AnimationState {

        public void setXKAnimationListener(AnimationStateContext animationStateContext) {
            animationStateContext.setAnimationState(this);

            pane.getChildren().clear();
            //Clean slate before Button is hit

            mediaPlayer.setAudioSpectrumListener(
                    (double timestamp,
                            double duration,
                            float[] magnitudes,
                            float[] phases) -> {
                        wave.getChildren().clear();
                        int i = 0;
                        int x = 20;

                        double y = pane.getHeight() + 10;
                        Random rand = new Random(System.currentTimeMillis());
                        // Build random colored circles
                        for (int j = 0; j < 64; j++) {
                            Circle circle = new Circle(15);
                            circle.setCenterX(x + i);
                            circle.setCenterY(y + ((-1 * (magnitudes[j] + 60) * 4)));
                            circle.setFill(Color.web("white", .3));
                            wave.getChildren().add(circle);
                            i += 21;
                        }
                        i = 0;
                        for (int j = 0; j < 64; j++) {
                            Circle circle = new Circle(15);
                            circle.setCenterX(pane.getWidth() - i - 20);
                            circle.setCenterY((magnitudes[j] + 60) * 4);
                            circle.setFill(Color.web("white", .3));
                            wave.getChildren().add(circle);
                            i += 21;
                        }
                    }
            );

            UsefulFunctions func = new UsefulFunctions();

            func.blendWithGrad(root, wave);

            pane.getChildren().add(root);

        }

    }

}
