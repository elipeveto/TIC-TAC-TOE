import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Tic_Game extends Application
{
    public static void main(String[] args)//launches the program.
    {
        Application.launch(args);
    }

    // Indicate which player has a turn, initially it is the X player
    private char whoseTurn = 'X';

    // Create and initialize cell
    private Cell[][] cell =  new Cell[3][3];

    // Create and initialize a status label
    private Label lblStatus = new Label("Cat's turn to play");

    int xWins; // Keeps track of X wins
    int oWins; // Keeps track of O wins
    private Label wins = new Label("Cat Wins: " + xWins + "  Dog Wins: " + oWins); // Label to display scoreboard
    private Label trashTalk = new Label(""); // Label for AI trash talk

    public boolean stop = false;
    public boolean won = false;

    public int currentDifficulty; // Reference for the current difficulty
    public int currentGameMode;

    // Indicates which game mode the user selects 0, 1, or 2 players
    private int gameMode = 0;

    // This is referenced for the character images
    private String[] charAvatars = {
            "Pictures/avatar1.jpg",
            "Pictures/avatar2.jpg",
            "Pictures/avatar3.jpg",
            "Pictures/avatar4.jpg",
            "Pictures/avatar5.jpg",
            "Pictures/avatar6.jpeg",
    };

    // This is referenced for the AI avatar images based on difficulty level
    private String[] automatedAvatars = {
            "Pictures/beginner.jpg",
            "Pictures/intermediate.jpeg",
            "Pictures/impossible.jpeg",
    };

    // These are the reference URLS for the images for the backgrounds
    private String[] linksBackgrounds = {
            "Pictures/back1.jpg",
            "Pictures/back2.jpg",
            "Pictures/back3.jpg",
            "Pictures/back4.jpg",
            "Pictures/back5.jpg",
            "Pictures/back6.jpg"
    };

    // Used when there are 0 players number correlates to the index of the image
    private int autoAvatar1 = 0;
    private int autoAvatar2 = 0;
    // Used when there is 1 player number correlates to the index of the image
    private int autoAvatar = 0;
    private int humanAvatar = 0;
    // Used when there are 2 players number correlates to the index of the image
    private int humanAvatar1 = 0;
    private int humanAvatar2 = 0;

    private int dif1; // Reference difficulty level
    private int dif2; // Reference difficulty level

    private Label forGameResults = new Label(""); // This is used for win/lose/draw animations

    public void start(Stage primaryStage) throws FileNotFoundException // This is the star method that immediately fires the titleScreen method
    {
        titleScreen(primaryStage);

    }
    private void titleScreen(Stage primaryStage) throws FileNotFoundException // Method that creates the title screen
    {
        BorderPane borderPane = new BorderPane(); // Creates a borderPane (the root pane)

        Label instructions = new Label("Welcome to Tic Tac Toe!! Please choose the settings you wish to play with below!"); // Label with instructions
        instructions.setTextFill(Color.ORANGE);
        instructions.setFont(Font.font("", FontWeight.BOLD, 20)); // makes options bold and size 20.
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

        VBox automatedGame = new VBox(20); // This is the back bone of the automated Game setting menu
        VBox autoVSHuman = new VBox(20); // This is the back bone of the automated versus human Game setting menu
        VBox humanVSHuman = new VBox(20);// This is the back bone of the human versus human Game setting menu

        levels.getChildren().addAll(gameModeRow, startRow); // start with autoVSHuman selected

        ToggleGroup numberOfPlayers = new ToggleGroup(); // Toggle group to select amount of players

        RadioButton zero = new RadioButton("0 players"); // Radio Button for 0 players
        zero.setTextFill(Color.YELLOW);
        zero.setFont(Font.font("", FontWeight.BOLD, 15)); //makes options bold and size 15.

        zero.selectedProperty().addListener(new ChangeListener<Boolean>() { // Listener to change the selections to fit 0 players
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) { // Overrides the changed method
                if (isNowSelected)
                    levels.getChildren().add(automatedGame);
                else if (wasPreviouslySelected)
                    levels.getChildren().remove(automatedGame);
            }
        });

        RadioButton one = new RadioButton("1 players"); // Radio Button for 1 player
        one.setTextFill(Color.YELLOW);
        one.setFont(Font.font("", FontWeight.BOLD, 15));//makes options bold and size 15.

        one.selectedProperty().addListener(new ChangeListener<Boolean>() { // Listener to change the selections to fit 1 players
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) { // Overrides the changed method
                if (isNowSelected)
                    levels.getChildren().add(autoVSHuman);
                else if (wasPreviouslySelected)
                    levels.getChildren().remove(autoVSHuman);
            }
        });

        RadioButton two = new RadioButton("2 players"); // Radio Button for 2 players
        two.setTextFill(Color.YELLOW);
        two.setFont(Font.font("", FontWeight.BOLD, 15)); //makes options bold and size 15.

        two.selectedProperty().addListener(new ChangeListener<Boolean>() { // Listener to change the selections to fit 2 players
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) { // Overrides the changed method
                if (isNowSelected)
                    levels.getChildren().add(humanVSHuman);
                else if (wasPreviouslySelected)
                    levels.getChildren().remove(humanVSHuman);
            }
        });


        zero.setToggleGroup(numberOfPlayers);
        one.setToggleGroup(numberOfPlayers);
        two.setToggleGroup(numberOfPlayers);
        numberOfPlayers.selectToggle(one); // Starts with 1 player

        gameModeRow.getChildren().addAll(zero,one,two); // Adds buttons to pane

        Button start = new Button("START"); // Creates a button to start the game
        start.setScaleX(2);
        start.setScaleY(2);

        startRow.getChildren().addAll(start);

        // autoVSHuman
        HBox AISelection1 = new HBox(10); // Pane for AI 1 settings
        AISelection1.setAlignment(Pos.CENTER);
        HBox AISelection2 = new HBox(10); // Pane for AI 2 settings
        AISelection2.setAlignment(Pos.CENTER);

        automatedGame.getChildren().addAll(AISelection1, AISelection2);

        // AI One
        Label difficulty1 = new Label("Choose the Cat \"X\" AI's Difficulty: "); // Label for directions for user
        difficulty1.setTextFill(Color.BROWN);
        difficulty1.setFont(Font.font("", FontWeight.BOLD, 15));

        ToggleGroup setAISettings1 = new ToggleGroup(); // Toggle Group for AI 1 settings

        RadioButton beginner1 = new RadioButton("Beginner"); // RadioButton to select easy difficulty
        beginner1.setTextFill(Color.GREEN);
        beginner1.setFont(Font.font("", FontWeight.BOLD, 15));
        beginner1.setToggleGroup(setAISettings1);

        RadioButton intermediate1 = new RadioButton("Intermediate"); // RadioButton to select Intermediate difficulty
        intermediate1.setTextFill(Color.ORANGE);
        intermediate1.setFont(Font.font("", FontWeight.BOLD, 15));
        intermediate1.setToggleGroup(setAISettings1);

        RadioButton impossible1 = new RadioButton("Impossible");// RadioButton to select Impossible difficulty
        impossible1.setTextFill(Color.BROWN);
        impossible1.setFont(Font.font("", FontWeight.BOLD, 15));
        impossible1.setToggleGroup(setAISettings1);

        setAISettings1.selectToggle(beginner1);

        AISelection1.getChildren().addAll(difficulty1, beginner1, intermediate1, impossible1);

        // AI One
        Label difficulty2 = new Label("Choose the Dog \"0\" AI's Difficulty: "); // Label for directions for user
        difficulty2.setTextFill(Color.BROWN);
        difficulty2.setFont(Font.font("", FontWeight.BOLD, 15));

        ToggleGroup setAISettings2 = new ToggleGroup(); // Toggle Group for AI 2 settings

        RadioButton beginner2 = new RadioButton("Beginner"); // RadioButton to select easy difficulty
        beginner2.setTextFill(Color.GREEN);
        beginner2.setFont(Font.font("", FontWeight.BOLD, 15));
        beginner2.setToggleGroup(setAISettings2);

        RadioButton intermediate2 = new RadioButton("Intermediate"); // RadioButton to select Intermediate difficulty
        intermediate2.setTextFill(Color.ORANGE);
        intermediate2.setFont(Font.font("", FontWeight.BOLD, 15));
        intermediate2.setToggleGroup(setAISettings2);

        RadioButton impossible2 = new RadioButton("Impossible"); // RadioButton to select Impossible difficulty
        impossible2.setTextFill(Color.BROWN);
        impossible2.setFont(Font.font("", FontWeight.BOLD, 15));
        impossible2.setToggleGroup(setAISettings2);

        setAISettings2.selectToggle(beginner2); // Selects the first toggle because an input must be taken

        AISelection2.getChildren().addAll(difficulty2, beginner2, intermediate2, impossible2);

        // autoVSHuman
        HBox onePlayerAISelection = new HBox(10);
        onePlayerAISelection.setAlignment(Pos.CENTER);
        HBox humanAvatarSelection = new HBox(10);
        humanAvatarSelection.setAlignment(Pos.CENTER);

        autoVSHuman.getChildren().addAll(onePlayerAISelection, humanAvatarSelection);

        // Select AI
        Label difficulty = new Label("Choose the AI's Difficulty: "); // Label for directions for user
        difficulty.setTextFill(Color.BROWN);
        difficulty.setFont(Font.font("", FontWeight.BOLD, 15));

        ToggleGroup setAISettings = new ToggleGroup(); // Toggle Group for AI settings

        RadioButton beginner = new RadioButton("Beginner");  // RadioButton to select easy difficulty
        beginner.setTextFill(Color.GREEN);
        beginner.setFont(Font.font("", FontWeight.BOLD, 15));
        beginner.setToggleGroup(setAISettings);

        RadioButton intermediate = new RadioButton("Intermediate"); // RadioButton to select Intermediate difficulty
        intermediate.setTextFill(Color.ORANGE);
        intermediate.setFont(Font.font("", FontWeight.BOLD, 15));
        intermediate.setToggleGroup(setAISettings);

        RadioButton impossible = new RadioButton("Impossible"); // RadioButton to select Impossible difficulty
        impossible.setTextFill(Color.BROWN);
        impossible.setFont(Font.font("", FontWeight.BOLD, 15));
        impossible.setToggleGroup(setAISettings);

        setAISettings.selectToggle(beginner);

        onePlayerAISelection.getChildren().addAll(difficulty, beginner, intermediate, impossible);

        // Avatar Selection
        Label l = new Label("Choose an avatar!"); // Directs the user to choose an avatar
        l.setBackground(new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY))); // Creates a background for the text
        l.setTextFill(Color.RED);
        l.setFont(Font.font("", FontWeight.BOLD, 25));
        humanAvatarSelection.setAlignment(Pos.CENTER);

        ImageView[] images = {
                new ImageView(new Image(new FileInputStream(charAvatars[0]))), // These are the images displayed for the user to choose from.
                new ImageView(new Image(new FileInputStream(charAvatars[1]))),
                new ImageView(new Image(new FileInputStream(charAvatars[2]))),
                new ImageView(new Image(new FileInputStream(charAvatars[3]))),
                new ImageView(new Image(new FileInputStream(charAvatars[4]))),
                new ImageView(new Image(new FileInputStream(charAvatars[5]))) 	};

        VBox[] avatarChoices = new VBox[6]; // Array of Panes that hold each the avatar image and respective radio button
        RadioButton[] toggleAvatar = new RadioButton[6]; // Array of radio buttons to select the avatar
        ToggleGroup pickAvatar = new ToggleGroup(); // Group of 6 radio buttons
        for (int i = 0; i < 6; i++) {
            toggleAvatar[i] = new RadioButton("Select " + (i + 1)); // Creates radio buttons for each index
            toggleAvatar[i].setToggleGroup(pickAvatar);
            avatarChoices[i] = new VBox(2); // Creates the pane for the radio buttons and image
            images[i].setFitWidth(80);
            images[i].setFitHeight(80);
            avatarChoices[i].getChildren().addAll(images[i], toggleAvatar[i]);
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
        Label l1 = new Label("Choose an avatar!"); // Directs the user to choose an avatar
        l1.setBackground(new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY))); // Creates a background for the text
        l1.setTextFill(Color.RED);
        l1.setFont(Font.font("", FontWeight.BOLD, 25));
        humanPlayerSelection1.setAlignment(Pos.CENTER);

        ImageView[] images1 = {
                new ImageView(new Image(new FileInputStream(charAvatars[0]))), // These are the images displayed for the user to choose from.
                new ImageView(new Image(new FileInputStream(charAvatars[1]))),
                new ImageView(new Image(new FileInputStream(charAvatars[2]))),
                new ImageView(new Image(new FileInputStream(charAvatars[3]))),
                new ImageView(new Image(new FileInputStream(charAvatars[4]))),
                new ImageView(new Image(new FileInputStream(charAvatars[5]))) 	};

        VBox[] avatarChoices1 = new VBox[6]; // Array of Panes that hold each the avatar image and respective radio button
        RadioButton[] toggleAvatar1 = new RadioButton[6]; // Array of radio buttons to select the avatar
        ToggleGroup pickAvatar1 = new ToggleGroup(); // Group of 6 radio buttons
        for (int i = 0; i < 6; i++) {
            toggleAvatar1[i] = new RadioButton("Select " + (i + 1)); // Creates radio buttons for each index
            toggleAvatar1[i].setToggleGroup(pickAvatar1);
            avatarChoices1[i] = new VBox(2); // Creates the pane for the radio buttons and image
            images1[i].setFitWidth(80);
            images1[i].setFitHeight(80);
            avatarChoices1[i].getChildren().addAll(images1[i], toggleAvatar1[i]);
        }
        pickAvatar1.selectToggle(toggleAvatar1[0]);
        humanPlayerSelection1.getChildren().addAll( l1, avatarChoices1[0], avatarChoices1[1], avatarChoices1[2], avatarChoices1[3], avatarChoices1[4], avatarChoices1[5]);
        BorderPane.setMargin(humanPlayerSelection1, new Insets(0,0,10,0));

        // Avatar Selection 2
        Label l2 = new Label("Choose an avatar!"); // Directs the user to choose an avatar
        l2.setBackground(new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY))); // Creates a background for the text
        l2.setTextFill(Color.RED);//sets the options label to
        l2.setFont(Font.font("", FontWeight.BOLD, 25));
        humanPlayerSelection2.setAlignment(Pos.CENTER);

        ImageView[] images2 = {		// These are the images displayed for the user to choose from.
                new ImageView(new Image(new FileInputStream(charAvatars[0]))),
                new ImageView(new Image(new FileInputStream(charAvatars[1]))),
                new ImageView(new Image(new FileInputStream(charAvatars[2]))),
                new ImageView(new Image(new FileInputStream(charAvatars[3]))),
                new ImageView(new Image(new FileInputStream(charAvatars[4]))),
                new ImageView(new Image(new FileInputStream(charAvatars[5]))) 	};

        VBox[] avatarChoices2 = new VBox[6]; // Array of Panes that hold each the avatar image and respective radio button
        RadioButton[] toggleAvatar2 = new RadioButton[6]; // Array of radio buttons to select the avatar
        ToggleGroup pickAvatar2 = new ToggleGroup(); // Group of 6 radio buttons
        for (int i = 0; i < 6; i++) {
            toggleAvatar2[i] = new RadioButton("Select " + (i + 1));  // Creates radio buttons for each index
            toggleAvatar2[i].setToggleGroup(pickAvatar2);
            avatarChoices2[i] = new VBox(2); // Creates the pane for the radio buttons and image
            images2[i].setFitWidth(80);
            images2[i].setFitHeight(80);
            avatarChoices2[i].getChildren().addAll(images2[i], toggleAvatar2[i]);
        }
        pickAvatar2.selectToggle(toggleAvatar2[0]);
        humanPlayerSelection2.getChildren().addAll( l2, avatarChoices2[0], avatarChoices2[1], avatarChoices2[2], avatarChoices2[3], avatarChoices2[4], avatarChoices2[5]);
        BorderPane.setMargin(humanPlayerSelection2, new Insets(0,0,10,0));

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

        start.setOnAction(event -> { // Event that fires when the user clicks the star button (begin the game)

            if (numberOfPlayers.getSelectedToggle() == zero) {
                gameMode = 0;

                if(beginner1.isSelected())
                    dif1 = 0;
                if(beginner2.isSelected())
                    dif2 = 0;
                if(intermediate1.isSelected())
                    dif1 = 1;
                if(intermediate2.isSelected())
                    dif2 = 1;
                if(impossible1.isSelected())
                    dif1 = 2;
                if(impossible2.isSelected())
                    dif2 = 2;

                autoAvatar1 = setAISettings1.getToggles().indexOf(setAISettings1.getSelectedToggle()); // Sets the variable to the selected index
                autoAvatar2 = setAISettings2.getToggles().indexOf(setAISettings2.getSelectedToggle());


            } else if (numberOfPlayers.getSelectedToggle() == one) {
                gameMode = 1;

                if(beginner1.isSelected())
                    System.out.println("beginner1");
                if(beginner.isSelected())
                    System.out.println("beginner");

                if(beginner.isSelected())
                    currentDifficulty = 0;
                else if(intermediate.isSelected())
                    currentDifficulty = 1;
                else if(impossible.isSelected())
                    currentDifficulty = 2;
                autoAvatar = setAISettings.getToggles().indexOf(setAISettings.getSelectedToggle()); // Sets the variable to the selected index
                humanAvatar = pickAvatar.getToggles().indexOf(pickAvatar.getSelectedToggle());
            }
            else {
                gameMode = 3;
                humanAvatar1 = pickAvatar1.getToggles().indexOf(pickAvatar1.getSelectedToggle()); // Sets the variable to the selected index
                humanAvatar2 = pickAvatar2.getToggles().indexOf(pickAvatar2.getSelectedToggle());
            }
            primaryStage.setScene(null);
            try {
                game(primaryStage); // Initiates the game method
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    private void game(Stage primaryStage) throws FileNotFoundException { // This method initiates the game phase based on the users settings

        BorderPane borderPane = new BorderPane(); // root pane
        GridPane pane = new GridPane(); // Pane to hold cells

        for (int x = 0; x < 3; x++) { // Creates each cell for a 3 x 3
            for (int y = 0; y < 3; y++) {
                pane.add(cell[x][y] = new Cell(primaryStage), y, x);
            }
        }


        borderPane.setCenter(pane);

        VBox rightTop = new VBox(10); // Creates a Pane for the avatars
        rightTop.setAlignment(Pos.TOP_CENTER);

        ImageView one; // variable for the image for Avatar 1
        ImageView two; // variable for the image for Avatar 2
        Label dog; // Label for dog
        Label cat; // Label for cat

        // Based on the game mode, set the labels and avatars
        if (gameMode == 0) {
            one = new ImageView(new Image(new FileInputStream(automatedAvatars[autoAvatar2]))); // Creates the image for the avatar
            two = new ImageView(new Image(new FileInputStream(automatedAvatars[autoAvatar1]))); // Creates the image for the avatar
            dog = new Label("AI 1: Dog"); // Creates the label indicating the AI
            cat = new Label("AI 2: Cat"); // Creates the label indicating the AI
        } else if (gameMode == 1) {
            one = new ImageView(new Image(new FileInputStream(automatedAvatars[autoAvatar]))); // Creates the image for the avatar
            two = new ImageView(new Image(new FileInputStream(charAvatars[humanAvatar]))); // Creates the image for the avatar
            dog = new Label("AI: Dog"); // Creates the label indicating the AI
            cat = new Label("Human: Cat"); // Creates the label indicating the human
        } else {
            one = new ImageView(new Image(new FileInputStream(charAvatars[humanAvatar1]))); // Creates the image for the avatar
            two = new ImageView(new Image(new FileInputStream(charAvatars[humanAvatar2]))); // Creates the image for the avatar
            dog = new Label("Human 1: Dog"); // Creates the label indicating the player
            cat = new Label("Human 2: Cat"); // Creates the label indicating the player
        }
        one.setFitHeight(100);
        one.setFitWidth(100);
        two.setFitHeight(100);
        two.setFitWidth(100);
        VBox.setMargin(one, new Insets(0,20, 0,20));

        dog.setTextFill(Color.WHITE);//sets the options label to
        dog.setFont(Font.font("", FontWeight.BOLD, 15));
        cat.setTextFill(Color.WHITE);//sets the options label to
        cat.setFont(Font.font("", FontWeight.BOLD, 15));

        rightTop.getChildren().addAll(one, dog, two, cat);
        borderPane.setRight(rightTop);

        lblStatus.setTextFill(Color.WHITE);//sets the options label to white.
        lblStatus.setFont(Font.font("", FontWeight.BOLD, 30));//makes options bold and size 15.

        HBox top = new HBox(10); // Creates a pane for the information at the top
        top.setAlignment(Pos.CENTER_LEFT);
        top.setTranslateX(10);

        Button newGame = new Button("New Game"); // Button that resets the game in the current game mode
        Button toMenu = new Button("Return to Menu"); // Button that returns to the menu
        Button selectBackground = new Button("Change"); // Button that changes the background
        ChoiceBox<Integer> backgroundIndexChooser = new ChoiceBox<Integer>(); // Creates a choice box to choose a background index
        backgroundIndexChooser.getItems().addAll(1,2,3,4,5,6);
        backgroundIndexChooser.getSelectionModel().select(0);

        selectBackground.setOnAction(e -> { // When change is clicked, changes the background
            BackgroundImage myBI;
            try {
                myBI = new BackgroundImage(new Image(new FileInputStream(linksBackgrounds[backgroundIndexChooser.getValue()-1])), // Creates a new background from the reference index and URL string array
                        BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                        new BackgroundSize(1200,900, false, false, false, false));
                borderPane.setBackground(new Background(myBI)); // Sets the background
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }

        });


        wins.setTextFill(Color.WHITE);
        wins.setFont(Font.font("", FontWeight.BOLD, 20));
        trashTalk.setTextFill(Color.PINK);
        trashTalk.setFont(Font.font("", FontWeight.BOLD, 20));
        forGameResults.setVisible(false); // Sets visibility
        forGameResults.setTranslateX(-250); // Translates the game results label used for the animations to the center of the screen
        forGameResults.setTranslateY(300);
        forGameResults.toBack();
        forGameResults.setFont(Font.font("", FontWeight.BOLD, 40));

        top.getChildren().addAll(backgroundIndexChooser, selectBackground, newGame, toMenu, wins ,trashTalk, forGameResults);
        borderPane.setTop(top);
        borderPane.setBottom(lblStatus);
        borderPane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY))); // Create a black background

        Scene scene = new Scene(borderPane, 1200, 900); // Sets the scene
        primaryStage.setTitle("TicTacToe"); // Set the stage title
        primaryStage.setMaxWidth(1200);
        primaryStage.setMinWidth(1200);
        primaryStage.setMaxHeight(900);
        primaryStage.setMinHeight(900);
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage

        newGame.setOnAction(event -> { // This event resets the game for the same game mode
            primaryStage.setScene(null);
            try {

                whoseTurn = 'X';
                game(primaryStage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

        toMenu.setOnAction(event -> { // This event takes the use back to the menu
            xWins = 0;
            oWins = 0;
            wins.setText("Cat Wins: " + xWins + "  Dog Wins: " + oWins);
            primaryStage.setScene(null);
            try {
                whoseTurn = 'X';
                start(primaryStage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

        if(gameMode == 0)//If game mode is 0 players then this has the AI play each other.
        {
            Cell start = new Cell(primaryStage);
            start.AI(primaryStage, 'X');
            while (whoseTurn != ' ')
            {
                start.AI(primaryStage, 'O');
                start.AI(primaryStage, 'X');
            }
        }
    }

    public boolean isFull()//checks if every cell is full.
    {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (cell[i][j].getToken() == ' ')
                    return false;

        return true;
    }

    public boolean isWon(char token)//checks for a winner and returns true for 3 in a row of thes same token.
    {
        for (int i = 0; i < 3; i++)//checks for vertical 3 in a row.
            if (cell[i][0].getToken() == token && cell[i][1].getToken() == token && cell[i][2].getToken() == token)
                return true;

        for (int j = 0; j < 3; j++)//checks for horizontal 3 in a row.
            if (cell[0][j].getToken() ==  token && cell[1][j].getToken() == token && cell[2][j].getToken() == token)
                return true;

        //checks for top left to bottom right diagonal 3 in a row.
        if (cell[0][0].getToken() == token && cell[1][1].getToken() == token && cell[2][2].getToken() == token)
            return true;

        //checks for top right to bottom left diagonal 3 in a row.
        if (cell[0][2].getToken() == token && cell[1][1].getToken() == token && cell[2][0].getToken() == token)
            return true;

        return false;//If no winners yet return false.
    }

    // An inner class for a cell
    public class Cell extends Pane
    {
        // Token used for this cell
        private char token = ' ';

        public Cell(Stage primaryStage) throws FileNotFoundException {
            setStyle("-fx-border-color: RED");//sets the border of the game boxes to red.
            this.setPrefSize(800, 850);//sets the game dimensions.


            if(gameMode != 0)
                this.setOnMouseClicked(e -> //if the mouse clicks on a cell run the handleMouseClick method.
                {
                    try {
                        handleMouseClick(primaryStage);

                        if(gameMode == 1 && whoseTurn == 'O')
                            AI(primaryStage, token);

                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }
                });

        }

        public char getToken()
        {
            return token;
        }//allows for access to what the token is at the given cell.

        public void setToken(char c, Stage primaryStage) throws FileNotFoundException
        {
            token = c;//sets token.

            if (token == 'X')//if token is X place the cat image in the cell.
            {
                Pane catX = new Pane();//makes a pane for the image.
                ImageView cat = new ImageView(new Image(new FileInputStream("Pictures/X.jpg")));//sets the image file to cat.
                cat.fitHeightProperty().bind(catX.heightProperty());//sets the image height.
                cat.fitWidthProperty().bind(catX.widthProperty());//sets the image width.
                catX.getChildren().add(cat);//adds the cat image to the pane.
                catX.setPrefSize(primaryStage.getWidth()/3-51, primaryStage.getHeight()/3-49);//sets the pane size.
                this.getChildren().add(catX);//places the pane into the cell clicked.
            }
            else if (token == 'O')//This does the same thing for the dog that it did for the cat. The only difference is the Dog image file and variable names are changed to dog.
            {
                Pane dogX = new Pane();
                ImageView dog = new ImageView(new Image(new FileInputStream("Pictures/O.jpg")));
                dog.fitHeightProperty().bind(dogX.heightProperty());
                dog.fitWidthProperty().bind(dogX.widthProperty());
                dogX.getChildren().add(dog);
                dogX.setPrefSize(primaryStage.getWidth()/3-51, primaryStage.getHeight()/3-49);
                this.getChildren().add(dogX);
            }
        }
        private void handleMouseClick(Stage primaryStage) throws FileNotFoundException  //Handles the mouse click.
        {
            // If cell is empty and game is not over
            if (token == ' ' && whoseTurn != ' ') {
                setToken(whoseTurn, primaryStage); // Set token in the cell

                whoseTurn(primaryStage);//change the turn.
            }
        }

        public void whoseTurn(Stage primaryStage) throws FileNotFoundException
        {
            // Check game status
            if (isWon(whoseTurn))//if someone won continue.
            {
                if(whoseTurn == 'X')//if X(Cats) won continue.
                {
                    winAnimation(primaryStage, "Cats");
                    lblStatus.setText("Cats won! The game is over");//sets lblStatus to who won.
                    wins.setText("Cat Wins: " + ++xWins + "  Dog Wins: " + oWins);//Adds to the cat win count and sets the text to show it.
                }
                if(whoseTurn == 'O')//if O(Dogs) won continue.
                {
                    if (gameMode != 1) {
                        winAnimation(primaryStage, "Dogs");
                    } else
                        loseAnimation(primaryStage);
                    lblStatus.setText("Dogs won! The game is over");//sets lblStatus to who won.
                    wins.setText("Cat Wins: " + xWins + "  Dog Wins: " + ++oWins);//Adds to the dog win count and sets the text to show it.
                }
                whoseTurn = ' '; //ends game.
            }
            else if (isFull()) //if its a full board continue.
            {
                drawAnimation(primaryStage);
                lblStatus.setText("Draw! The game is over");//sets the lblStatus to a draw.

                whoseTurn = ' '; // Ends the game.
            }
            else//If its not a draw and no one won yet continue.
            {
                // Change the turn
                if(whoseTurn == 'O')
                    whoseTurn = 'X';
                else if(whoseTurn == 'X')
                    whoseTurn = 'O';

                theTrash();//do the trash talk method.

                if(whoseTurn == 'X')//Tells the cat its his turn it it is his turn.
                    lblStatus.setText("Cats turn.");
                else if(whoseTurn == 'O')//Tells the dog its his turn if its his turn.
                    lblStatus.setText("Dogs turn.");
            }
        }

       	private void AI(Stage primaryStage, char token) throws FileNotFoundException//Does the AI's turn.
        {
        if(gameMode == 0)//if its just AI's playing.
        {
            if(whoseTurn == 'X')//Does the X AI's turn
            {
                if (dif1 == 0)//beginner difficulty.
                {
                    randomMove(primaryStage, token);//picks a random free spot.
                }
                else if (dif1 == 1)//intermediate difficulty. Picks a random free spot, unless the opponent has 2 in a line.
                {
                    stop = false;//resets stop incase AIStopCheck stopped a move last move.
                    AIStopCheck(primaryStage, token);
                    if (stop == false) //does not move again if it already stopped a move.
                        randomMove(primaryStage, token);//does a random move.
                }
                else if (dif1 == 2) //Impossible difficulty
                {
                    System.out.println("XXXX"+ "   " + whoseTurn);
                    stop = false;//This resets stop to false each move.
                    won = false;//This resets won to false each move.
                    if (whoseTurn != ' ')//as long as the game is not over yet continue.
                        AIWinCheck(primaryStage, token);

                    if (whoseTurn != ' ' && won == false)//As long as the game is not over yet, and the AIWinCheck method did not just win continue.
                    {
                        AIStopCheck(primaryStage, token);
                        if (stop == false) //As long as the AIStopCheck method did not just make a move continue.
                        {
                            AINormalMove(primaryStage, token);
                        }
                    }
                }
            }
            else if (whoseTurn == 'O')//does the O AI's turn
            {
                if (dif2 == 0)//beginner difficulty.
                {
                    randomMove(primaryStage, token);//picks a random free spot.
                }
                else if (dif2 == 1)//intermediate difficulty. Picks a random free spot, unless the opponent has 2 in a line.
                {
                    stop = false;//resets stop incase AIStopCheck stopped a move last move.
                    AIStopCheck(primaryStage, token);
                    if (stop == false) //does not move again if it already stopped a move.
                        randomMove(primaryStage, token);//does a random move.
                }
                else if (dif2 == 2) //Impossible difficulty
                {
                    System.out.println("OOO" + "   " + whoseTurn);
                    stop = false;//This resets stop to false each move.
                    won = false;//This resets won to false each move.
                    if (whoseTurn != ' ')//as long as the game is not over yet continue.
                        AIWinCheck(primaryStage, token);

                    if (whoseTurn != ' ' && won == false)//As long as the game is not over yet, and the AIWinCheck method did not just win continue.
                    {
                        AIStopCheck(primaryStage, token);
                        if (stop == false) //As long as the AIStopCheck method did not just make a move continue.
                        {
                            AINormalMove(primaryStage, token);
                        }
                    }

                }
            }

        }
        else if(currentDifficulty == 0)//beginner difficulty.
        {
            randomMove(primaryStage, token);//picks a random free spot.
        }
        else if(currentDifficulty == 1)//intermediate difficulty. Picks a random free spot, unless the opponent has 2 in a line.
        {
            stop = false;//resets stop incase AIStopCheck stopped a move last move.
            AIStopCheck(primaryStage, token);
            if(stop == false) //does not move again if it already stopped a move.
                randomMove(primaryStage, token);//does a random move.
        }
        else if(currentDifficulty == 2) //Impossible difficulty
        {
            stop = false;//This resets stop to false each move.
            won = false;//This resets won to false each move.
            if (whoseTurn != ' ')//as long as the game is not over yet continue.
                AIWinCheck(primaryStage, token);

            if (whoseTurn != ' ' && won == false)//As long as the game is not over yet, and the AIWinCheck method did not just win continue.
            {
                AIStopCheck(primaryStage, token);
                if (stop == false) //As long as the AIStopCheck method did not just make a move continue.
                {
                    AINormalMove(primaryStage, token);
                }
            }
        }
    }
        private void randomMove(Stage primaryStage, char token) throws FileNotFoundException
        {
            //the if, else if swaps it to the AI's token.
            if(token == 'O')
                token = 'X';
            else if(token == 'X')
                token = 'O';
            boolean moved = false;
            while(moved == false)
            {
                int x = (int)(Math.random() * 3);
                int y = (int)(Math.random() * 3);
                char check = cell[x][y].getToken();
                if(check == ' ') {
                    cell[x][y].setToken(token, primaryStage);
                    whoseTurn(primaryStage);
                    moved = true;
                }
            }
        }


        private void AINormalMove(Stage primaryStage, char token) throws FileNotFoundException//This method does normal moves if the AIWinCheck and AIStopCheck are not needed.
        {
            //the if, else if swaps it back to the AI's token.
            if(token == 'O')
                token = 'X';
            else if(token == 'X')
                token = 'O';

            //The next 8 chars collect the state of the board.
            char topCenter = (cell[0][1].getToken());
            char leftCenter = (cell[1][0].getToken());
            char rightCenter = (cell[1][2].getToken());
            char bottomCenter = (cell[2][1].getToken());

            char topLeft = (cell[0][0].getToken());
            char topRight = (cell[0][2].getToken());
            char bottomLeft = (cell[2][0].getToken());
            char bottomRight = (cell[2][2].getToken());

            if(cell[1][1].getToken() == ' ')//first move center if possible.
            {
                //takes the center and runs whoseTurn.
                cell[1][1].setToken(token, primaryStage);
                whoseTurn(primaryStage);
            }

            //if the center was taken by the AI and it has not taken the top bottom left or right center spots then go with one of those.
            else if(cell[1][1].getToken() == token && true == (token != topCenter && token != leftCenter && token != rightCenter && token != bottomCenter))
            {
                if(topCenter == ' ' && bottomCenter == ' ') //if top center and bottom center are open take the top spot.
                {
                    cell[0][1].setToken(token, primaryStage);
                    whoseTurn(primaryStage);
                }
                else if(leftCenter == ' ' && rightCenter == ' ')//if left center and right center are free take the left center.
                {
                    cell[1][0].setToken(token, primaryStage);
                    whoseTurn(primaryStage);
                }
                else if(bottomCenter != ' ' && bottomCenter != token && rightCenter != ' ' && rightCenter != token && bottomRight == ' ')//if the bottom center and right center is taken by the opponent take the bottom right.
                {
                    cell[2][2].setToken(token, primaryStage);
                    whoseTurn(primaryStage);
                }
                else if(topLeft == ' ')//all else fails take the top left if its open.
                {
                    cell[0][0].setToken(token, primaryStage);
                    whoseTurn(primaryStage);
                }
                else if(leftCenter == ' ')//all else fails take the left center if its open.
                {
                    cell[1][0].setToken(token, primaryStage);
                    whoseTurn(primaryStage);
                }
                else if(topRight == ' ')//all else fails take the top right if its open.
                {
                    cell[0][2].setToken(token, primaryStage);
                    whoseTurn(primaryStage);
                }
            }
            //if the center was taken by the AI and it has taken the top bottom left or right center spots then continue.
            else if(cell[1][1].getToken() == token && false == (token != topCenter && token != leftCenter && token != rightCenter && token != bottomCenter))
            {
                //stops a way it can lose I found.
                if(rightCenter != ' ' && rightCenter != token && topCenter != ' ' && topCenter != token && bottomLeft != ' ' && bottomLeft != token)
                {
                    cell[2][2].setToken(token, primaryStage);
                    whoseTurn(primaryStage);
                }
                //stops a way it can lose I found.
                else if(topLeft != ' ' && topLeft != token && bottomCenter != ' ' && bottomCenter != token && rightCenter != ' ' && rightCenter != token)
                {
                    cell[2][0].setToken(token, primaryStage);
                    whoseTurn(primaryStage);
                }
                //stops a way it can lose I found.
                else if(topRight != ' ' && topRight != token && bottomCenter != ' ' && bottomCenter != token && leftCenter != ' ' && leftCenter != token)
                {
                    cell[2][2].setToken(token, primaryStage);
                    whoseTurn(primaryStage);
                }
                //Basically this happens towards the end, and this takes a spot if its open.
                else if(topLeft == ' ')
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
            else if(cell[1][1].getToken() != token && topLeft != token)//if the center was taken by the opponent, and the top left is not taken yet take the top left.
            {
                cell[0][0].setToken(token, primaryStage);
                whoseTurn(primaryStage);
            }

            else if(cell[0][0].getToken() == token && bottomRight != ' ' && bottomRight != token)//if the Ai has the top left, and the bottom right is taken by the opponent continue here.
            {
                if(topRight == ' ' && topCenter == ' ')//if top right and top center are open take the top right.
                {
                    cell[0][2].setToken(token, primaryStage);
                    whoseTurn(primaryStage);
                }
                else if(topCenter == ' ')//if all else fails take top center if its open.
                {
                    cell[0][1].setToken(token, primaryStage);
                    whoseTurn(primaryStage);
                }
                else if(leftCenter == ' ')//if all else fails take left center if its open.
                {
                    cell[1][0].setToken(token, primaryStage);
                    whoseTurn(primaryStage);
                }
            }
        }
        private void AIWinCheck(Stage primaryStage, char token) throws FileNotFoundException//This method wins the game if the AI has 2 in a line and can place the third.
        {
            token = whoseTurn;//sets token to the AI's token.
            for (int i = 0; i < 3; i++)//This checks for 2 in a line Horizontally. If found it takes the 3rd spot and wins.
            {
                char one = (cell[i][0].getToken());
                char two = (cell[i][1].getToken());
                char three = (cell[i][2].getToken());

                if(one == token && two == token && (three == ' '))
                {
                    cell[i][2].setToken(token, primaryStage);
                    whoseTurn(primaryStage);
                    won = true;//sets won to true to let the rest of the method know not to make another move.(same for the rest of these in the method)
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
            if(won == false)//if the AI has not won continue.
                for (int i = 0; i < 3; i++)//Checks for 2 in a line vertically, and if found it takes the 3rd spot and wins.
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

            if(won == false) //if the AI has not won continue.
            {
                //The following if and 2 else if's check for 2 in a line of the top left to bottom right diagonal line. If found then it takes the 3rd and wins the game.
                if (lCrossOne == token && lCrossTwo == token && lCrossThree == ' ')
                {
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

            if(won == false) //if the AI has not won continue.
            {
                //The following if and 2 else if's check for 2 in a line of the top right to bottom left diagonal line. If found then it takes the 3rd and wins the game.
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

        private void AIStopCheck(Stage primaryStage, char token) throws FileNotFoundException //This method stops the opponent from getting 3 in a row if they have 2 in a line already.
        {
            token = whoseTurn;//this sets the token to the AI's token.

            //The following if and else if swap the token to the opponents token.
            if (token == 'O')
                token = 'X';
            else if (token == 'X')
                token = 'O';

            for (int i = 0; i < 3; i++)//This checks for 2 in a line Horizontally. If found it takes the 3rd spot to stop the opponent from getting 3 in a row.
            {
                char one = (cell[i][0].getToken());
                char two = (cell[i][1].getToken());
                char three = (cell[i][2].getToken());

                if (one == token && two == token && (three == ' ')) {
                    //The following else and else if swaps the token back to the AI's token before taking the spot.(Same for the rest of these in the method)
                    if (token == 'O')
                        token = 'X';
                    else if (token == 'X')
                        token = 'O';

                    cell[i][2].setToken(token, primaryStage);
                    whoseTurn(primaryStage);
                    stop = true;//sets stop to true so the rest of the method knows not take another spot. (same for the rest of these in the method)
                    break;
                } else if (one == token && two == ' ' && three == token) {
                    if (token == 'O')
                        token = 'X';
                    else if (token == 'X')
                        token = 'O';

                    cell[i][1].setToken(token, primaryStage);
                    whoseTurn(primaryStage);
                    stop = true;

                    break;
                } else if (one == ' ' && two == token && three == token) {
                    if (token == 'O')
                        token = 'X';
                    else if (token == 'X')
                        token = 'O';

                    cell[i][0].setToken(token, primaryStage);
                    whoseTurn(primaryStage);
                    stop = true;
                    break;
                }
            }

            if (stop == false)//if the AI has not stopped and opponents move continue.
                for (int i = 0; i < 3; i++)//This checks for 2 in a line Vertically. If found it takes the 3rd spot to stop the opponent from getting 3 in a row.
                {
                    char one = (cell[0][i].getToken());
                    char two = (cell[1][i].getToken());
                    char three = (cell[2][i].getToken());

                    if (one == token && two == token && three == ' ') {
                        if (token == 'O')
                            token = 'X';
                        else if (token == 'X')
                            token = 'O';

                        cell[2][i].setToken(token, primaryStage);
                        whoseTurn(primaryStage);
                        stop = true;
                        break;
                    } else if (one == token && two == ' ' && three == token) {
                        if (token == 'O')
                            token = 'X';
                        else if (token == 'X')
                            token = 'O';

                        cell[1][i].setToken(token, primaryStage);
                        whoseTurn(primaryStage);
                        stop = true;
                        break;
                    } else if (one == ' ' && two == token && three == token) {
                        if (token == 'O')
                            token = 'X';
                        else if (token == 'X')
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

            if (stop != true)//if the AI has not stopped and opponents move continue.
            {
                //The following if and 2 else if's check for 2 in a line of the top left to bottom right diagonal line. If found then it takes the 3rd spot to stop the opponent from getting 3 in a row.
                if (lCrossOne == token && lCrossTwo == token && lCrossThree == ' ') {
                    if (token == 'O')
                        token = 'X';
                    else if (token == 'X')
                        token = 'O';

                    cell[2][2].setToken(token, primaryStage);
                    whoseTurn(primaryStage);
                    stop = true;
                } else if (lCrossOne == token && lCrossTwo == ' ' && lCrossThree == token) {
                    if (token == 'O')
                        token = 'X';
                    else if (token == 'X')
                        token = 'O';

                    cell[1][1].setToken(token, primaryStage);
                    whoseTurn(primaryStage);
                    stop = true;
                } else if (lCrossOne == ' ' && lCrossTwo == token && lCrossThree == token) {
                    if (token == 'O')
                        token = 'X';
                    else if (token == 'X')
                        token = 'O';

                    cell[0][0].setToken(token, primaryStage);
                    whoseTurn(primaryStage);
                    stop = true;
                }
            }

            char rCrossOne = (cell[0][2].getToken());
            char rCrossTwo = (cell[1][1].getToken());
            char rCrossThree = (cell[2][0].getToken());

            if(stop != true)//if the AI has not stopped and opponents move continue.
            {
                //The following if and 2 else if's check for 2 in a line of the top right to bottom left diagonal line. If found then it takes the 3rd spot to stop the opponent from getting 3 in a row.
                if (rCrossOne == token && rCrossTwo == token && rCrossThree == ' ') {
                    if (token == 'O')
                        token = 'X';
                    else if (token == 'X')
                        token = 'O';

                    cell[2][0].setToken(token, primaryStage);
                    whoseTurn(primaryStage);
                    stop = true;
                } else if (rCrossOne == token && rCrossTwo == ' ' && rCrossThree == token) {
                    if (token == 'O')
                        token = 'X';
                    else if (token == 'X')
                        token = 'O';

                    cell[1][1].setToken(token, primaryStage);
                    whoseTurn(primaryStage);
                    stop = true;
                } else if (rCrossOne == ' ' && rCrossTwo == token && rCrossThree == token) {
                    if (token == 'O')
                        token = 'X';
                    else if (token == 'X')
                        token = 'O';

                    cell[0][2].setToken(token, primaryStage);
                    whoseTurn(primaryStage);
                    stop = true;
                }
            }
        }

        private void theTrash()//This method is in charge of the trash talk.
        {
            int doOrNot = (int)(Math.random() * 2) + 1;//flips a coin.
            int rand = (int)(Math.random() * 6) + 1;//picks a random number from 1-6 for the trash talk.
            trashTalk.setText("");//sets the trash talk text to nothing
            trashTalk.setFont(Font.font(16));
            if(doOrNot == 1)//flips a coin to see if it will or will not mock the other player.
                switch (rand)//picks a random trash talk, and sets trashTalk to a new trash talk text.
                {
                    case 1:
                    {
                        trashTalk.setText("AI: Wow. Call that a move?");
                    }break;
                    case 2:
                    {
                        trashTalk.setText("AI: I saw that a mile away.");
                    }break;
                    case 3:
                    {
                        trashTalk.setText("AI: How predictable.");
                    }break;
                    case 4:
                    {
                        trashTalk.setText("AI: You insult my intellect.");
                    } break;
                    case 5:
                    {
                        trashTalk.setText("AI: You should just give up.");
                    }break;
                    case 6:
                    {
                        trashTalk.setText("AI: You really need to practice.");
                    }break;
                }

        }
    }

    // Win Animations takes two parameters for when there are two human players and AI vs AI
    private void winAnimation(Stage primaryStage, String whoWon) throws FileNotFoundException // Method that animates a label to show the user the win
    {
        Label win = forGameResults; // references the existing label that is not visible

        trashTalk.setText("");

        win.toFront();
        win.setVisible(true);
        win.setText("Congrats! " + whoWon + " won!"); // Shows who won
        win.setTextFill(Color.BLACK);
        win.setFont(Font.font("", FontWeight.BOLD, 40));
        win.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY))); // Creates a background for the label

        FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), win); // Creates a new Fade transition to fade in
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
        fadeIn.setOnFinished(e -> { // Event fires on the end of fade in
            FadeTransition fadeOut = new FadeTransition(Duration.millis(4000), win); // Creates a new Fade transition to fade out
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.play();
            fadeIn.setOnFinished(r -> { // Event fire on the end of fade out
                win.toBack();
                win.setVisible(false); // Once animation is done, change visibility
            });
        });
    }

    private void loseAnimation(Stage primaryStage) throws FileNotFoundException // Method that animates a label to show the user the loss
    {
        Label lose = forGameResults; // references the existing label that is not visible

        trashTalk.setText("");

        lose.toFront();
        lose.setVisible(true);
        lose.setText("Better Luck next Time!!");
        lose.setTextFill(Color.RED);
        lose.setFont(Font.font("", FontWeight.BOLD, 40));
        lose.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY))); // Creates a background for the label

        FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), lose); // Creates a new Fade transition to fade in
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
        fadeIn.setOnFinished(e -> { // Event fires on the end of fade in
            FadeTransition fadeOut = new FadeTransition(Duration.millis(4000), lose); // Creates a new Fade transition to fade out
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.play();
            fadeIn.setOnFinished(r -> { // Event fire on the end of fade out
                lose.toBack();
                lose.setVisible(false); // Once animation is done, change visibility
            });
        });

    }

    private void drawAnimation(Stage primaryStage) throws FileNotFoundException { // Method that animates a label to show the user the draw

        Label draw = forGameResults; // references the existing label that is not visible

        trashTalk.setText("");

        draw.toFront();
        draw.setVisible(true);
        draw.setText("Its a draw folks!!");
        draw.setTextFill(Color.WHITE);
        draw.setFont(Font.font("", FontWeight.BOLD, 40));
        draw.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY))); // Creates a background for the label

        FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), draw);  // Creates a new Fade transition to fade in
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
        fadeIn.setOnFinished(e -> { // Event fires on the end of fade in
            FadeTransition fadeOut = new FadeTransition(Duration.millis(4000), draw); // Creates a new Fade transition to fade out
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.play();
            fadeIn.setOnFinished(r -> { // Event fire on the end of fade out
                draw.toBack();
                draw.setVisible(false); // Once animation is done, change visibility
            });
        });
    }
}