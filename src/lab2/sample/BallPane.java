package lab2.sample;

import commonlib.ColorFrame;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.io.IOException;

/**
 * Created by shyslav on 9/4/16.
 */
public class BallPane extends BorderPane {
    private static Label labelScore;
    private static int score;
    private BallGamePane pane;
    public BallPane() throws IOException {
        pane = new BallGamePane();
        labelScore = new Label("You score: " + 0);
        labelScore.setPadding(new Insets(5, 5, 5, 5));
        labelScore.setTextFill(Color.web("#FFF6FE"));
        this.setCenter(pane);
        this.setBottom(addHBox());
    }

    public static void increaseScore() {
        labelScore.setText("You score: " + (++score));
    }

    public HBox addHBox() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #336699;");

        Button btnBLUE = new Button("ADD BLUE");
        btnBLUE.setPrefSize(100, 20);
        btnBLUE.setOnMouseClicked((e) -> pane.addBallToPane(Color.BLUE,0,0));

        Button btnRED = new Button("ADD RED");
        btnRED.setPrefSize(100, 20);
        btnRED.setOnMouseClicked((e) -> pane.addBallToPane(Color.RED,0,0));

        Button btnBLACK = new Button("ADD Black");
        btnBLACK.setPrefSize(100, 20);
        btnBLACK.setOnMouseClicked((e) -> pane.addBallToPane(Color.BLACK,0,0));

        hbox.getChildren().addAll(btnBLUE, btnRED, btnBLACK, labelScore);

        return hbox;
    }


    /**
     * Print all balls move count
     */
    public void printBallMove() {
        pane.getBallArray().forEach(e ->
                System.out.println(e.getAnsiColor()
                        + e.getBallId()
                        + ColorFrame.getAnsiReset()
                        + " -> "
                        + e.getAmountMove())
        );
    }

    public BallGamePane getPane() {
        return pane;
    }
}
