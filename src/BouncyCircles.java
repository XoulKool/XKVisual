
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
    
    BouncyCircles(Group group){
        
        Group circles = new Group();
        for (int i = 0; i < 100; i++) {
            Circle circle = new Circle(25, Color.web("white", 0.05));
            circle.setStrokeType(StrokeType.OUTSIDE);
            circle.setStroke(Color.web("white", 0.16));
            circle.setStrokeWidth(4);
            circle.setCenterX(500);
            circle.setCenterY(300);
            circles.getChildren().add(circle);
        }
        
        Timeline timeline = new Timeline();
         for (Node circle : circles.getChildren()) {
         timeline.getKeyFrames().addAll(
         new KeyFrame(Duration.ZERO, // set start position at 0
         new KeyValue(circle.translateXProperty(), 0),
         new KeyValue(circle.translateYProperty(), 0)
         ),
         new KeyFrame(new Duration(2000), // set end position at 40s
         new KeyValue(circle.translateXProperty(), random() * 1000),
         new KeyValue(circle.translateYProperty(), random() * 1000)
         )
         );
         }
         
         group.getChildren().add(this)
        
    }
    
}
