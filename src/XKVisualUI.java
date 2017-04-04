
import java.io.File;
import static java.lang.Math.floor;
import static java.lang.String.format;
import java.util.Random;
import java.util.Timer;

import javafx.application.Application;
import javafx.application.Platform;
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

/**
 * XKVisualUI.java is the main class for this audio visualizer. It instantiates
 * all necessary function calls to display to the user different representations
 * of audio. This audio is read in by Java FX's MediaPlayer class, and is then
 * controlled by a variety of button and menu selection options.
 *
 * @author Jason Loux
 */
public class XKVisualUI extends Application {

    //Instantiate Necessary Buttons
    MediaPlayer mediaPlayer;
    private Label time;
    private Duration duration;
    private Button playButton, pauseButton, stopButton, fileOpenButton, fullScreenButton;
    private MenuButton modeSelect, animationSelect;
    private MenuItem runByAudio, runByTime, runByUser, ConcentricAnim, CongregAnim, WaveAnim;

    private Scene scene;
    private Media media;
    private MediaView mediaView;

    private Timer runByTimeThread;
    private Timer runByAudioThread;

    private Pane pane;

    private final int totalNumberOfAnimations = 3;

    private Group root, wave, conCircles;

    UsefulFunctions func = new UsefulFunctions();

    private double bassMagnitude;

    @Override
    public void start(Stage primaryStage) {

        startXKVisual(primaryStage);

    }

    /**
     * Called to restart the stage for whatever reason
     *
     * @param stage
     */
    public void restart(Stage stage) {
        startXKVisual(stage);
    }

    /**
     * This function sets the stage for the GUI which will be presented to the
     * user.
     *
     * @param stage
     */
    public void startXKVisual(Stage stage) {
        initializeMedia();

        UsefulFunctions func = new UsefulFunctions();

        BorderPane borderPane = new BorderPane();

        root = new Group();
        pane = new Pane();

        wave = new Group();
        conCircles = new Group();
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

        runByAudio.setOnAction((ActionEvent e) -> {
            if (modeStateContext.isMode("RunByTime")) {
                runByTimeThread.cancel();
            }

            RunByAudioMode runByAudioMode = new RunByAudioMode();
            runByAudioMode.setXKModeListener(modeStateContext);
        });

        runByTime.setOnAction((ActionEvent e) -> {
            RunByTimeMode runByTimeMode = new RunByTimeMode();
            runByTimeMode.setXKModeListener(modeStateContext);
        });

        runByUser.setOnAction((ActionEvent e) -> {
            if (modeStateContext.isMode("RunByTime")) {
                runByTimeThread.cancel();
            }
            RunByUserMode runByUserMode = new RunByUserMode();
            runByUserMode.setXKModeListener(modeStateContext);
        });
        func.circlePath(conCircles);
    }

    /**
     * The context which provides functionality to set and get the appropriate
     * modes.
     *
     */
    class ModeStateContext {

        private ModeState modeState;
        private String modeName;

        /**
         * Immediately sets XKVisual to RunByAudio mode to immerse the user into
         * every created animation.
         */
        ModeStateContext() {
            modeState = new RunByAudioMode();
            modeState.setXKModeListener(this);
        }

        /**
         * This provides functionality to change the current running mode given
         * a new mode state and a new mode name.
         *
         * @param newModeState
         * @param newModeName
         */
        public void setMode(ModeState newModeState, String newModeName) {
            this.modeState = newModeState;
            this.modeName = newModeName;
        }

        /**
         * This function tells you with a boolean value whether or not the mode
         * specified, via a specific String, is running.
         *
         * @param checkModeName
         * @return
         */
        public boolean isMode(String checkModeName) {
            return checkModeName.equalsIgnoreCase(this.modeName);
        }
    }

    /**
     * The RunByAudio mode begins by running the waveform animation. It then
     * schedules a Timing thread which operates every five seconds, updating the
     * current animation with the right one if certain magnitude specifications
     * are met.
     */
    class RunByAudioMode implements ModeState {

