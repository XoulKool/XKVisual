
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.BoxBlur;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathBuilder;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class UsefulFunctions {

    public Path createEllipsePath(double centerX, double centerY, double radiusX, double radiusY, double rotate) {
        ArcTo arcTo = new ArcTo();
        arcTo.setX(centerX - radiusX + 1); // to simulate a full 360 degree celcius circle.
        arcTo.setY(centerY - radiusY);
        arcTo.setSweepFlag(false);
        arcTo.setLargeArcFlag(true);
        arcTo.setRadiusX(radiusX);
        arcTo.setRadiusY(radiusY);
        arcTo.setXAxisRotation(rotate);

        Path path = PathBuilder.create()
                .elements(
                        new MoveTo(centerX - radiusX, centerY - radiusY),
                        arcTo,
                        new ClosePath()) // close 1 px gap.
                .build();
        return path;
    }

    public void blendWithGrad(Group root, Node... varargs) {
        LinearGradient grad = new LinearGradient(0f, 1f, 1f, 0f, true, CycleMethod.NO_CYCLE, new Stop[]{
            new Stop(0, Color.web("green")),
            new Stop(0.14, Color.web("turquoise")),
            new Stop(0.28, Color.web("violet")),
            new Stop(0.57, Color.web("yellow")),
            new Stop(0.71, Color.web("hotpink")),
            new Stop(1, Color.web("maroon")),});

        Rectangle colors = new Rectangle(1600, 1200, grad);

        Node[] newargs = new Node[varargs.length + 2];

        for (int i = 0; i < newargs.length; i++) {
            if (i == 0) {
                newargs[i] = new Rectangle(1600, 1200, Color.BLACK);
            }
            else if(i < newargs.length - 1){
                newargs[i] = varargs[i -1];
            }
            else
                newargs[i] = colors;
        }

        Group blendModeGroup
                = new Group(newargs);
        colors.setBlendMode(BlendMode.OVERLAY);
        root.getChildren().add(blendModeGroup);
        
        for(int i = 0; i < varargs.length; i++){
            varargs[i].setEffect(new BoxBlur(3, 3, 3));
        }
        
        
    }

    public void circlePath(Node node) {
        UsefulFunctions func = new UsefulFunctions();
        Path path = func.createEllipsePath(1400, 400, 700, 300, 0);

        //path.getElements().add(new MoveTo(50, 50));
        //path.getElements().add(arcTo);
        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(4000));
        pathTransition.setPath(path);
        pathTransition.setNode(node);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setCycleCount(Timeline.INDEFINITE);
        pathTransition.setInterpolator(Interpolator.LINEAR);
        pathTransition.setAutoReverse(false);
        pathTransition.setRate(.5);
        pathTransition.play();
    }
}
