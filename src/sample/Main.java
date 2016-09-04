package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;


public class Main extends Application {
    private volatile static ArrayList<Ball> ballArray;
    private long startTime;
    private static Pane root;
    private Bounds bounds;
    private int currentDelay = 15;
    private boolean game = true;
    int ballID = 1;
    private final ColorFrame colorFrame = new ColorFrame();

    @Override
    public void start(Stage primaryStage) throws Exception {

        ballArray = new ArrayList();
        startTime = System.currentTimeMillis();
        root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        final Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Shyshkin Vladyslav IS-33");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(WindowEvent -> System.exit(0));
        bounds = root.getBoundsInLocal();

        addBallToPane();

        Thread moveThread = new Thread(() -> {
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
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE) {
                addBallToPane();
            }
            if (event.getCode() == KeyCode.ESCAPE) {
                if(!moveThread.isAlive()){
                    System.exit(0);
                }
                game = false;
                printBallMove();
            }
        });
    }

    public void printBallMove() {
        ballArray.forEach(e ->
                System.out.println(colorFrame.get(e.getBallId()-1).getANSI_COLOR()
                        + e.getBallId()
                        + colorFrame.getAnsiReset()
                        + " -> "
                        + e.getAmountMove())
        );
    }

    public void recapture() {
        for (int i = 0; i < ballArray.size(); i++) {
            if (i + 1 < ballArray.size() &&
                    ballArray.get(i).getBoundsInParent().intersects(ballArray.get(i + 1).getBoundsInParent())) {
                rotate(ballArray.get(i), ballArray.get(i + 1));
            }
        }
    }

    public int getTime() {
        if (System.currentTimeMillis() - startTime > 60000) {
            startTime = System.currentTimeMillis();
            currentDelay += 15;
            return currentDelay;
        }
        return currentDelay;
    }

    private void addBallToPane() {
        if (ballID <= colorFrame.size()) {
            Ball ball = new Ball(colorFrame.get(ballID - 1).getColor(),
                    ballID,
                    colorFrame.get(ballID - 1).getANSI_COLOR());
            ballID++;
            ball.relocate(100, 100);
            ballArray.add(ball);
            root.getChildren().addAll(ball);
        }
    }

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

    public static void rotate(Ball b1, Ball b2) {
        b1.setDeltaX(b1.getDeltaX() * -1);
        b1.setDeltaY(b1.getDeltaY() * -1);

        b2.setDeltaX(b1.getDeltaX() * -1);
        b2.setDeltaY(b1.getDeltaY() * -1);
    }


    public static void main(String[] args) throws InterruptedException {
        launch(args);
    }
}