        public void setXKModeListener(ModeStateContext modeStateContext) {

            modeStateContext.setMode(this, "RunByAudio");

            pane.getChildren().clear();//Clean slate before Button is hit

            runByAudioThread = new Timer();

            AnimationStateContext animationStateContext = new AnimationStateContext();

            WaveformAnimation waveformAnimation = new WaveformAnimation();
            waveformAnimation.setXKAnimationListener(animationStateContext);

            runByAudioThread.schedule(
                    new java.util.TimerTask() {
                @Override
                public void run() {
                    //Do some stuff in another thread
                    Platform.runLater(new Runnable() {
                        public void run() {
                            System.out.println(bassMagnitude);
                            if (bassMagnitude > 120) {
                                ConcentricGeneratorAnimation concentricGeneratorAnimation = new ConcentricGeneratorAnimation();
                                concentricGeneratorAnimation.setXKAnimationListener(animationStateContext);
                            }
                        }
                    });
                }
            },
                    2000, 2000
            );
        }
    }

    /**
     * The RunByTime mode runs by scheduling a thread which runs a random
     * animation every fifteen seconds. It also takes care to not schedule the
     * same animation twice.
     */
    class RunByTimeMode implements ModeState {

        public void setXKModeListener(ModeStateContext modeStateContext) {

            modeStateContext.setMode(this, "RunByTime");

            runByTimeThread = new Timer();

            AnimationStateContext animationStateContext = new AnimationStateContext();

            runByTimeThread.schedule(
                    new java.util.TimerTask() {
                @Override
                public void run() {
                    //Do some stuff in another thread
                    Platform.runLater(new Runnable() {
                        public void run() {

                            pane.getChildren().clear();

                            boolean sameAnimationAlreadyRunning = false;

                            do {

                                Random random = new Random();
                                int randomAnimation = random.nextInt(totalNumberOfAnimations) + 1;

                                if (randomAnimation == 1 && !animationStateContext.isAnimationState("Concentric Circle Generator")) {
                                    sameAnimationAlreadyRunning = false;
                                    ConcentricGeneratorAnimation concentricGeneratorAnimation = new ConcentricGeneratorAnimation();
                                    concentricGeneratorAnimation.setXKAnimationListener(animationStateContext);
                                } else if (randomAnimation == 2 && !animationStateContext.isAnimationState("Wave")) {
                                    sameAnimationAlreadyRunning = false;
                                    WaveformAnimation waveformAnimation = new WaveformAnimation();
                                    waveformAnimation.setXKAnimationListener(animationStateContext);
                                } else if (randomAnimation == 3 && !animationStateContext.isAnimationState("Congregated Circles")) {
                                    sameAnimationAlreadyRunning = false;
                                    CongregatedCirclesAnimation congregatedCirclesAnimation = new CongregatedCirclesAnimation();
                                    congregatedCirclesAnimation.setXKAnimationListener(animationStateContext);
                                } else {
                                    sameAnimationAlreadyRunning = true;
                                }
                            } while (sameAnimationAlreadyRunning);

                        }
                    });
                }
            },
                    0, 15000
            );

        }
    }

    /**
     * The RunByUser mode allows the user to select a particular animation they
     * want to see at run-time/
     */
    class RunByUserMode implements ModeState {

