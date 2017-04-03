
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class ConcentricGenerator extends Group {

    private class Ripple extends Circle {

        Timeline animation;

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

    private double generatorCenterX = 100.0;
    private double generatorCenterY = 100.0;

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
    public void setGeneratorCenterX(double generatorCenterX) {
        this.generatorCenterX = generatorCenterX;
    }

    public void setGeneratorCenterY(double generatorCenterY) {
        this.generatorCenterY = generatorCenterY;
    }

}
