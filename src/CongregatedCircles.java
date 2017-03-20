
import static java.lang.Math.random;
import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
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
                new KeyFrame(new Duration(5000), 
                    new KeyValue(super.opacityProperty(), 1)),
                new KeyFrame(new Duration(10000), // set end position at 40s
                        new KeyValue(super.translateXProperty(), (random() - .5) * 500),
                        new KeyValue(super.translateYProperty(), (random() - .5) * 500),
                        new KeyValue(super.opacityProperty(), 0),
                        new KeyValue(super.fillProperty(), Color.DARKSALMON))
        );

        RandomCircle(double x, double y) {
            super(x * 1000, y * 600, 80, Color.web("blue", 0.15));
            super.setStrokeType(StrokeType.OUTSIDE);
            super.setStroke(Color.web("white", 0.75));
            super.setStrokeWidth(4);
        }

    }

    private Group circles = new Group();

    public CongregatedCircles() {
        Timeline animationGenerator = new Timeline(
                new KeyFrame(Duration.seconds(10), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        UsefulFunctions func = new UsefulFunctions();
                        
                        double randomX = random();
                        double randomY = random();
                        animateCircles(randomX, randomY);
                    }
                }
                )
        );

        double randomX = random();
        double randomY = random();
        animationGenerator.setCycleCount(Timeline.INDEFINITE);
        this.animateCircles(randomX, randomY);
        animationGenerator.play();

    }

    public void animateCircles(double x, double y) {

        for (int i = 0; i < 20; i++) {
            RandomCircle randomCircle = new RandomCircle(x, y);
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

    /*public void startPlaying() {
        animationGenerator.play();
    }

    public void stopPlaying() {
        animationGenerator.stop();
    }

    /*EventHandler onFinished = new EventHandler<ActionEvent>() {
         public void handle(ActionEvent t) {
         group.getChildren().remove(blendModeGroup);
         animation(group);
         }
         };*/
}
