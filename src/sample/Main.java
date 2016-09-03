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
    private static ArrayList<Ball> ballArray;
    private long startTime;
    private static Pane root;
    private static double deltaX = 3;
    private static double deltaY = 3;

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

        Thread tr = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {

                }
                move();
            }
        });
        tr.start();
        scene.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.SPACE){
                addBallToPane();
            }
        });
    }

    private static void addBallToPane() {
        synchronized (ballArray) {
            Ball ball = new Ball(15, Color.BLUE);
            ball.relocate(100, 100);
            ballArray.add(ball);
            root.getChildren().addAll(ball);
        }
    }

    private static void move() {
        //Can use TimeLine, but cant change duration in this class
        synchronized (ballArray) {
            for (int i = 0 ; i < ballArray.size(); i++){
                Ball bal = ballArray.get(i);
                bal.setLayoutX(bal.getLayoutX() + deltaX);
                bal.setLayoutY(bal.getLayoutY() + deltaY);

                final Bounds bounds = root.getBoundsInLocal();
                final boolean atRightBorder = bal.getLayoutX() >= (bounds.getMaxX() - bal.getRadius());
                final boolean atLeftBorder = bal.getLayoutX() <= (bounds.getMinX() + bal.getRadius());
                final boolean atBottomBorder = bal.getLayoutY() >= (bounds.getMaxY() - bal.getRadius());
                final boolean atTopBorder = bal.getLayoutY() <= (bounds.getMinY() + bal.getRadius());

                if (atRightBorder || atLeftBorder) {
                    deltaX *= -1;
                }
                if (atBottomBorder || atTopBorder) {
                    deltaY *= -1;
                }
            }
        }
    }

    public long getDuration() {
        return System.currentTimeMillis() - startTime;
    }

    public static void main(String[] args) throws InterruptedException {
        launch(args);
    }
}