        public void setXKModeListener(ModeStateContext modeStateContext) {

            modeStateContext.setMode(this, "RunByUser");

            AnimationStateContext animationStateContext = new AnimationStateContext();

            //Set Concentric Generator animation button
            ConcentricAnim.setOnAction((ActionEvent e) -> {
                if (!modeStateContext.isMode("RunByUser")) { //If it's not in this mode nothing should be executed!
                    return;
                }
                ConcentricGeneratorAnimation concentricGeneratorAnimation = new ConcentricGeneratorAnimation();
                concentricGeneratorAnimation.setXKAnimationListener(animationStateContext);
            });

            //Set Waveform Animation button
            WaveAnim.setOnAction((ActionEvent e) -> {
                if (!modeStateContext.isMode("RunByUser")) {//If it's not in this mode nothing should be executed!
                    return;
                }
                WaveformAnimation waveformAnimation = new WaveformAnimation();
                waveformAnimation.setXKAnimationListener(animationStateContext);
            });

            //Congregated Circles animation button
            CongregAnim.setOnAction((ActionEvent e) -> {

                if (!modeStateContext.isMode("RunByUser")) {//If it's not in this mode nothing should be executed!
                    return;
                }

                CongregatedCirclesAnimation congregatedCirclesAnimation = new CongregatedCirclesAnimation();
                congregatedCirclesAnimation.setXKAnimationListener(animationStateContext);
            });

        }
    }

    /**
     * This class supplies functionality to XKVisual so that a particular
     * animation can be selected cleanly and succinctly.
     */
    class AnimationStateContext {

        private AnimationState animationState;
        private String animationName;

        /**
         * Don't do anything except globals to null when instantiated
         */
        AnimationStateContext() {
            animationState = null;
            animationName = null;
        }

        /**
         * Allows a new animation to be selected and carries out necessary
         * operations to update the global variables.
         *
         * @param newAnimationState
         * @param newAnimationName
         */
        public void setAnimation(AnimationState newAnimationState, String newAnimationName) {
            this.animationState = newAnimationState;
            this.animationName = newAnimationName;
        }

        /**
         * Reports with a boolean whether or not AnimationModeContext is in the
         * specified state with a string.
         *
         * @param animationToBeRan
         * @return
         */
        public boolean isAnimationState(String checkAnimation) {
            return checkAnimation.equalsIgnoreCase(this.animationName);
        }
    }

    /**
     * This class enables functionality and accessibility to the Concentric
     * Circle generator class so that it can be used with the state design
     * pattern.
     */
    class ConcentricGeneratorAnimation implements AnimationState {

        public void setXKAnimationListener(AnimationStateContext animationStateContext) {

            animationStateContext.setAnimation(this, "Concentric Circle Generator");

            pane.getChildren().clear();
            //Clean slate before Button is hit

            mediaPlayer.setAudioSpectrumListener(
                    (double timestamp,
                            double duration,
                            float[] magnitudes,
                            float[] phases) -> {

                        bassMagnitude = (magnitudes[0] + 60) * 4;

                        if ((magnitudes[0] + 60) * 4 > 120) {
                            conCircles.getChildren().add(new ConcentricGenerator((magnitudes[0] + 60) * 12));
                        }
                    }
            );
            func.blendWithGrad(root, conCircles);
            pane.getChildren().add(root);

        }
    }

    /**
     * The CongregatedCirclesAnimation class provides accessibility to the
     * CongregatedCircles class so that this particular animation can be shown
     * by itself when in either RunByUser or RunByTime mode.
     */
    class CongregatedCirclesAnimation implements AnimationState {

        public void setXKAnimationListener(AnimationStateContext animationStateContext) {
            animationStateContext.setAnimation(this, "Congregated Circles");

            pane.getChildren().clear();//Clean slate Once mode is selected

            mediaPlayer.setAudioSpectrumListener(
                    (double timestamp,
                            double duration,
                            float[] magnitudes,
                            float[] phases) -> {

                        bassMagnitude = (magnitudes[0] + 60) * 4;

                        if ((magnitudes[2] + 60) * 4 > 120) {
                            root.getChildren().add(new CongregatedCircles(30, Color.web("blue", 0.1)));
                        } else if ((magnitudes[0] + 60) * 4 > 120) {
                            root.getChildren().add(new CongregatedCircles(60, Color.web("red", 0.1)));

                        } else if ((magnitudes[40] + 60) * 4 > 30) {
                            root.getChildren().add(new CongregatedCircles(60, Color.web("yellow", 0.1)));
                        }
                    }
            );

            func.blendWithGrad(root);
            pane.getChildren().add(root);

        }
    }

