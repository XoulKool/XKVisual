
import java.util.Random;
import javafx.scene.Group;
import javafx.scene.effect.Glow;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class WaveForm {

    public Group Waveform(Pane pane, MediaPlayer mediaPlayer) {
        Group vizContainer = new Group();
        mediaPlayer.setAudioSpectrumListener(
                (double timestamp,
                        double duration,
                        float[] magnitudes,
                        float[] phases) -> {
                    vizContainer.getChildren().clear();
                    int i = 0;
                    int x = 20;
                    double y = pane.getHeight() / 2;
                    Random rand = new Random(System.currentTimeMillis());
                    // Build random colored circles
                    for (float phase : phases) {
                        int red = rand.nextInt(255);
                        int green = rand.nextInt(255);
                        int blue = rand.nextInt(255);
                        Circle circle = new Circle(15);
                        circle.setCenterX(x + i);
                        circle.setCenterY(y + (phase * 50));
                        circle.setEffect(new Glow(1.0));
                        circle.setFill(Color.web("white", .16));
                        vizContainer.getChildren().add(circle);
                        i += 10;
                    }
                });
        return vizContainer;
    }
}
