
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 * @author Jason Loux
 */
public class ConcentricGenerator extends Group {

    /**
     * This class provides a single circle with radius determined by the number
     * passed by the main method. The animation associated with one ripple
     * shrinks the circle.
     */
    private class Ripple extends Circle {

        Timeline animation; //The animation associated with each Ripple

        /**
         * Instantiation of the class requires a point in the pane to display
         * it as well as the starting size of the ripple. The instantiation then
         * takes care of the size, color, and opacity of the ring on the outer
         * part of the circle.
         *
         * @param centerX
         * @param centerY
         * @param radius
         */
        private Ripple(double centerX, double centerY, double radius) {
            super(centerX, centerY, 0, null);
            setStroke(Color.web("white", .5));
            setStrokeWidth(2);
            animation = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(radiusProperty(), radius)),
                    new KeyFrame(Duration.seconds(1), new KeyValue(opacityProperty(), 100)),
                    new KeyFrame(Duration.seconds(5), new KeyValue(radiusProperty(), 0)),
                    new KeyFrame(Duration.seconds(5), new KeyValue(opacityProperty(), 0))
            );
        }
    }

    //Variables to tell where to generate in regards to the pane
    private double generatorCenterX = 100.0;
    private double generatorCenterY = 100.0;

    /**
     * The instantiation of the constructor for this class makes the necessary
     * calls to instantiate a ripple animation.  A new Timeline animation is 
     * also created to remove the ripple animation after a certain period of
     * time.  This prevents the application from getting flooded with 
     * thousands of useless animation threads.
     * 
     * @param radius
     */
    public ConcentricGenerator(double radius) {
        final Ripple ripple = new Ripple(generatorCenterX, generatorCenterY, radius);
        getChildren().add(ripple);
        ripple.animation.play();

        Timeline remover = new Timeline(
                new KeyFrame(Duration.seconds(5), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        getChildren().remove(ripple);
                        ripple.animation.stop();
                    }
                })
        );
        remover.play();
    }
}
