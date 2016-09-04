package sample;

import javafx.scene.shape.Circle;
import javafx.scene.paint.*;
/**
 * Created by shyslav on 9/3/16.
 */
public class Ball extends Circle{
    private static double ballRadius = 15;
    private final int lifeTime = 60;
    private int deltaX = 3;
    private int deltaY = 3;
    private long startTime;
    private int sleepTime;
    public Ball(Color fill) {
        super(ballRadius, fill);
        this.startTime = System.currentTimeMillis();
        this.sleepTime = 15;
    }

    public long increaseSleepTime() {
        if(System.currentTimeMillis() - startTime >10000){
            startTime = System.currentTimeMillis();
            sleepTime++;
        }
        return sleepTime;
    }

    public int getSleepTime() {
        return sleepTime;
    }

    public int getDeltaX() {
        return deltaX;
    }

    public void setDeltaX(int deltaX) {
        this.deltaX = deltaX;
    }

    public int getDeltaY() {
        return deltaY;
    }

    public void setDeltaY(int deltaY) {
        this.deltaY = deltaY;
    }
}
