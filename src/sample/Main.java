package sample;

import javafx.animation.AnimationTimer;
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
    private AnimationTimer timer;
    private static ArrayList<Ball> ballArray;
    private long startTime;
    private static Pane root;

    @Override
    public void start(Stage primaryStage) throws Exception {
        ballArray = new ArrayList();
        startTime = System.currentTimeMillis();
        root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        final Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Shyshkin Vladyslav IS-33");
        primaryStage.setTitle("Game");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(WindowEvent -> System.exit(0));

        addBallToPane();

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                for (Ball bal : ballArray) {
                    try {
                        Thread.sleep(bal.getSleepTime());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    move(bal);
                }
            }
        };
        timer.start();
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE) {
                addBallToPane();
            }
        });
    }

    private static void addBallToPane() {
        Ball ball = new Ball(Color.GREEN);
        ball.relocate(100, 100);
        ballArray.add(ball);
        root.getChildren().addAll(ball);
    }

    private static void move(Ball bal) {
        for (int i = 0; i < ballArray.size(); i++) {
            if (i + 1 < ballArray.size() && ballArray.get(i).getBoundsInParent().intersects(ballArray.get(i + 1).getBoundsInParent())) {
                rotate(ballArray.get(i), ballArray.get(i + 1));
            }
        }
        //Can use TimeLine, but cant change duration in this class
        bal.increaseSleepTime();

        bal.setLayoutX(bal.getLayoutX() + bal.getDeltaX());
        bal.setLayoutY(bal.getLayoutY() + bal.getDeltaY());

        final Bounds bounds = root.getBoundsInLocal();
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

    public long getDuration() {
        return System.currentTimeMillis() - startTime;
    }

    public static void main(String[] args) throws InterruptedException {
        launch(args);
    }
}
