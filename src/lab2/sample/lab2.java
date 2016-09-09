package lab2.sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;


public class lab2 extends Application {
    private BallPane root;

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Shyshkin Vladyslav IS-33");
        primaryStage.setResizable(false);
        primaryStage.setScene(initializeScene());
        primaryStage.show();
        primaryStage.setOnCloseRequest(WindowEvent -> System.exit(0));
        Platform.runLater(()->
        {
            root.addBallToPane(Color.BLACK,0,0);
            root.initializePaneAfterStart();
        });
    }

    private Scene initializeScene() throws IOException {
        root = new BallPane();
        Scene scene = new Scene(root, 800, 600);

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.R) {
                root.addBallToPane(Color.RED,0,0);
            }
            if (event.getCode() == KeyCode.B) {
                root.addBallToPane(Color.BLUE,0,0);
            }
            if (event.getCode() == KeyCode.C) {
                root.addBallToPane(Color.BLACK,0,0);
            }
            if (event.getCode() == KeyCode.ESCAPE) {
                root.printBallMove();
                System.exit(0);
            }
        });
        scene.setOnMouseClicked(event -> {
            root.addBallToPane(Color.BLACK,(int) event.getSceneX(),(int) event.getSceneY());
        });
        return scene;
    }


    public static void main(String[] args) throws InterruptedException {
        launch(args);
    }
}
