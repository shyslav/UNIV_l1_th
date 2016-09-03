package sample;

import javafx.scene.shape.Circle;
import javafx.scene.paint.*;
/**
 * Created by shyslav on 9/3/16.
 */
public class Ball extends Circle{
    private final int ballRadius = 20;

    public Ball(double radius, Color fill) {
        super(radius, fill);
    }

}
