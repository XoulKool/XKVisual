
import static java.lang.Math.random;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeType;
import javafx.util.Duration;

/**
 * This class provides an animation of rotating lines whose length and appearance
 * corresponds to the bass of the music.
 *
 * @author Jason Loux
 */
public class SlashingLines extends Group{

    private final Duration animationLength = Duration.seconds(5);
    private final int numberOfRotations = 4;

    /**
     * This class represents a single line animation which will be added to the
     * overall size of the animation
     */
    private class Slash extends Line {
        
        Timeline animation;
        
        Slash(double x1, double y1, double x2, double y2, Color color) {
            super(0, 0, 0, 0);
            setStroke(color);
            setFill(color);
            setStrokeWidth(20);
            animation = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(startXProperty(), x1)),
                    new KeyFrame(Duration.ZERO, new KeyValue(startYProperty(), y1)),
                    new KeyFrame(Duration.ZERO, new KeyValue(endXProperty(), x2)),
                    new KeyFrame(Duration.ZERO, new KeyValue(endYProperty(), y2)),
                    new KeyFrame(Duration.ZERO, new KeyValue(opacityProperty(), 50)),
                    new KeyFrame(animationLength.divide(2), new KeyValue(opacityProperty(), 100)),
                    new KeyFrame(animationLength, new KeyValue(startXProperty(), 0)),
                    new KeyFrame(animationLength, new KeyValue(startYProperty(), 0)),
                    new KeyFrame(animationLength, new KeyValue(endXProperty(), 0)),
                    new KeyFrame(animationLength, new KeyValue(endYProperty(), 0)),
                    new KeyFrame(animationLength, new KeyValue(opacityProperty(), 50)),
                    new KeyFrame(animationLength, new KeyValue(strokeProperty(), Color.web("white", .5))),
                    new KeyFrame(animationLength, new KeyValue(rotateProperty(), 360* numberOfRotations))
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
