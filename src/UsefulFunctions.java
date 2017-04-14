
import java.util.Random;
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

/**
 * This class provides some extra functionality that I decided to leave out of
 * XKViusalUI.java. 
 * 
 * @author Jason Loux
 */

public class UsefulFunctions {
    Random random = new Random();
    int counter = random.nextInt(5) + 1; //A global counter used for modding out the gradients.
    int numberOGrads = 5;
    //Construction of multiple gradients which are used for the program
    
    LinearGradient grad1 = new LinearGradient(0f, 1f, 1f, 0f, true, CycleMethod.NO_CYCLE, new Stop[]{
        new Stop(0, Color.web("green")),
        new Stop(0.14, Color.web("turquoise")),
        new Stop(0.28, Color.web("violet")),
        new Stop(0.57, Color.web("yellow")),
        new Stop(0.71, Color.web("hotpink")),
        new Stop(1, Color.web("maroon")),});

    LinearGradient grad2 = new LinearGradient(0f, 1f, 1f, 0f, true, CycleMethod.NO_CYCLE, new Stop[]{
        new Stop(0, Color.web("LightCoral")),
        new Stop(0.14, Color.web("Cyan")),
        new Stop(0.28, Color.web("violet")),
        new Stop(0.57, Color.web("Crimson")),
        new Stop(0.71, Color.web("FireBrick")),
        new Stop(1, Color.web("DarkRed")),});
    
    LinearGradient grad3 = new LinearGradient(0f, 1f, 1f, 0f, true, CycleMethod.NO_CYCLE, new Stop[]{
        new Stop(0, Color.web("Lime")),
        new Stop(0.14, Color.web("lightGreen")),
        new Stop(0.28, Color.web("green")),
        new Stop(0.57, Color.web("silver")),
        new Stop(0.71, Color.web("lime")),
        new Stop(1, Color.web("forestGreen")),});
    LinearGradient grad4 = new LinearGradient(0f, 1f, 1f, 0f, true, CycleMethod.NO_CYCLE, new Stop[]{
        new Stop(0, Color.web("lightBlue")),
        new Stop(0.14, Color.web("blue")),
        new Stop(0.28, Color.web("cyan")),
        new Stop(0.57, Color.web("indigo")),
        new Stop(0.71, Color.web("teal")),
        new Stop(1, Color.web("aqua")),});
        LinearGradient grad5 = new LinearGradient(0f, 1f, 1f, 0f, true, CycleMethod.NO_CYCLE, new Stop[]{
        new Stop(0, Color.web("silver")),
        new Stop(0.14, Color.web("white")),
        new Stop(0.28, Color.web("gold")),
        new Stop(0.57, Color.web("cyan")),
        new Stop(0.71, Color.web("gold")),
        new Stop(1, Color.web("cyan")),});
    /**
     * This function instantiates an ellipsoidal path based on the following 
     * parameters and returns this path to be used to any Group or animation
     * which utilizes it.
     *
     * @param centerX
     * @param centerY
     * @param radiusX
     * @param radiusY
     * @return 
     */
    
    public Path createEllipsePath(double centerX, double centerY, double radiusX, double radiusY) {
        ArcTo arcTo = new ArcTo();
        arcTo.setX(centerX - radiusX + 1);
        arcTo.setY(centerY - radiusY);
        arcTo.setSweepFlag(false);
        arcTo.setLargeArcFlag(true);
        arcTo.setRadiusX(radiusX);
        arcTo.setRadiusY(radiusY);

        Path path = PathBuilder.create()
                .elements(
                        new MoveTo(centerX - radiusX, centerY - radiusY),
                        arcTo,
                        new ClosePath()) // close 1 px gap.
                .build();
        return path;
    }
    
    /**
     * This function blends all passed Groups, Nodes, and Animations to one of
     * the gradients defined.  It applies these gradients sequentially, and in
     * order of when they are selected.
     * 
     * @param root
     * @param varargs 
     */

    public void blendWithGrad(Group root, Node... varargs) {
        
        Rectangle colors;

        if (counter % numberOGrads == 0) 
            colors = new Rectangle(1600, 1200, grad1);
        else if(counter % numberOGrads == 1)
            colors = new Rectangle(1600, 1200, grad2);
        else if(counter % numberOGrads == 2)
            colors = new Rectangle(1600, 1200, grad3);
        else if(counter % numberOGrads == 3)
            colors = new Rectangle(1600, 1200, grad4);
        else
            colors = new Rectangle(1600, 1200, grad5);

        Node[] newargs = new Node[varargs.length + 2];

        for (int i = 0; i < newargs.length; i++) {
            if (i == 0) {
                newargs[i] = new Rectangle(1600, 1200, Color.BLACK);
            } else if (i < newargs.length - 1) {
                newargs[i] = varargs[i - 1];
            } else {
                newargs[i] = colors;
            }
        }

        Group blendModeGroup = new Group(newargs);
        colors.setBlendMode(BlendMode.OVERLAY);
        root.getChildren().add(blendModeGroup);

        for (int i = 0; i < varargs.length; i++) {
            varargs[i].setEffect(new BoxBlur(3, 3, 3));
        }
        counter++;

    }
    
    /**
     * Applies the createEllipsePath function to any given animation or node
     * 
     * @param node 
     */

    public void circlePath(Node node) {
        Path path = createEllipsePath(1400, 400, 700, 300);
        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(4000));
        pathTransition.setPath(path);
        pathTransition.setNode(node);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setCycleCount(Timeline.INDEFINITE);
        pathTransition.setInterpolator(Interpolator.LINEAR);
        pathTransition.setAutoReverse(false);
        pathTransition.setRate(.25);
        pathTransition.play();
    }

}
