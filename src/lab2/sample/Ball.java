package lab2.sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Created by shyslav on 9/3/16.
 */
public class Ball extends Circle{
    private int BallId;
    private static double ballRadius = 15;
    private int deltaX = 3;
    private int deltaY = 3;
    private int amountMove;
    private String ansiColor;
    private boolean game = true;
    private int speed;
    private long startTime;
    public Ball(Color fill, int id, String ansiColor) {
        super(ballRadius, fill);
        amountMove = 0;
        this.BallId = id;
        this.ansiColor = ansiColor;
        this.speed = 15;
        this.startTime = System.currentTimeMillis();
    }

    public boolean isGame() {
        return game;
    }

    public void setGame(boolean game) {
        this.game = game;
    }

    public int getAmountMove() {
        return amountMove;
    }

    public void increaseAmountMove() {
        this.amountMove++;
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

    public int getBallId() {
        return BallId;
    }

    public void setBallId(int ballId) {
        BallId = ballId;
    }

    /**
     * Get time to slow balls
     *
     * @return time
     */
    public int getTime() {
        if (System.currentTimeMillis() - startTime > 60000) {
            startTime = System.currentTimeMillis();
            speed += 15;
            return speed;
        }
        return speed;
    }
}
