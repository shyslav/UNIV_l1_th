package lab2.sample;

import commonlib.ColorFrame;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by shyslav on 9/9/16.
 */
public class BallGamePane extends Pane {
    private Bounds bounds;
    private final Rectangle[] rectangle;
    private volatile ArrayList<Ball> ballArray;
    private final Random random;


    public BallGamePane() {
        ballArray = new ArrayList<>();
        this.rectangle = new Rectangle[4];
        this.random = new Random();
    }

    /**
     * Start ball move thread
     */
    public void initializePaneAfterStart() {
        bounds = this.getBoundsInLocal();
        initializeRectangles();
    }

    /**
     * Initialize rectangles
     */
    private void initializeRectangles() {
        //buttom left
        rectangle[0] = new Rectangle(100, 100, Color.RED);
        //buttom right
        rectangle[1] = new Rectangle(100, 100, Color.RED);
        //top left
        rectangle[2] = new Rectangle(100, 100, Color.RED);
        //top right
        rectangle[3] = new Rectangle(100, 100, Color.RED);

        for (Rectangle aRectangle : rectangle) {
            aRectangle.setArcHeight(100);
            aRectangle.setArcWidth(100);
        }
        int padding = 25;
        rectangle[0].relocate(0 - padding, bounds.getMaxY() - rectangle[0].getHeight() + padding);
        rectangle[1].relocate(bounds.getMaxX() - rectangle[1].getWidth() + padding, bounds.getMaxY() - rectangle[1].getHeight() + padding);
        rectangle[2].relocate(0 - padding, 0 - padding);
        rectangle[3].relocate(bounds.getMaxX() - rectangle[3].getWidth() + padding, 0 - padding);

        this.getChildren().addAll(rectangle[0], rectangle[1], rectangle[2], rectangle[3]);
    }


    /**
     * Add ball to pane
     */
    public void addBallToPane(Color color,int x, int y) {
        String ansi;
        if (color == Color.BLUE) {
            ansi = ColorFrame.getAnsiBlue();
        } else if (color == Color.RED) {
            ansi = ColorFrame.getAnsiRed();
        } else {
            ansi = ColorFrame.getAnsiBlack();
        }
        Thread thread = new Thread(() -> {
            Ball ball = new Ball(color,
                    ballArray.size(),
                    ansi);
            if(x == 0 && y == 0) {
                ball.relocate(random.nextInt((int) bounds.getWidth() - 200) + 100, random.nextInt((int) bounds.getHeight() - 200) + 100);
            }else {
                ball.relocate(x,y);
            }
            platformAddBall(ball);
            while (ball.isGame()) {
                move(ball);
                try {
                    Thread.sleep(ball.getTime());
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            }
        });
        if (color == Color.RED) {
            thread.setPriority(6);
        }
        thread.start();
    }

    /**
     * Add ball to platform
     *
     * @param ball current ball
     */
    private void platformAddBall(Ball ball) {
        Platform.runLater(() -> {
                    this.getChildren().add(ball);
                    ballArray.add(ball);
                }
        );
    }


    /**
     * Action to check if ball collided
     */
    private boolean recapture(Ball ball) {
        for (Ball aBallArray : ballArray) {
            if (ball != aBallArray &&
                    ball.getBoundsInParent().intersects(aBallArray.getBoundsInParent()) &&
                    aBallArray.isGame()) {
                rotate(ball, aBallArray);
                return true;
            }
        }
        return false;
    }

    /**
     * Check if ball recapture in box
     *
     * @param ball - current ball
     */
    private boolean rectangleRecapture(Ball ball) {
        for (Rectangle aRectangle : rectangle) {
            if (ball.getBoundsInParent().intersects(aRectangle.getBoundsInParent())) {
                Platform.runLater(() -> {
                    BallPane.increaseScore();
                    System.out.println("recapture with rectangle");
                    removeBall(ball);
                });
                return true;
            }
        }
        return false;
    }

    /**
     * remove ball from pane
     *
     * @param ball ball to remove
     */
    private void removeBall(Ball ball) {
        ball.setGame(false);
        this.getChildren().remove(ball);
    }

    /**
     * Move ball
     *
     * @param bal ball to move
     */
    synchronized private void move(Ball bal) {
        if (rectangleRecapture(bal)) {
            return;
        }
        recapture(bal);
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

        bal.increaseAmountMove();
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

    public ArrayList<Ball> getBallArray() {
        return ballArray;
    }
}
