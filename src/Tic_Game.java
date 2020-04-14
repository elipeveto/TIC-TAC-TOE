package application;

import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.awt.color.*;

public class Tic_Game extends Application
{
        // Indicate which player has a turn, initially it is the X player
        private char whoseTurn = 'X';

        // Create and initialize cell
        private Cell[][] cell =  new Cell[3][3];

        // Create and initialize a status label
        private Label lblStatus = new Label("Cat's turn to play");

        int xWins;
        int oWins;
        private Label wins = new Label("Cat Wins: " + xWins + "  Dog Wins: " + oWins);
        private Label trashTalk = new Label("");

        public void start(Stage primaryStage) throws FileNotFoundException {
            winAnimation(primaryStage);
            titleScreen(primaryStage);
        }

    private void titleScreen(Stage primaryStage) throws FileNotFoundException
    {
        BorderPane borderPane = new BorderPane();

        Label instructions = new Label("Welcome to Tic Tac Toe!! Please choose the settings you wish to play with below!"); ///////////////////////////////////////////// FROM HERE
        instructions.setTextFill(Color.ORANGE);//sets the options label to .
        instructions.setFont(Font.font("", FontWeight.BOLD, 20));//makes options bold and size 20.
        borderPane.setTop(instructions);
        BorderPane.setAlignment(instructions, Pos.CENTER); 

        VBox levels = new VBox(20);
        levels.setPadding(new Insets(10,0,0,0));
        HBox row1 = new HBox(10);
        row1.setAlignment(Pos.CENTER);
        HBox row2 = new HBox(10);
        row2.setAlignment(Pos.CENTER);
        HBox row3 = new HBox(10);
        row3.setAlignment(Pos.CENTER);
        levels.getChildren().addAll(row1, row2, row3);
        row3.setPadding(new Insets(30,0,0,0));

        ToggleGroup numberOfPlayers = new ToggleGroup();
        RadioButton zero = new RadioButton("0 players");
        zero.setTextFill(Color.YELLOW);//sets the options label to yellow.
        zero.setFont(Font.font("", FontWeight.BOLD, 15));//makes options bold and size 15.

        RadioButton one = new RadioButton("1 players");
        one.setTextFill(Color.YELLOW);//sets the options label to yellow.
        one.setFont(Font.font("", FontWeight.BOLD, 15));//makes options bold and size 15.

        RadioButton two = new RadioButton("2 players");
        two.setTextFill(Color.YELLOW);//sets the options label to yellow.
        two.setFont(Font.font("", FontWeight.BOLD, 15));//makes options bold and size 15.

        Label difficulty = new Label("Choose the AI's Difficulty: ");
        difficulty.setTextFill(Color.BROWN);//sets the options label to
        difficulty.setFont(Font.font("", FontWeight.BOLD, 15));

        Button beginner = new Button("Beginner");
        beginner.setTextFill(Color.GREEN);//sets the options label to
        beginner.setFont(Font.font("", FontWeight.BOLD, 15));

        Button intermediate = new Button("Intermediate");
        intermediate.setTextFill(Color.ORANGE);//sets the options label to
        intermediate.setFont(Font.font("", FontWeight.BOLD, 15));

        Button impossible = new Button("Impossible");
        impossible.setTextFill(Color.RED);//sets the options label to
        impossible.setFont(Font.font("", FontWeight.BOLD, 15));
        
        row1.getChildren().addAll(zero,one,two);
        row2.getChildren().addAll(difficulty,beginner, intermediate, impossible);

        Button start = new Button("START");
        start.setScaleX(2);
        start.setScaleY(2);
        
        row3.getChildren().addAll(start);

        HBox avatars = new HBox(10);
        Label l = new Label("Chose an avatar!");
        l.setBackground(new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)));
        l.setTextFill(Color.RED);//sets the options label to
        l.setFont(Font.font("", FontWeight.BOLD, 25));
        avatars.setAlignment(Pos.CENTER);
        ImageView avatar1 = new ImageView(new Image(new FileInputStream("Pictures/avatar1.jpg")));
        ImageView avatar2 = new ImageView(new Image(new FileInputStream("Pictures/avatar1.jpg")));
        ImageView avatar3 = new ImageView(new Image(new FileInputStream("Pictures/avatar1.jpg")));
        ImageView avatar4 = new ImageView(new Image(new FileInputStream("Pictures/avatar1.jpg")));
        ImageView avatar5 = new ImageView(new Image(new FileInputStream("Pictures/avatar1.jpg")));
        ImageView avatar6 = new ImageView(new Image(new FileInputStream("Pictures/avatar1.jpg")));
        avatar1.setFitWidth(80);
        avatar1.setFitHeight(80);
        avatar2.setFitWidth(80);
        avatar2.setFitHeight(80);
        avatar3.setFitWidth(80);
        avatar3.setFitHeight(80);
        avatar4.setFitWidth(80);
        avatar4.setFitHeight(80);
        avatar5.setFitWidth(80);
        avatar5.setFitHeight(80);
        avatar6.setFitWidth(80);
        avatar6.setFitHeight(80);
        avatars.getChildren().addAll(l, avatar1,avatar2,avatar3,avatar4,avatar5,avatar6);
        borderPane.setBottom(avatars);
        BorderPane.setMargin(avatars, new Insets(0,0,10,0)); /////////////////////////////////////////////////////////////////////////////  TO HERE DANIEL 4/14/20 4:00 PM


        zero.setToggleGroup(numberOfPlayers);
        one.setToggleGroup(numberOfPlayers);
        two.setToggleGroup(numberOfPlayers);

        borderPane.setCenter(levels);

        borderPane.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        Scene scene = new Scene(borderPane, 800, 800);
        primaryStage.setTitle("TicTacToe"); // Set the stage title
        primaryStage.setMaxWidth(800);
        primaryStage.setMinWidth(800);
        primaryStage.setMaxHeight(800);
        primaryStage.setMinHeight(800);
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage

        start.setOnAction(event -> {
            primaryStage.setScene(null);
            try {
                game(primaryStage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
    }
    private void game(Stage primaryStage) throws FileNotFoundException
    {

        // Pane to hold cell
        GridPane pane = new GridPane();
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                pane.add(cell[x][y] = new Cell(primaryStage), y, x);
                //GridPane.setHalignment(cell[x][y], HPos.CENTER);
                //GridPane.setValignment(cell[x][y], VPos.CENTER);
            }
        }
        

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(pane);
        lblStatus.setTextFill(Color.WHITE);//sets the options label to white.
        lblStatus.setFont(Font.font("", FontWeight.BOLD, 30));//makes options bold and size 15.

        HBox top = new HBox(10);
        Button newGame = new Button("New Game");
        wins.setTextFill(Color.WHITE);//sets the options label to white.
        wins.setFont(Font.font("", FontWeight.BOLD, 20));
        trashTalk.setTextFill(Color.PINK);//sets the options label to white.
        trashTalk.setFont(Font.font("", FontWeight.BOLD, 20));
        top.getChildren().addAll(newGame, wins,trashTalk);
        borderPane.setTop(top);

        borderPane.setBottom(lblStatus);
        // Create a scene and place it in the stage
        borderPane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        Scene scene = new Scene(borderPane, 800, 800);
        primaryStage.setTitle("TicTacToe"); // Set the stage title
        primaryStage.setMaxWidth(800);
        primaryStage.setMinWidth(800);
        primaryStage.setMaxHeight(800);
        primaryStage.setMinHeight(800);
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage

        newGame.setOnAction(event -> {
            primaryStage.setScene(null);
            try {

                game(primaryStage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
    }
    public boolean isFull()
        {
            for (int i = 0; i < 3; i++)
                for (int j = 0; j < 3; j++)
                    if (cell[i][j].getToken() == ' ')
                        return false;

            return true;
        }

        /** Determine if the player with the specified token wins */
        public boolean isWon(char token)
        {
            for (int i = 0; i < 3; i++)
                if (cell[i][0].getToken() == token
                        && cell[i][1].getToken() == token
                        && cell[i][2].getToken() == token) {
                    return true;
                }

            for (int j = 0; j < 3; j++)
                if (cell[0][j].getToken() ==  token
                        && cell[1][j].getToken() == token
                        && cell[2][j].getToken() == token) {
                    return true;
                }

            if (cell[0][0].getToken() == token
                    && cell[1][1].getToken() == token
                    && cell[2][2].getToken() == token) {
                return true;
            }

            if (cell[0][2].getToken() == token
                    && cell[1][1].getToken() == token
                    && cell[2][0].getToken() == token) {
                return true;
            }

            return false;
        }

        // An inner class for a cell
        public class Cell extends Pane
        {
            // Token used for this cell
            private char token = ' ';

            public Cell(Stage primaryStage)
            {
                setStyle("-fx-border-color: RED");
                this.setPrefSize(800, 800);
                this.setOnMouseClicked(e -> {
                    try {
                        handleMouseClick(primaryStage);
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }
                });
            }

            /** Return token */
            public char getToken()
            {
                return token;
            }

            /** Set a new token */
            public void setToken(char c, Stage primaryStage) throws FileNotFoundException {
                token = c;

                if (token == 'X')
                {
                    Pane catX = new Pane();
                    ImageView cat = new ImageView(new Image(new FileInputStream("Pictures/X.jpg")));
                    cat.fitHeightProperty().bind(catX.heightProperty());
                    cat.fitWidthProperty().bind(catX.heightProperty());
                    catX.getChildren().add(cat);
                    catX.setPrefSize(primaryStage.getWidth()/4, primaryStage.getHeight()/4);
                    //catX.setPadding(new Insets(10,10,10,10));
                    this.getChildren().add(catX);
                }
                else if (token == 'O')
                {
                    Pane dogX = new Pane();
                    ImageView dog = new ImageView(new Image(new FileInputStream("Pictures/O.jpg")));
                    dog.fitHeightProperty().bind(dogX.heightProperty());
                    dog.fitWidthProperty().bind(dogX.heightProperty());
                    dogX.getChildren().add(dog);
                    dogX.setPrefSize(primaryStage.getWidth()/4, primaryStage.getHeight()/4);
                    this.getChildren().add(dogX);
                }
            }

            /* Handle a mouse click event */
            private void handleMouseClick(Stage primaryStage) throws FileNotFoundException {
                // If cell is empty and game is not over
                if (token == ' ' && whoseTurn != ' ') {
                    setToken(whoseTurn, primaryStage); // Set token in the cell

                    // Check game status
                    if (isWon(whoseTurn)) {
                        if(whoseTurn == 'X')
                        {
                            lblStatus.setText("Cats won! The game is over");
                            wins.setText("Cat Wins: " + ++xWins + "  Dog Wins: " + oWins);
                        }
                        if(whoseTurn == 'O')
                        {
                            lblStatus.setText("Dogs won! The game is over");
                            wins.setText("Cat Wins: " + xWins + "  Dog Wins: " + ++oWins);
                        }
                        whoseTurn = ' '; // Game is over
                    }
                    else if (isFull()) {
                        lblStatus.setText("Draw! The game is over");
                        whoseTurn = ' '; // Game is over
                    }
                    else {
                        // Change the turn
                        whoseTurn = (whoseTurn == 'X') ? 'O' : 'X';
                        // Display whose turn
                        theTrash();
                        if(whoseTurn == 'X')
                            lblStatus.setText("Cats turn.");
                        if(whoseTurn == 'O')
                            lblStatus.setText("Dogs turn.");
                    }
                }
            }
        }


        private void theTrash()
        {
            int rand = (int)(Math.random() * 6) + 1;
            switch (rand)
            {
                case 1:
                {
                    trashTalk.setText("Wow you call that a move?");
                }break;
                case 2:
                {
                    trashTalk.setText("I saw that from a mile away.");
                }break;
                case 3:
                {
                    trashTalk.setText("How predictable.");
                }break;
                case 4:
                {
                    trashTalk.setText("You insult my intellect with that.");
                } break;
                case 5:
                {
                    trashTalk.setText("You should just give up now.");
                }break;
                case 6:
                {
                    trashTalk.setText("You really need to practice more.");
                }break;
            }

        }
        /*
        get these methods below to work.
         */
    private void winAnimation(Stage primaryStage) throws FileNotFoundException
    {
        Label win = new Label("Congradulations!!");
        win.setTextFill(Color.YELLOW);//sets the options label to yellow.
        win.setFont(Font.font("", FontWeight.BOLD, 40));
        win.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(5000));

        Node w = win;

        Path path = new Path();//Defines a Path for the PathTransition
        path.getElements().add(new MoveTo(primaryStage.getWidth()/2,primaryStage.getHeight()/2));//defines where the path goes to.
        path.getElements().add(new LineTo(primaryStage.getWidth(),primaryStage.getHeight()));//defines the line to follow.
        pathTransition.setNode(w);
        pathTransition.setPath(path);
        pathTransition.play();
    }

    private void loseAnimation(Stage primaryStage) throws FileNotFoundException
    {
        Label lose = new Label("Better Luck next Time!!");
        lose.setTextFill(Color.RED);
        lose.setFont(Font.font("", FontWeight.BOLD, 40));
        lose.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

    }

    private void drawAnimation(Stage primaryStage) throws FileNotFoundException
    {
        Label draw = new Label("Looks like a draw folks!!");
        draw.setTextFill(Color.WHITE);
        draw.setFont(Font.font("", FontWeight.BOLD, 40));
        draw.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
    }
    public static void main(String[] args) {
		launch(args);
	}
    }
