
import static java.lang.Math.random;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeType;
import javafx.util.Duration;

/**
 * This class provides an animation of intersecting lines whose length
 * corresponds to the magnitude of the music.
 *
 * @author Kelly
 */
public class SlashingLines extends Group{

    private final Duration animationLength = Duration.seconds(5);

    private class Slash extends Line {
        
        Timeline animation;
        
        Slash(double x1, double y1, double x2, double y2, Color color) {
            super(0, 0, 0, 0);
            setFill(color);
            setStroke(Color.web("white", .5));
            setStrokeWidth(20);
            animation = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(startXProperty(), x1*4)),
                    new KeyFrame(Duration.ZERO, new KeyValue(startYProperty(), y1 *3)),
                    new KeyFrame(Duration.ZERO, new KeyValue(endXProperty(), x2)),
                    new KeyFrame(Duration.ZERO, new KeyValue(endYProperty(), y2*2)),
                    new KeyFrame(Duration.ZERO, new KeyValue(opacityProperty(), 50)),
                    new KeyFrame(animationLength, new KeyValue(startXProperty(), 0)),
                    new KeyFrame(animationLength, new KeyValue(startYProperty(), 0)),
                    new KeyFrame(animationLength, new KeyValue(endXProperty(), 0)),
                    new KeyFrame(animationLength, new KeyValue(endYProperty(), 0)),
                    new KeyFrame(animationLength, new KeyValue(opacityProperty(), 0)),
                    new KeyFrame(animationLength, new KeyValue(strokeProperty(), Color.BLUEVIOLET)),
                    new KeyFrame(animationLength, new KeyValue(rotateProperty(), 1080))
            );
        }
    }

    SlashingLines(double startX, double startY, double endX, double endY, Color color) {
        final Slash slash = new Slash(startX, startY, endX, endY, color);
        getChildren().add(slash);
        slash.animation.play();

        Timeline remover = new Timeline(
                new KeyFrame(animationLength, new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        getChildren().remove(slash);
                        slash.animation.stop();
                    }
                })
        );
        remover.play();
    }
}
