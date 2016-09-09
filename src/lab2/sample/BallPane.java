package lab2.sample;

import commonlib.ColorFrame;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by shyslav on 9/4/16.
 */
public class BallPane extends Pane {
    private volatile static ArrayList<Ball> ballArray;
    private Bounds bounds;
    private Rectangle[] rectangle = new Rectangle[4];
    private ArrayList<Thread> threadsList = new ArrayList<>();

    public BallPane() throws IOException {
        ballArray = new ArrayList<>();
    }

    private void initializeRectangles() {
        //buttom left
        rectangle[0] = new Rectangle(100, 100, Color.RED);
        //buttom right
        rectangle[1] = new Rectangle(100, 100, Color.RED);
        //top left
        rectangle[2] = new Rectangle(100, 100, Color.RED);
        //top right
        rectangle[3] = new Rectangle(100, 100, Color.RED);

        for (int i = 0; i < rectangle.length; i++) {
            rectangle[i].setArcHeight(100);
            rectangle[i].setArcWidth(100);
        }

        rectangle[0].relocate(0 - 25, bounds.getMaxY() - rectangle[0].getHeight() + 25);
        rectangle[1].relocate(bounds.getMaxX() - rectangle[1].getWidth() + 25, bounds.getMaxY() - rectangle[1].getHeight() + 25);
        rectangle[2].relocate(0 - 25, 0 - 25);
        rectangle[3].relocate(bounds.getMaxX() - rectangle[3].getWidth() + 25, 0 - 25);

        this.getChildren().addAll(rectangle[0], rectangle[1], rectangle[2], rectangle[3]);
    }

    /**
     * Start ball move thread
     */
    public void initializePaneAfterStart() {
        bounds = super.getBoundsInLocal();
        initializeRectangles();
    }


    /**
     * Add ball to pane
     */
    public void addBallToPane(Color color) {
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
            ballArray.add(ball);
            ball.relocate(400, 400);
            platformAddBall(ball);
            while (ball.isGame()) {
                try {
                    Thread.sleep(ball.getTime());
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
                System.out.println("Move - " + ball.getBallId());
                move(ball);
                ball.increaseAmountMove();
            }
        });
        if(color == Color.RED){
            thread.setPriority(6);
        }
        thread.start();
    }

    /**
     * Add ball to platform
     *
     * @param ball
     */
    private void platformAddBall(Ball ball) {
        Platform.runLater(() ->
                this.getChildren().add(ball));
    }

    /**
     * Print all balls move count
     */
    public void printBallMove() {
        ballArray.forEach(e ->
                System.out.println(e.getAnsiColor()
                        + e.getBallId()
                        + ColorFrame.getAnsiReset()
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
     * Check if ball recapture in box
     *
     * @param ball
     */
    private void rectangleRecapture(Ball ball) {
        for (int i = 0; i < rectangle.length; i++) {
            if (ball.getBoundsInParent().intersects(rectangle[i].getBoundsInParent())) {
                Platform.runLater(() -> {
                    System.out.println("recapture with rectangle");
                    removeBall(ball);
                });
                return;
            }
        }
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
    private void move(Ball bal) {
        rectangleRecapture(bal);
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
    public boolean isAlive(int id) {
        return threadsList.get(id).isAlive();
    }

    /**
     * Command stop thread
     *
     * @param game command to work or not
     */
    public void setGame(Ball game) {
        game.setGame(false);
    }

}
