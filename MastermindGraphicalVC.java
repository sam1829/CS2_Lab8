/**
 *  @author Susan Margevich
 */

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;


public class MastermindGraphicalVC extends Application implements Observer {

    // initialize model
    private MastermindModel model;

    // initialize borderpane
    private BorderPane bpane;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        //creating the borderpane and the stage
        bpane = new BorderPane();
        Label message = new Label("Welcome to Mastermind!");
        bpane.setTop(message);
        bpane.setCenter(createCenter());
        bpane.setLeft(createLeft());
        bpane.setRight(createRight());

        //starts the gui doing its thing
        primaryStage.setScene(new Scene(bpane));
        primaryStage.setTitle("Mastermind");
        primaryStage.show();
    }

    /**
     * function to store text that is at the top of the GUI (information as to gameplay)
     * @return label with message
     */
    public Label createTop()
    {
        String message = new String();
        if (model.getVictoryStatus() == true)
        {
            message = "You cracked the code!!!!!!!!";
        }
        else if (model.getRemainingGuesses() <= 10 && model.getRemainingGuesses() > 0)
        {
            message = "You have " + model.getRemainingGuesses() + " guesses remaining.";
        }
        else if (model.getRemainingGuesses() == 0)
        {
            message = "You've run out of guesses!";
        }
        Label done = new Label(message);
        return done;
    }

    /**
     * function that creates the center, mainly including the tiles and playable space
     * @return GridPane including the playable space
     */
    public GridPane createCenter()
    {
        ArrayList<Color> colorlist = new ArrayList<>();
        colorlist.add(Color.GREY);
        colorlist.add(Color.BLACK);
        colorlist.add(Color.WHITE);
        colorlist.add(Color.RED);
        colorlist.add(Color.GREEN);
        colorlist.add(Color.YELLOW);
        colorlist.add(Color.BLUE);

        GridPane playspace = new GridPane();
        //creating integers for gridpane: i1 is rows, i2 is columns
        for (int ans = 0; ans < 4;  ans ++)
        {
            Shape square = new Rectangle(50, 50);
            square.setFill(colorlist.get(model.getSolution().get(ans)));
            playspace.add(square, ans, 0);
        }
        int i1 = 1, i2 = 0;
        while (i1 < 11)
        {
            while (i2 < 4)
            {
                int row = i1;
                int col = i2;
                Shape clickybox = new Rectangle(50, 50);
                clickybox.setFill(colorlist.get(model.getGuessData().get(((row - 1) * 4) + col)));
                clickybox.setOnMouseClicked(e -> model.choose(row, col + 1));
                playspace.add(clickybox, i2, 10 - i1 + 1);
                i2 += 1;
            }
            i2 = 0;
            i1 += 1;
        }
        //make the gui pretty
        playspace.setHgap(5);
        playspace.setVgap(5);
        playspace.setPadding(new Insets(15));
        return playspace;
    }

    /**
     * function that creates the stuff on the left, including the clue grid and information
     * @return GridPane to the left (clue stuff)
     */
    public GridPane createLeft()
    {
        GridPane cluegrid = new GridPane();
        int j1 = 0, j2 = 0;
        while (j1 < 10)
        {
            while (j2 < 4)
            {
                Shape cluecircle = new Circle(10);
                if (((j1 * 4) + j2) < 40)
                {
                    switch (model.getClueData().get((j1 * 4) + j2))
                    {
                        case ' ':
                            cluecircle.setFill(Color.GREY);
                            break;
                        case 'B':
                            cluecircle.setFill(Color.BLACK);
                            break;
                        case 'W':
                            cluecircle.setFill(Color.WHITE);
                            break;
                    }
                }
                cluegrid.add(cluecircle, 4 - j2, 10 - j1);
                j2 += 1;
            }
            j2 = 0;
            j1 += 1;
        }
        cluegrid.setPadding(new Insets(50));
        cluegrid.setHgap(1);
        cluegrid.setVgap(35);
        return cluegrid;
    }

    /**
     * creates buttons on the right and their actions
     * @return returns Vbox of three buttons
     */
    public VBox createRight()
    {
        Button newbutton = new Button("New Game");
        Button peekbutton = new Button("Peek");
        Button guessbutton = new Button("Guess");
        VBox rightbuttons = new VBox(15, newbutton, peekbutton, guessbutton);
        //creates small gap between window edges and vbox containing button
        rightbuttons.setPadding(new Insets(10));
        bpane.setRight(rightbuttons);
        //creates small gap between window edges and bpane label
        bpane.setPadding(new Insets(10));

        newbutton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                model.reset();
            }
        });

        peekbutton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                model.peek();
            }
        });

        guessbutton.setOnAction(e -> model.makeGuess());

        return rightbuttons;
    }

    /**
     * initializes and does startup tasks
     * @throws Exception
     */
    public void init() throws Exception{
        super.init();
        model = new MastermindModel();
        model.addObserver(this);
    }

    /**
     * recreates the GUI and runs all the stuff
     * @param o observable object
     * @param arg argument object
     */
    @Override
    public void update(Observable o, Object arg) {
        bpane.setCenter(createCenter());
        bpane.setRight(createRight());
        bpane.setLeft(createLeft());
        bpane.setTop(createTop());
    }

    /**
     * launches
     * @param args
     */
    public static void main(String[] args) {
        Application.launch(args);
    }
}
