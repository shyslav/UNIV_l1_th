package lab2.sample;

import commonlib.ColorFrame;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by shyslav on 9/4/16.
 */
public class BallPane extends BorderPane {
    private volatile static ArrayList<Ball> ballArray;
    private Bounds bounds;
    private Rectangle[] rectangle = new Rectangle[4];
    private Pane pane;
    private Label labelScore;
    private int score;

    public BallPane() throws IOException {
        pane = new Pane();
        this.labelScore = new Label("You score: " + 0);
        labelScore.setPadding(new Insets(5,5,5,5));
        labelScore.setTextFill(Color.web("#FFF6FE"));
        this.setCenter(pane);
        this.setBottom(addHBox());
        ballArray = new ArrayList<>();
    }

    private void increaseScore(){
            labelScore.setText("You score: " + (++score));
    }

    public HBox addHBox() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #336699;");

        Button btnBLUE = new Button("ADD BLUE");
        btnBLUE.setPrefSize(100, 20);
        btnBLUE.setOnMouseClicked((e) -> addBallToPane(Color.BLUE));

        Button btnRED = new Button("ADD RED");
        btnRED.setPrefSize(100, 20);
        btnRED.setOnMouseClicked((e) -> addBallToPane(Color.RED));

        Button btnBLACK = new Button("ADD Black");
        btnBLACK.setPrefSize(100, 20);
        btnBLACK.setOnMouseClicked((e) -> addBallToPane(Color.BLACK));

        hbox.getChildren().addAll(btnBLUE, btnRED, btnBLACK,labelScore);

        return hbox;
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

        for (Rectangle aRectangle : rectangle) {
            aRectangle.setArcHeight(100);
            aRectangle.setArcWidth(100);
        }
        int padding = 25;
        rectangle[0].relocate(0 - padding, bounds.getMaxY() - rectangle[0].getHeight() + padding);
        rectangle[1].relocate(bounds.getMaxX() - rectangle[1].getWidth() + padding, bounds.getMaxY() - rectangle[1].getHeight() + padding);
        rectangle[2].relocate(0 - padding, 0 - padding);
        rectangle[3].relocate(bounds.getMaxX() - rectangle[3].getWidth() + padding, 0 - padding);

        pane.getChildren().addAll(rectangle[0], rectangle[1], rectangle[2], rectangle[3]);
    }

    /**
     * Start ball move thread
     */
    public void initializePaneAfterStart() {
        bounds = pane.getBoundsInLocal();
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
            ball.relocate(400, 400);
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
                    pane.getChildren().add(ball);
                        ballArray.add(ball);
                }
        );
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
                    increaseScore();
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
        pane.getChildren().remove(ball);
    }

    /**
     * Move ball
     *
     * @param bal ball to move
     */
    synchronized private void move(Ball bal) {
        if (rectangleRecapture(bal) || recapture(bal)) {
            return;
        }

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
}
