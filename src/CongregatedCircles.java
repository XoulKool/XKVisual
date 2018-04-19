
import static java.lang.Math.random;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.util.Duration;

/**
 * Class which can be called to create the Congregated Circles animation
 *
 * @author Jason Loux
 */

public class CongregatedCircles extends Group {

    /**
     * This subclass provides an animation which changes the color, opacity, and
     * position of a particular circles over a specified period of time.
     */
    private final Duration animationLength = Duration.seconds(5); //Length of Animation
    private final int numberOfCircles = 8; //Number of circles at each congregation
    private final int movementMultiplier = 2;

    private class RandomCircle extends Circle {

        Timeline animation;
        
        /**
         * Instantiates a RandomCircle object using the specified parameters.
         * The x & y provided are multiplied by appropriate numbers to populate
         * the circles around the specified pane properly.
         *
         * @param x
         * @param y
         * @param radius
         * @param color
         */
        RandomCircle(double x, double y, double radius, Color color) {
            super(x * 2000, y * 900, radius, color);
            super.setStrokeType(StrokeType.OUTSIDE);
            super.setStroke(Color.web("white", 1));
            super.setStrokeWidth(4);

            animation = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(super.translateXProperty(), 0),
                            new KeyValue(super.translateYProperty(), 0),
                            new KeyValue(super.opacityProperty(), 0)),
                    new KeyFrame(animationLength.divide(2),
                            new KeyValue(super.opacityProperty(), 1)),
                    new KeyFrame(animationLength, // set end position at 40s
                            new KeyValue(super.translateXProperty(), (random() - .5) * radius * movementMultiplier),
                            new KeyValue(super.translateYProperty(), (random() - .5) * radius * movementMultiplier),
                            new KeyValue(super.opacityProperty(), 0),
                            new KeyValue(super.fillProperty(), Color.web("white", 0.7)))
            );
        }

    }

    /**
     * The constructor for Congregated Circles calls the RandomCircle subclass 8
     * different times to create many different circles which emanate from a
     * specific focal point. It also instantiates a remover which cleans up any
     * leftover animation threads left behind by a particular RandomCircle
     * animation.
     *
     * @param radius
     * @param color
     */
    public CongregatedCircles(double radius, Color color) {

        //Have random places where these circles can congregate
        double randomX = random();
        double randomY = random();

        for (int i = 0; i < numberOfCircles; i++) {
            RandomCircle randomCircle = new RandomCircle(randomX, randomY, radius, color);
            getChildren().add(randomCircle);
            randomCircle.animation.play();

            Timeline remover = new Timeline(
                    new KeyFrame(animationLength, new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            getChildren().remove(randomCircle);
                            randomCircle.animation.stop();
                        }
                    })
            );
            remover.play();
        }
    }
}