    /**
     * This animation creates a waveform constructed of small circles which
     * dances to the music being played.
     */
    class WaveformAnimation implements AnimationState {

        public void setXKAnimationListener(AnimationStateContext animationStateContext) {

            animationStateContext.setAnimation(this, "Wave");

            pane.getChildren().clear();
            //Clean slate before Button is hit

            mediaPlayer.setAudioSpectrumListener(
                    (double timestamp,
                            double duration,
                            float[] magnitudes,
                            float[] phases) -> {
                        wave.getChildren().clear();

                        bassMagnitude = (magnitudes[0] + 60) * 4;

                        int i = 0;

                        int x = 20;

                        double y = pane.getHeight() + 10;
                        Random rand = new Random(System.currentTimeMillis());
                        // Build random colored circles
                        for (int j = 0; j < 64; j++) {
                            Circle circle = new Circle(15);
                            circle.setCenterX(x + i);
                            circle.setCenterY(y + ((-1 * (magnitudes[j] + 60) * 10)));
                            circle.setFill(Color.web("white", .3));
                            wave.getChildren().add(circle);
                            i += 21;
                        }
                        i = 0;
                        for (int j = 0; j < 64; j++) {
                            Circle circle = new Circle(15);
                            circle.setCenterX(pane.getWidth() - i - 20);
                            circle.setCenterY(y + ((-1 * (magnitudes[j] + 60) * 10)));
                            circle.setFill(Color.web("white", .3));
                            wave.getChildren().add(circle);
                            i += 21;
                        }
                        i = 0;
                        for (int j = 0; j < 64; j++) {
                            Circle circle = new Circle(15);
                            circle.setCenterX(pane.getWidth() - i - 20);
                            circle.setCenterY((magnitudes[j] + 60) * 10);
                            circle.setFill(Color.web("white", .3));
                            wave.getChildren().add(circle);
                            i += 21;
                        }
                        i = 0;
                        for (int j = 0; j < 64; j++) {
                            Circle circle = new Circle(15);
                            circle.setCenterX(x + i);
                            circle.setCenterY((magnitudes[j] + 60) * 10);
                            circle.setFill(Color.web("white", .3));
                            wave.getChildren().add(circle);
                            i += 21;
                        }
                    }
            );

            func.blendWithGrad(root, wave);

            pane.getChildren().add(root);

        }

    }

    /**
     * This function takes care of setting up the toolbar at the bottom of the
     * XKVisual UI.
     *
     * @return
     */
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

        //Setup Buttons with Icons
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

    /**
     * A sort of Factory pattern. Since all icon buttons basically need the
     * exact same setup and are instantiated the same way, I created a function
     * to setup a particular button given only the button and a string denoting
     * the path of your Icon image on your computer.
     *
     * @param button
     * @param imagePath
     */
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

    /**
     * This function provides functionality to each non-OpenFile Icon button.
     * The MediaPlayer class' play and pause methods made these particular
     * buttons easy to provide functionality to.
     */
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

    /**
     * This function provides the ability to access a particular .mp3 file by
     * utilizing java's FileChooser class.
     *
     */
    private void initializeMedia() {

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

    /**
     * This function initializes the java Label which keeps track of the time
     * and duration of the currently selected media. This works by implementing
     * an observer pattern and updating the values within this label based off
     * of the point in time where the MediaPlayer is in a particular song.
     */
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

    /**
     * Used in conjuction with the listener in the initializeTimeLabel
     */
    protected void updateValues() {
        if (time != null) {
            runLater(() -> {
                javafx.util.Duration currentTime = mediaPlayer.getCurrentTime();
                time.setText(formatTime(currentTime, duration));
            });
        }
    }

    /**
     * Formats the Duration type given in the updateValues function into a
     * meaningful minute/seconds representation.
     *
     * @param elapsed
     * @param duration
     * @return
     */
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

}
