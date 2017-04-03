
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

public class CongregatedCircles extends Group {

    private class RandomCircle extends Circle {

        Timeline animation = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(super.translateXProperty(), 0),
                        new KeyValue(super.translateYProperty(), 0),
                        new KeyValue(super.opacityProperty(), 0)),
                new KeyFrame(new Duration(2500), 
                    new KeyValue(super.opacityProperty(), 1)),
                new KeyFrame(new Duration(5000), // set end position at 40s
                        new KeyValue(super.translateXProperty(), (random() - .5) * 50),
                        new KeyValue(super.translateYProperty(), (random() - .5) * 50),
                        new KeyValue(super.opacityProperty(), 0),
                        new KeyValue(super.fillProperty(), Color.WHITE))
        );

        RandomCircle(double x, double y, double radius, Color color) {
            super(x * 1400, y * 600, radius, color);
            super.setStrokeType(StrokeType.OUTSIDE);
            super.setStroke(Color.web("white", 0.4));
            super.setStrokeWidth(4);
        }

    }

    private Group circles = new Group();

    public CongregatedCircles(double radius, Color color) {
        
        double randomX = random();
        double randomY = random();
        
        for (int i = 0; i < 8; i++) {
            RandomCircle randomCircle = new RandomCircle(randomX, randomY, radius, color);
            //setBoundaries(pane, randomCircle);
            getChildren().add(randomCircle);
            randomCircle.animation.play();

            Timeline remover = new Timeline(
                    new KeyFrame(Duration.seconds(10), new EventHandler<ActionEvent>() {
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
