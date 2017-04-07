
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;


/**
 * This class helps to enumerate the particular gradients I am creating
 *
 * @author Jason Loux
 */
public class Gradient{
    private int currentGrad = 0;
    LinearGradient currentGradient;
    
    LinearGradient grad1 = new LinearGradient(0f, 1f, 1f, 0f, true, CycleMethod.NO_CYCLE, new Stop[]{
        new Stop(0, Color.web("green")),
        new Stop(0.14, Color.web("turquoise")),
        new Stop(0.28, Color.web("violet")),
        new Stop(0.57, Color.web("yellow")),
        new Stop(0.71, Color.web("hotpink")),
        new Stop(1, Color.web("maroon")),});
    
    Gradient(LinearGradient newGradient){
        currentGrad += currentGrad;
    }
    

    
}