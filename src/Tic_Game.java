import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

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

        public boolean stop = false;
        public boolean won = false;
        public void start(Stage primaryStage) throws FileNotFoundException {
            winAnimation(primaryStage);
            titleScreen(primaryStage);
        }
//How to commit
    private void titleScreen(Stage primaryStage) throws FileNotFoundException
    {
        BorderPane borderPane = new BorderPane();

        Label instructions = new Label("Welcome to Tic Tac Toe!! Please choose the settings you wish to play with below!"); ///////////////////////////////////////////// Editted from here
        instructions.setTextFill(Color.ORANGE);//sets the options label to .
        instructions.setFont(Font.font("", FontWeight.BOLD, 20));//makes options bold and size 20.
        borderPane.setTop(instructions);
        BorderPane.setAlignment(instructions, Pos.CENTER);

        VBox levels = new VBox(50);
        levels.setPadding(new Insets(10,0,0,0));
        HBox gameModeRow = new HBox(10);
        gameModeRow.setAlignment(Pos.CENTER);
        HBox startRow = new HBox(10);
        startRow.setAlignment(Pos.CENTER);
        startRow.setPadding(new Insets(0,0,0,0));
        //borderPane.setBottom(startRow);

        VBox automatedGame = new VBox(20);
        VBox autoVSHuman = new VBox(20);
        VBox humanVSHuman = new VBox(20);

        levels.getChildren().addAll(gameModeRow, startRow); // start with autoVSHuman selected

        ToggleGroup numberOfPlayers = new ToggleGroup();
        RadioButton zero = new RadioButton("0 players");
        zero.setTextFill(Color.YELLOW);//sets the options label to yellow.
        zero.setFont(Font.font("", FontWeight.BOLD, 15));//makes options bold and size 15.

        zero.selectedProperty().addListener(new ChangeListener<Boolean>() { // Listener to change the selections to fit 0 players
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
                if (isNowSelected)
                    levels.getChildren().add(automatedGame);
                else if (wasPreviouslySelected)
                    levels.getChildren().remove(automatedGame);
            }
        });

        RadioButton one = new RadioButton("1 players");
        one.setTextFill(Color.YELLOW);//sets the options label to yellow.
        one.setFont(Font.font("", FontWeight.BOLD, 15));//makes options bold and size 15.

        one.selectedProperty().addListener(new ChangeListener<Boolean>() { // Listener to change the selections to fit 1 players
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
                if (isNowSelected)
                    levels.getChildren().add(autoVSHuman);
                else if (wasPreviouslySelected)
                    levels.getChildren().remove(autoVSHuman);
            }
        });

        RadioButton two = new RadioButton("2 players");
        two.setTextFill(Color.YELLOW);//sets the options label to yellow.
        two.setFont(Font.font("", FontWeight.BOLD, 15));//makes options bold and size 15.

        two.selectedProperty().addListener(new ChangeListener<Boolean>() { // Listener to change the selections to fit 2 players
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
                if (isNowSelected)
                    levels.getChildren().add(humanVSHuman);
                else if (wasPreviouslySelected)
                    levels.getChildren().remove(humanVSHuman);
            }
        });


        zero.setToggleGroup(numberOfPlayers);
        one.setToggleGroup(numberOfPlayers);
        two.setToggleGroup(numberOfPlayers);
        numberOfPlayers.selectToggle(one);

        gameModeRow.getChildren().addAll(zero,one,two);

        Button start = new Button("START");
        start.setScaleX(2);
        start.setScaleY(2);

        startRow.getChildren().addAll(start);
        // autoVSHuman
        HBox AISelection1 = new HBox(10);
        AISelection1.setAlignment(Pos.CENTER);
        HBox AISelection2 = new HBox(10);
        AISelection2.setAlignment(Pos.CENTER);

        automatedGame.getChildren().addAll(AISelection1, AISelection2);

        // AI One
        Label difficulty1 = new Label("Choose the Cat \"X\" AI's Difficulty: ");
        difficulty1.setTextFill(Color.BROWN);//sets the options label to
        difficulty1.setFont(Font.font("", FontWeight.BOLD, 15));

        ToggleGroup setAISettings1 = new ToggleGroup();

        RadioButton beginner1 = new RadioButton("Beginner");
        beginner1.setTextFill(Color.GREEN);//sets the options label to
        beginner1.setFont(Font.font("", FontWeight.BOLD, 15));
        beginner1.setToggleGroup(setAISettings1);

        RadioButton intermediate1 = new RadioButton("Intermediate");
        intermediate1.setTextFill(Color.ORANGE);//sets the options label to
        intermediate1.setFont(Font.font("", FontWeight.BOLD, 15));
        intermediate1.setToggleGroup(setAISettings1);

        RadioButton impossible1 = new RadioButton("Impossible");
        impossible1.setTextFill(Color.BROWN);//sets the options label to
        impossible1.setFont(Font.font("", FontWeight.BOLD, 15));
        impossible1.setToggleGroup(setAISettings1);

        setAISettings1.selectToggle(beginner1);

        AISelection1.getChildren().addAll(difficulty1, beginner1, intermediate1, impossible1);

        // AI One
        Label difficulty2 = new Label("Choose the Dog \"0\" AI's Difficulty: ");
        difficulty2.setTextFill(Color.BROWN);//sets the options label to
        difficulty2.setFont(Font.font("", FontWeight.BOLD, 15));

        ToggleGroup setAISettings2 = new ToggleGroup();

        RadioButton beginner2 = new RadioButton("Beginner");
        beginner2.setTextFill(Color.GREEN);//sets the options label to
        beginner2.setFont(Font.font("", FontWeight.BOLD, 15));
        beginner2.setToggleGroup(setAISettings2);

        RadioButton intermediate2 = new RadioButton("Intermediate");
        intermediate2.setTextFill(Color.ORANGE);//sets the options label to
        intermediate2.setFont(Font.font("", FontWeight.BOLD, 15));
        intermediate2.setToggleGroup(setAISettings2);

        RadioButton impossible2 = new RadioButton("Impossible");
        impossible2.setTextFill(Color.BROWN);//sets the options label to
        impossible2.setFont(Font.font("", FontWeight.BOLD, 15));
        impossible2.setToggleGroup(setAISettings2);

        setAISettings2.selectToggle(beginner2);

        AISelection2.getChildren().addAll(difficulty2, beginner2, intermediate2, impossible2);

        // autoVSHuman
        HBox onePlayerAISelection = new HBox(10);
        onePlayerAISelection.setAlignment(Pos.CENTER);
        HBox humanAvatarSelection = new HBox(10);
        humanAvatarSelection.setAlignment(Pos.CENTER);

        autoVSHuman.getChildren().addAll(onePlayerAISelection, humanAvatarSelection);

        // Select AI
        Label difficulty = new Label("Choose the AI's Difficulty: ");
        difficulty.setTextFill(Color.BROWN);//sets the options label to
        difficulty.setFont(Font.font("", FontWeight.BOLD, 15));

        ToggleGroup setAISettings = new ToggleGroup();

        RadioButton beginner = new RadioButton("Beginner");
        beginner.setTextFill(Color.GREEN);//sets the options label to
        beginner.setFont(Font.font("", FontWeight.BOLD, 15));
        beginner.setToggleGroup(setAISettings);

        RadioButton intermediate = new RadioButton("Intermediate");
        intermediate.setTextFill(Color.ORANGE);//sets the options label to
        intermediate.setFont(Font.font("", FontWeight.BOLD, 15));
        intermediate.setToggleGroup(setAISettings);

        RadioButton impossible = new RadioButton("Impossible");
        impossible.setTextFill(Color.BROWN);//sets the options label to
        impossible.setFont(Font.font("", FontWeight.BOLD, 15));
        impossible.setToggleGroup(setAISettings);

        setAISettings.selectToggle(beginner);

        onePlayerAISelection.getChildren().addAll(difficulty, beginner, intermediate, impossible);

        // Avatar Selection
        Label l = new Label("Choose an avatar!");
        l.setBackground(new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)));
        l.setTextFill(Color.RED);//sets the options label to
        l.setFont(Font.font("", FontWeight.BOLD, 25));
        humanAvatarSelection.setAlignment(Pos.CENTER);

        ImageView[] images = {
                new ImageView(new Image(new FileInputStream("Pictures/avatar1.jpg"))),
                new ImageView(new Image(new FileInputStream("Pictures/avatar1.jpg"))),
                new ImageView(new Image(new FileInputStream("Pictures/avatar1.jpg"))),
                new ImageView(new Image(new FileInputStream("Pictures/avatar1.jpg"))),
                new ImageView(new Image(new FileInputStream("Pictures/avatar1.jpg"))),
                new ImageView(new Image(new FileInputStream("Pictures/avatar1.jpg"))) 	};

        VBox[] avatarChoices = new VBox[6];
        RadioButton[] toggleAvatar = new RadioButton[6];
        ToggleGroup pickAvatar = new ToggleGroup();
        for (int i = 0; i < 6; i++) {
            toggleAvatar[i] = new RadioButton("Select " + (i + 1));
            toggleAvatar[i].setToggleGroup(pickAvatar);
            avatarChoices[i] = new VBox(2);
            images[i].setFitWidth(80);
            images[i].setFitHeight(80);
            avatarChoices[i].getChildren().addAll(images[i], toggleAvatar[i]);  // Need to make radio buttons for avatar choices
        }
        pickAvatar.selectToggle(toggleAvatar[0]);
        humanAvatarSelection.getChildren().addAll( l, avatarChoices[0], avatarChoices[1], avatarChoices[2], avatarChoices[3], avatarChoices[4], avatarChoices[5]);
        BorderPane.setMargin(humanAvatarSelection, new Insets(0,0,10,0));

        // humanVSHuman
        HBox humanPlayerSelection1 = new HBox(10);
        humanPlayerSelection1.setAlignment(Pos.CENTER);
        HBox humanPlayerSelection2 = new HBox(10);
        humanPlayerSelection2.setAlignment(Pos.CENTER);

        humanVSHuman.getChildren().addAll(humanPlayerSelection1, humanPlayerSelection2);

        // Avatar Selection 1
        Label l1 = new Label("Choose an avatar!");
        l1.setBackground(new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)));
        l1.setTextFill(Color.RED);//sets the options label to
        l1.setFont(Font.font("", FontWeight.BOLD, 25));
        humanPlayerSelection1.setAlignment(Pos.CENTER);

        ImageView[] images1 = {
                new ImageView(new Image(new FileInputStream("Pictures/avatar1.jpg"))),
                new ImageView(new Image(new FileInputStream("Pictures/avatar1.jpg"))),
                new ImageView(new Image(new FileInputStream("Pictures/avatar1.jpg"))),
                new ImageView(new Image(new FileInputStream("Pictures/avatar1.jpg"))),
                new ImageView(new Image(new FileInputStream("Pictures/avatar1.jpg"))),
                new ImageView(new Image(new FileInputStream("Pictures/avatar1.jpg"))) 	};

        VBox[] avatarChoices1 = new VBox[6];
        RadioButton[] toggleAvatar1 = new RadioButton[6];
        ToggleGroup pickAvatar1 = new ToggleGroup();
        for (int i = 0; i < 6; i++) {
            toggleAvatar1[i] = new RadioButton("Select " + (i + 1));
            toggleAvatar1[i].setToggleGroup(pickAvatar1);
            avatarChoices1[i] = new VBox(2);
            images1[i].setFitWidth(80);
            images1[i].setFitHeight(80);
            avatarChoices1[i].getChildren().addAll(images1[i], toggleAvatar1[i]);  // Need to make radio buttons for avatar choices
        }
        pickAvatar1.selectToggle(toggleAvatar1[0]);
        humanPlayerSelection1.getChildren().addAll( l1, avatarChoices1[0], avatarChoices1[1], avatarChoices1[2], avatarChoices1[3], avatarChoices1[4], avatarChoices1[5]);
        BorderPane.setMargin(humanPlayerSelection1, new Insets(0,0,10,0));

        // Avatar Selection 2
        Label l2 = new Label("Choose an avatar!");
        l2.setBackground(new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)));
        l2.setTextFill(Color.RED);//sets the options label to
        l2.setFont(Font.font("", FontWeight.BOLD, 25));
        humanPlayerSelection2.setAlignment(Pos.CENTER);

        ImageView[] images2 = {
                new ImageView(new Image(new FileInputStream("Pictures/avatar1.jpg"))),
                new ImageView(new Image(new FileInputStream("Pictures/avatar1.jpg"))),
                new ImageView(new Image(new FileInputStream("Pictures/avatar1.jpg"))),
                new ImageView(new Image(new FileInputStream("Pictures/avatar1.jpg"))),
                new ImageView(new Image(new FileInputStream("Pictures/avatar1.jpg"))),
                new ImageView(new Image(new FileInputStream("Pictures/avatar1.jpg"))) 	};

        VBox[] avatarChoices2 = new VBox[6];
        RadioButton[] toggleAvatar2 = new RadioButton[6];
        ToggleGroup pickAvatar2 = new ToggleGroup();
        for (int i = 0; i < 6; i++) {
            toggleAvatar2[i] = new RadioButton("Select " + (i + 1));
            toggleAvatar2[i].setToggleGroup(pickAvatar1);
            avatarChoices2[i] = new VBox(2);
            images2[i].setFitWidth(80);
            images2[i].setFitHeight(80);
            avatarChoices2[i].getChildren().addAll(images2[i], toggleAvatar2[i]);  // Need to make radio buttons for avatar choices
        }
        pickAvatar2.selectToggle(toggleAvatar2[0]);
        humanPlayerSelection2.getChildren().addAll( l2, avatarChoices2[0], avatarChoices2[1], avatarChoices2[2], avatarChoices2[3], avatarChoices2[4], avatarChoices2[5]);
        BorderPane.setMargin(humanPlayerSelection2, new Insets(0,0,10,0));


        /////////////////////////////////////////////////////////////////////////////  TO HERE DANIEL 4/18/20 4:40 PM

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
        for (int x = 0; x < 3; x++)
            for (int y = 0; y < 3; y++)
                pane.add(cell[x][y] = new Cell(primaryStage), y, x);

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
                whoseTurn = 'X';
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
                this.setPrefSize(800, 850);
                this.setOnMouseClicked(e -> {
                    try {
                        handleMouseClick(primaryStage);
                        if(whoseTurn == 'O') {
                            AI(primaryStage, token);
                        }
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

                    whoseTurn(primaryStage);

                }
            }

            public void whoseTurn(Stage primaryStage) throws FileNotFoundException {
                // Check game status
                if (isWon(whoseTurn))
                {
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
                else
                    {
                    // Change the turn
                    if(whoseTurn == 'O')
                        whoseTurn = 'X';
                    else if(whoseTurn == 'X')
                        whoseTurn = 'O';

                    // Display whose turn
                    theTrash();
                    if(whoseTurn == 'X')
                        lblStatus.setText("Cats turn.");
                    else if(whoseTurn == 'O')
                    {
                        lblStatus.setText("Dogs turn.");

                    }
                }
            }

            private void AI(Stage primaryStage, char token) throws FileNotFoundException
            {
                    stop = false;
                    won = false;
                    if (whoseTurn != ' ')
                        AIWinCheck(primaryStage, token);


                    if (whoseTurn != ' ' && won == false)
                    {
                    AIStopCheck(primaryStage, token);
                    if(stop == false) {
                        AINormalMove(primaryStage, token);
                    }
                    }
            }

            private void AINormalMove(Stage primaryStage, char token) throws FileNotFoundException
            {
                System.out.println("NORMAL");
                if(token == 'O')
                token = 'X';
                else if(token == 'X')
                token = 'O';


                char topCenter = (cell[0][1].getToken());
                char leftCenter = (cell[1][0].getToken());
                char rightCenter = (cell[1][2].getToken());
                char bottomCenter = (cell[2][1].getToken());

                char topLeft = (cell[0][0].getToken());
                char topRight = (cell[0][2].getToken());
                char bottomLeft = (cell[2][0].getToken());
                char bottomRight = (cell[2][2].getToken());

                System.out.println("Left " + leftCenter + " Right " + rightCenter);
                if(cell[1][1].getToken() == ' ')//first move center if possible
                {
                    cell[1][1].setToken(token, primaryStage);
                    whoseTurn(primaryStage);
                }

                //if the center was used and it has not taken the top bottom left or right center spots then go with one of those.
                else if(cell[1][1].getToken() == token && true == (token != topCenter && token != leftCenter && token != rightCenter && token != bottomCenter))
                {
                    if(topCenter == ' ' && bottomCenter == ' ') //if top is free go their and stop
                    {
                        System.out.println("HEYYY");
                        cell[0][1].setToken(token, primaryStage);
                        whoseTurn(primaryStage);
                    }
                    else if(leftCenter == ' ' && rightCenter == ' ')//if top center is not free go left center and the next 2 else if continue this process to the other side centers.
                    {
                        System.out.println("YYYYYYY");
                        cell[1][0].setToken(token, primaryStage);
                        whoseTurn(primaryStage);
                    }
                    else if(bottomCenter != ' ' && bottomCenter != token && rightCenter != ' ' && rightCenter != token && bottomRight == ' ')
                    {
                        cell[2][2].setToken(token, primaryStage);
                        whoseTurn(primaryStage);
                    }

                    else if(topLeft == ' ')
                    {
                        System.out.println("NOWWWWWWWWW");

                        cell[0][0].setToken(token, primaryStage);
                        whoseTurn(primaryStage);
                    }
                    else if(leftCenter == ' ')
                    {
                        cell[1][0].setToken(token, primaryStage);
                        whoseTurn(primaryStage);
                    }
                    else if(bottomCenter == ' ')
                    {
                        cell[2][1].setToken(token, primaryStage);
                        whoseTurn(primaryStage);
                    }
                    else if(topCenter == ' ')
                    {
                        cell[0][1].setToken(token, primaryStage);
                        whoseTurn(primaryStage);
                    }

                }
                else if(cell[1][1].getToken() == token && false == (token != topCenter && token != leftCenter && token != rightCenter && token != bottomCenter))
                {


                    if(topLeft == ' ')
                    {
                        cell[0][0].setToken(token, primaryStage);
                        whoseTurn(primaryStage);
                    }
                    else if(topRight == ' ')
                    {
                        cell[0][2].setToken(token, primaryStage);
                        whoseTurn(primaryStage);
                    }
                    else if(bottomLeft == ' ')
                    {
                        cell[2][0].setToken(token, primaryStage);
                        whoseTurn(primaryStage);
                    }
                    else if(bottomRight == ' ')
                    {
                        cell[2][2].setToken(token, primaryStage);
                        whoseTurn(primaryStage);
                    }
                    else if(leftCenter == ' ')
                    {
                        cell[1][0].setToken(token, primaryStage);
                        whoseTurn(primaryStage);
                    }
                    else if(rightCenter == ' ')
                    {
                        cell[1][2].setToken(token, primaryStage);
                        whoseTurn(primaryStage);
                    }
                    else if(topCenter == ' ')
                    {
                        cell[0][1].setToken(token, primaryStage);
                        whoseTurn(primaryStage);
                    }
                    else if(bottomCenter == ' ')
                    {
                        cell[2][1].setToken(token, primaryStage);
                        whoseTurn(primaryStage);
                    }
                }
                else if(cell[1][1].getToken() != token && topLeft != token)
                {
                    cell[0][0].setToken(token, primaryStage);
                    whoseTurn(primaryStage);
                }

                else if(cell[0][0].getToken() == token && bottomRight == ' ' && bottomRight != token)
                {
                    if(topRight == ' ' && topCenter == ' ')
                    {
                        cell[2][0].setToken(token, primaryStage);
                        whoseTurn(primaryStage);
                    }
                    else
                    {
                        cell[0][1].setToken(token, primaryStage);
                        whoseTurn(primaryStage);
                    }
                }
                //System.out.println("WHY" + cell[0][0].getToken() + "_____ " + bottomRight + "== " + token);
            }
            private void AIWinCheck(Stage primaryStage, char token) throws FileNotFoundException
            {

               token = whoseTurn;
                for (int i = 0; i < 3; i++)
                {
                    char one = (cell[i][0].getToken());
                    char two = (cell[i][1].getToken());
                    char three = (cell[i][2].getToken());

                    if(one == token && two == token && (three == ' '))
                    {
                        cell[i][2].setToken(token, primaryStage);
                        whoseTurn(primaryStage);
                        won = true;
                        break;
                    }
                    else if(one == token && two == ' ' && three == token)
                    {
                        cell[i][1].setToken(token, primaryStage);
                        whoseTurn(primaryStage);
                        won = true;
                        break;
                    }
                    else if(one == ' ' && two == token && three == token)
                    {
                        cell[i][0].setToken(token, primaryStage);
                        whoseTurn(primaryStage);
                        won = true;
                        break;
                    }
                }
                    if(won == false)
                    for (int i = 0; i < 3; i++)
                    {

                        char one = (cell[0][i].getToken());
                        char two = (cell[1][i].getToken());
                        char three = (cell[2][i].getToken());

                        if(one == token && two == token && three == ' ')
                        {
                            cell[2][i].setToken(token, primaryStage);
                            whoseTurn(primaryStage);
                            won = true;
                            break;
                        }
                        else if(one == token && two == ' ' && three == token)
                        {
                            cell[1][i].setToken(token, primaryStage);
                            whoseTurn(primaryStage);
                            won = true;
                            break;
                        }
                        else if(one == ' ' && two == token && three == token)
                        {
                            cell[0][i].setToken(token, primaryStage);
                            whoseTurn(primaryStage);
                            won = true;
                            break;
                        }
                    }

                char lCrossOne = (cell[0][0].getToken());
                char lCrossTwo = (cell[1][1].getToken());
                char lCrossThree = (cell[2][2].getToken());

                if(won == false) {
                    if (lCrossOne == token && lCrossTwo == token && lCrossThree == ' ') {
                        cell[2][2].setToken(token, primaryStage);
                        whoseTurn(primaryStage);
                        won = true;
                    } else if (lCrossOne == token && lCrossTwo == ' ' && lCrossThree == token) {
                        cell[1][1].setToken(token, primaryStage);
                        whoseTurn(primaryStage);
                        won = true;
                    } else if (lCrossOne == ' ' && lCrossTwo == token && lCrossThree == token) {
                        cell[0][0].setToken(token, primaryStage);
                        whoseTurn(primaryStage);
                        won = true;
                    }
                }
                char rCrossOne = (cell[0][2].getToken());
                char rCrossTwo = (cell[1][1].getToken());
                char rCrossThree = (cell[2][0].getToken());

                if(won == false) {
                    if (rCrossOne == token && rCrossTwo == token && rCrossThree == ' ') {
                        cell[2][0].setToken(token, primaryStage);
                        whoseTurn(primaryStage);
                        won = true;
                    } else if (lCrossOne == token && lCrossTwo == ' ' && lCrossThree == token) {
                        cell[1][1].setToken(token, primaryStage);
                        whoseTurn(primaryStage);
                        won = true;
                    } else if (lCrossOne == ' ' && lCrossTwo == token && lCrossThree == token) {
                        cell[0][2].setToken(token, primaryStage);
                        whoseTurn(primaryStage);
                        won = true;
                    }
                }
            }

            private void AIStopCheck(Stage primaryStage, char token) throws FileNotFoundException
            {
                //this if and if else swap token to check the opponents tokens on the board
               token = whoseTurn;

                if(token == 'O')
                    token = 'X';
                else if(token == 'X')
                    token = 'O';
                for (int i = 0; i < 3; i++)
                {
                    char one = (cell[i][0].getToken());
                    char two = (cell[i][1].getToken());
                    char three = (cell[i][2].getToken());

                    if(one == token && two == token && (three == ' '))
                    {
                        if(token == 'O')
                            token = 'X';
                        else if(token == 'X')
                            token = 'O';
                        cell[i][2].setToken(token, primaryStage);
                        whoseTurn(primaryStage);
                        stop = true;
                        break;
                    }
                    else if(one == token && two == ' ' && three == token)
                    {
                        if(token == 'O')
                            token = 'X';
                        else if(token == 'X')
                            token = 'O';

                        cell[i][1].setToken(token, primaryStage);
                        whoseTurn(primaryStage);
                        stop = true;

                        break;
                    }
                    else if(one == ' ' && two == token && three == token)
                    {
                        if(token == 'O')
                            token = 'X';
                        else if(token == 'X')
                            token = 'O';

                        cell[i][0].setToken(token, primaryStage);
                        whoseTurn(primaryStage);
                        stop = true;
                        break;
                    }
                }

                if(stop != true)
                    for (int i = 0; i < 3; i++)
                    {

                        char one = (cell[0][i].getToken());
                        char two = (cell[1][i].getToken());
                        char three = (cell[2][i].getToken());

                        if(one == token && two == token && three == ' ')
                        {
                            if(token == 'O')
                                token = 'X';
                            else if(token == 'X')
                                token = 'O';

                            cell[2][i].setToken(token, primaryStage);
                            whoseTurn(primaryStage);
                            stop = true;
                            break;
                        }
                        else if(one == token && two == ' ' && three == token)
                        {
                            if(token == 'O')
                                token = 'X';
                            else if(token == 'X')
                                token = 'O';

                            cell[1][i].setToken(token, primaryStage);
                            whoseTurn(primaryStage);
                            stop = true;
                            break;
                        }
                        else if(one == ' ' && two == token && three == token)
                        {
                            if(token == 'O')
                                token = 'X';
                            else if(token == 'X')
                                token = 'O';

                            cell[0][i].setToken(token, primaryStage);
                            whoseTurn(primaryStage);
                            stop = true;
                            break;
                        }
                    }

                char lCrossOne = (cell[0][0].getToken());
                char lCrossTwo = (cell[1][1].getToken());
                char lCrossThree = (cell[2][2].getToken());

                if(stop != true)
                    if(lCrossOne == token && lCrossTwo == token && lCrossThree == ' ')
                    {
                        if(token == 'O')
                            token = 'X';
                        else if(token == 'X')
                            token = 'O';

                        cell[2][2].setToken(token, primaryStage);
                        whoseTurn(primaryStage);
                        stop = true;
                    }
                    else if(lCrossOne == token && lCrossTwo == ' ' && lCrossThree == token)
                    {
                        if(token == 'O')
                            token = 'X';
                        else if(token == 'X')
                            token = 'O';

                        cell[1][1].setToken(token, primaryStage);
                        whoseTurn(primaryStage);
                        stop = true;
                    }
                    else if(lCrossOne == ' ' && lCrossTwo == token && lCrossThree == token)
                    {
                        if(token == 'O')
                            token = 'X';
                        else if(token == 'X')
                            token = 'O';

                        cell[0][0].setToken(token, primaryStage);
                        whoseTurn(primaryStage);
                        stop = true;
                    }

                char rCrossOne = (cell[0][2].getToken());
                char rCrossTwo = (cell[1][1].getToken());
                char rCrossThree = (cell[2][0].getToken());
                if(stop != true)
                    if(rCrossOne == token && rCrossTwo == token && rCrossThree == ' ')
                    {
                        if(token == 'O')
                            token = 'X';
                        else if(token == 'X')
                            token = 'O';

                        cell[2][0].setToken(token, primaryStage);
                        whoseTurn(primaryStage);
                        stop = true;
                    }
                    else if(rCrossOne == token && rCrossTwo == ' ' && rCrossThree == token)
                    {
                        if(token == 'O')
                            token = 'X';
                        else if(token == 'X')
                            token = 'O';

                        cell[1][1].setToken(token, primaryStage);
                        whoseTurn(primaryStage);
                        stop = true;
                    }
                    else if(rCrossOne == ' ' && rCrossTwo == token && rCrossThree == token)
                    {
                        if(token == 'O')
                            token = 'X';
                        else if(token == 'X')
                            token = 'O';

                        cell[0][2].setToken(token, primaryStage);
                        whoseTurn(primaryStage);
                        stop = true;
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
    }
