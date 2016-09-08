package lab1.sample;

import commonlib.ColorFrame;
import javafx.geometry.Bounds;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by shyslav on 9/4/16.
 */
public class BallPane extends Pane {
    private final ColorFrame colorFrame = new ColorFrame();
    private volatile static ArrayList<Ball> ballArray;
    private long startTime;
    private Bounds bounds;
    private Thread moveThread;
    private int currentDelay = 15;
    private boolean game = true;
    private int ballID = 1;

    public BallPane() throws IOException {
        ballArray = new ArrayList<>();
        startTime = System.currentTimeMillis();
    }

    /**
     * Start ball move thread
     */
    public void startBallMoving() {
        bounds = super.getBoundsInLocal();
        moveThread = new Thread(() -> {
            while (game) {
                try {
                    Thread.sleep(getTime());
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
                for (int i = 0; i < ballArray.size(); i++) {
                    Ball bal = ballArray.get(i);
                    move(bal);
                    bal.increaseAmountMove();
                }
            }
        });
        moveThread.start();
    }

    /**
     * Get time to slow balls
     *
     * @return time
     */
    public int getTime() {
        if (System.currentTimeMillis() - startTime > 60000) {
            startTime = System.currentTimeMillis();
            currentDelay += 15;
            return currentDelay;
        }
        return currentDelay;
    }

    /**
     * Add ball to pane
     */
    public void addBallToPane() {
        if (ballID <= colorFrame.size()) {
            Ball ball = new Ball(colorFrame.get(ballID - 1).getColor(),
                    ballID,
                    colorFrame.get(ballID - 1).getANSI_COLOR());
            ballID++;
            ball.relocate(100, 100);
            ballArray.add(ball);
            this.getChildren().addAll(ball);
        }
    }

    /**
     * Print all balls move count
     */
    public void printBallMove() {
        ballArray.forEach(e ->
                System.out.println(colorFrame.get(e.getBallId() - 1).getANSI_COLOR()
                        + e.getBallId()
                        + colorFrame.getAnsiReset()
                        + " -> "
                        + e.getAmountMove())
        );
    }

    /**
     * Action to check if ball collided
     */
    private void recapture() {
        for (int i = 0; i < ballArray.size(); i++) {
            if (i + 1 < ballArray.size() &&
                    ballArray.get(i).getBoundsInParent().intersects(ballArray.get(i + 1).getBoundsInParent())) {
                rotate(ballArray.get(i), ballArray.get(i + 1));
            }
        }
    }


    /**
     * Move ball
     *
     * @param bal ball to move
     */
    private void move(Ball bal) {
        recapture();

        bal.setLayoutX(bal.getLayoutX() + bal.getDeltaX());
        bal.setLayoutY(bal.getLayoutY() + bal.getDeltaY());

        final boolean atRightBorder = bal.getLayoutX() >= (bounds.getMaxX() - bal.getRadius());
        final boolean atLeftBorder = bal.getLayoutX() <= (bounds.getMinX() + bal.getRadius());
        final boolean atBottomBorder = bal.getLayoutY() >= (bounds.getMaxY() - bal.getRadius());
        final boolean atTopBorder = bal.getLayoutY() <= (bounds.getMinY() + bal.getRadius());

        if (atRightBorder || atLeftBorder) {
            bal.setDeltaX(bal.getDeltaX() * -1);
        }
        if (atBottomBorder || atTopBorder) {
            bal.setDeltaY(bal.getDeltaY() * -1);
        }
    }

    /**
     * Rotate ball if balls collided
     *
     * @param b1 first ball
     * @param b2 second ball
     */
    private static void rotate(Ball b1, Ball b2) {
        b1.setDeltaX(b1.getDeltaX() * -1);
        b1.setDeltaY(b1.getDeltaY() * -1);
        b2.setDeltaX(b2.getDeltaX() * -1);
        b2.setDeltaY(b2.getDeltaY() * -1);
    }

    /**
     * Check if move thread live
     *
     * @return result of thread live
     */
    public boolean isAlive() {
        return moveThread.isAlive();
    }

    /**
     * Command stop thread
     *
     * @param game command to work or not
     */
    public void setGame(boolean game) {
        this.game = game;
    }

}
