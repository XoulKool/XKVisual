
import static java.lang.Math.random;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.util.Duration;

public class BouncyCircles extends Group {

    private class RandomCircle extends Circle {

        Timeline animation = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(super.translateXProperty(), 0),
                    new KeyValue(super.translateYProperty(), 0)),
            new KeyFrame(new Duration(2000), // set end position at 40s
                    new KeyValue(super.translateXProperty(), random() * 1000),
                    new KeyValue(super.translateYProperty(), random() * 1000))
        );
        
        RandomCircle(){
            super(300, 500, 25, Color.web("white", 0.05));
            super.setStrokeType(StrokeType.OUTSIDE);
            super.setStroke(Color.web("white", 0.16));
            super.setStrokeWidth(4);
        }
        
        
    }

    private Group circles = new Group();
    
    private Timeline animationGenerator = new Timeline();

    public BouncyCircles() {
        animationGenerator.setCycleCount(Timeline.INDEFINITE);
        this.animateCircles();
        this.startPlaying();

    }

    public void animateCircles() {
        

        for (int i = 0; i < 100; i++) {
            RandomCircle randomCircle = new RandomCircle();
            getChildren().add(randomCircle);
            randomCircle.animation.play();
        }
    }

    public void startPlaying() {
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
