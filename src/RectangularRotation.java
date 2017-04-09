
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 *
 * @author Jason Loux
 */
public class RectangularRotation extends Group {
    
    private final Duration animationLength = Duration.seconds(5);
    private final int numberOfRotations = 4;

    /**
     * This class represents a single line animation which will be added to the
     * overall size of the animation
     */
    private class RotatingRectangle  extends Rectangle {
        
        Timeline animation;
        
        RotatingRectangle(double x, double y, double width, double height, Color color) {
            super(x, y, width, height);
            setStroke(Color.web("white", 0.7));
            setFill(color);
            setStrokeWidth(20);
            animation = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(opacityProperty(), 50)),
                    new KeyFrame(animationLength.divide(2), new KeyValue(opacityProperty(), 100)),
                    new KeyFrame(animationLength, new KeyValue(rotateProperty(), 360 * numberOfRotations)),
                    new KeyFrame(animationLength, new KeyValue(opacityProperty(), 50)),
                    new KeyFrame(animationLength, new KeyValue(widthProperty(), 0)),
                    new KeyFrame(animationLength, new KeyValue(heightProperty(), 0))
            );
        }
    }

    RectangularRotation(double x, double y, double height, double width, Color color) {
        final RotatingRectangle rect = new RotatingRectangle(x, y, width, height, color);
        getChildren().add(rect);
        rect.animation.play();

        Timeline remover = new Timeline(
                new KeyFrame(animationLength, new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        getChildren().remove(rect);
                        rect.animation.stop();
                    }
                })
        );
        remover.play();
    }
}
