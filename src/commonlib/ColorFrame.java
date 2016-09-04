package commonlib;

import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Created by shyslav on 9/4/16.
 */
public class ColorFrame  extends ArrayList<ColorFrame>{
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";
    private Color color;
    private String ANSI_COLOR;

    public ColorFrame(){
        super.add(new ColorFrame(Color.GREEN,ANSI_GREEN));
        super.add(new ColorFrame(Color.RED,ANSI_RED));
        super.add(new ColorFrame(Color.YELLOW,ANSI_YELLOW));
        super.add(new ColorFrame(Color.BLUE,ANSI_BLUE));
        super.add(new ColorFrame(Color.PURPLE,ANSI_PURPLE));
        super.add(new ColorFrame(Color.CYAN, ANSI_CYAN));
        super.add(new ColorFrame(Color.BLACK,ANSI_BLACK));
    }
    private ColorFrame(Color color, String ANSI_COLOR) {
        this.color = color;
        this.ANSI_COLOR = ANSI_COLOR;
    }

    public Color getColor() {
        return color;
    }

    public String getANSI_COLOR() {
        return ANSI_COLOR;
    }

    public String getAnsiReset() {
        return ANSI_RESET;
    }
}
