package jacksonfootball;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.swing.ImageIcon;

/**
 * @author Kai Duty
 */
public class JacksonFootball extends Application {
    
    private ArrayList<Game> games;
    private ArrayList<Player> players;
    private String seasonString;
    private Font textFont;
    private static int gameCounter;
    private static boolean isPlayerCreated;

    /**
     * Constructor that initializes all variables and tried to get data from the
     * databases
     */
    public JacksonFootball(){
        
        games = new ArrayList();
        players = new ArrayList();
        seasonString = "";
        textFont = new Font("Courier", 20);
        gameCounter = 0;
        isPlayerCreated = false;
        loadGameDatabase();
        loadPlayerDatabase();
    
    }

    /**
     * Main stage of the program, displays all information and holds buttons for
     * further actions
     * @param primaryStage 
     */
    @Override
    public void start(Stage primaryStage) {
        
        //Sorts players based on win count
        Collections.sort(players);

        BorderPane borderPane = new BorderPane();
        //Background image
        ImageView background = new ImageView(
                               new Image("image/startupBackground.jpg"));
        background.fitWidthProperty().bind(primaryStage.widthProperty());
        background.fitHeightProperty().bind(primaryStage.heightProperty());
        ImageView background2 = new ImageView(
                                 new Image("image/collegeFootball.png"));
        background2.fitWidthProperty().bind(primaryStage.widthProperty());
        background2.fitHeightProperty().bind(primaryStage.heightProperty());
        background2.setOpacity(.20);
        
        //Top
        Text header = new Text(seasonString + " College Bowl Championships");
        header.setFill(Color.WHITE);
        header.setFont(new Font("Courier", 30));
        header.setStyle("-fx-font-weight:bold");
        StackPane topPane = new StackPane();
        topPane.setAlignment(Pos.CENTER);
        topPane.getChildren().add(header);
        borderPane.setTop(topPane);

        //Holds the leaderboard and results
        HBox leaderboardAndResults = new HBox();
        leaderboardAndResults.setAlignment(Pos.CENTER);
        leaderboardAndResults.setSpacing(40);
        
        //Leaderboard
        VBox leaderboardVBox = new VBox();
        leaderboardVBox.setSpacing(10);
        leaderboardVBox.setAlignment(Pos.TOP_CENTER);
        GridPane leaderboard = refreshLeaderboardGridPane();
        
        Text leaderboardText = new Text("Leaderboard");
        leaderboardText.setUnderline(true);
        leaderboardText.setFont(new Font("Courier", 25));
        leaderboardText.setFill(Color.WHITE);
        leaderboardText.setStyle("-fx-font-weight:bold");
        leaderboardVBox.getChildren().addAll(leaderboardText, leaderboard);
        
        //Results
        VBox resultsVBox = new VBox();
        resultsVBox.setSpacing(10);
        resultsVBox.setAlignment(Pos.TOP_CENTER);
        GridPane results = refreshResultsGridPane();
        
        Text resultsText = new Text("Results");
        resultsText.setFont(new Font("Courier", 25));
        resultsText.setUnderline(true);
        resultsText.setFill(Color.WHITE);
        resultsText.setStyle("-fx-font-weight:bold");
        
        resultsVBox.getChildren().addAll(resultsText, results);
        leaderboardAndResults.getChildren().addAll(leaderboardVBox, resultsVBox);
        borderPane.setCenter(leaderboardAndResults);
        
        
        //Bottom
        VBox bottomResultsAndButtons = new VBox();
        bottomResultsAndButtons.setSpacing(5);
        bottomResultsAndButtons.setAlignment(Pos.CENTER);
        Text addResultsText = new Text("Enter the winner of the next game:");
        addResultsText.setUnderline(true);
        addResultsText.setFont(new Font("Courier", 25));
        addResultsText.setFill(Color.WHITE);
        addResultsText.setStyle("-fx-font-weight:bold");
        bottomResultsAndButtons.getChildren().add(addResultsText);
        
        Button addPlayers = new Button("Add Players");
        
        if(getNextGameIndex() != -1){
            
            HBox displayNextResultHBox = new HBox();
            displayNextResultHBox.setAlignment(Pos.CENTER);
            //displayNextResultHBox.setTranslateX(displayNextResultHBox.getTranslateX() + 225);
            displayNextResultHBox.setSpacing(10);
            Game tmpGame = games.get(getNextGameIndex());
            Text game1ResultsText = new Text(tmpGame.getTeam1Name());
            Text versesResultsText = new Text("vs.");
            Text game2ResultsText = new Text(tmpGame.getTeam2Name());
            game1ResultsText.setFont(textFont);
            versesResultsText.setFont(textFont);
            game2ResultsText.setFont(textFont);
            game1ResultsText.setFill(Color.WHITE);
            versesResultsText.setFill(Color.WHITE);
            game2ResultsText.setFill(Color.WHITE);
            displayNextResultHBox.getChildren().addAll(game1ResultsText,
                                                       versesResultsText,
                                                       game2ResultsText);
            HBox checkBoxHBox = new HBox();
            checkBoxHBox.setAlignment(Pos.CENTER);
            checkBoxHBox.setSpacing(25);
            CheckBox game1Check = new CheckBox();
            CheckBox game2Check = new CheckBox();
            TextField combinedScore = new TextField("enter combined score");
            combinedScore.setVisible(false);
            game1Check.setOnMouseClicked(e ->
                       setAndRefreshResults(results, game1Check, resultsVBox,
                                            game1ResultsText, game2ResultsText,
                                            1, addPlayers, displayNextResultHBox,
                                            checkBoxHBox, addResultsText,
                                            combinedScore, leaderboard,
                                            leaderboardVBox));

            
            game2Check.setOnMouseClicked(e ->
                       setAndRefreshResults(results,game2Check, resultsVBox,
                                            game1ResultsText, game2ResultsText,
                                            2, addPlayers, displayNextResultHBox,
                                            checkBoxHBox, addResultsText,
                                            combinedScore, leaderboard,
                                            leaderboardVBox));
            
            checkBoxHBox.getChildren().addAll(game1Check, combinedScore, 
                                              game2Check);
            
            bottomResultsAndButtons.getChildren().addAll(displayNextResultHBox,
                                                         checkBoxHBox);
            
        }
        
        HBox bottomButtons = new HBox();
        bottomButtons.setPadding(new Insets(10));
        bottomButtons.setAlignment(Pos.CENTER);
        bottomButtons.setPadding(new Insets(0,0,20,0));
        bottomButtons.setSpacing(20);
        addPlayers.setFont(new Font(15));
        addPlayers.setPrefSize(125, 40);
        addPlayers.setOnMouseClicked(e -> addPlayerHandler(leaderboard, leaderboardVBox));
        
        Button saveAndExit = new Button("Save and Exit");
        saveAndExit.setFont(new Font(15));
        saveAndExit.setPrefSize(125, 40);
        saveAndExit.setOnMouseClicked(e ->{
            
            writeGameDatabase();
            writePlayerDatabase();
            System.exit(0);
            
        });
        Button reset = new Button("Reset All");
        reset.setFont(new Font(15));
        reset.setPrefSize(100, 17);
        reset.setOnMouseClicked(e ->{
            
            File deleteGames = new File("gameDatabase");
            File deletePlayers = new File("playerDatabase");
            deleteGames.delete();
            deletePlayers.delete();
            primaryStage.close();
            
        });
        
        bottomButtons.getChildren().addAll(addPlayers, saveAndExit, reset);
        bottomResultsAndButtons.getChildren().addAll(bottomButtons);
        borderPane.setBottom(bottomResultsAndButtons);
        
        StackPane primarySceneStack = new StackPane();
        primarySceneStack.getChildren().addAll(background, background2, borderPane);
        Scene scene = new Scene(primarySceneStack, 800, 900);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Jackson Football");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        
        
    }

    /**
     * Loads all player data from the player database
     */
    private void loadPlayerDatabase(){
        
        ObjectInputStream input = null;
         
        try {
             
            input = new ObjectInputStream(
                    new FileInputStream("playerDatabase"));
             
            //Infinite loop to run until EOFException is caught
            for(int i = 0; i < 1; i--){
                 
                Player tmpPlayer = (Player)input.readObject();
                boolean exists = false;
                for(int j = 0; j < players.size(); j++){
                    
                    if(players.get(j).getName().equals(tmpPlayer.getName()))
                        exists = true;
                    
                }
                 
                if(!exists)
                    players.add(tmpPlayer);
                 
            }
             
        }catch(EOFException ex){
            try {
                input.close();
            } catch (IOException ex1) {
               Logger.getLogger(JacksonFootball.class.getName()).log(Level.SEVERE,
                                                                     null, ex1);
            }
 
        } catch(FileNotFoundException ex){
            System.out.println("Error: database not found!");
        } catch(IOException | ClassNotFoundException ex){
            Logger.getLogger(JacksonFootball.class.getName()).log(Level.SEVERE,
                                                                  null, ex);
        }
        
    }
    
    /**
     * Writes all player data to the player database
     */
    private void writePlayerDatabase(){
        
        ObjectOutputStream output = null;
         
        try {
            output = new ObjectOutputStream(
                     new FileOutputStream("playerDatabase", false));
             
            //Writes all memory data into the file
            for(int i = 0; i < players.size(); i++)
                output.writeObject(players.get(i));
             
        } catch (FileNotFoundException ex) {
            System.out.println("Error: database not found!");
        } catch (IOException ex) {
            Logger.getLogger(JacksonFootball.class.getName()).log(Level.SEVERE,
                                                                  null, ex);
        }finally{
            try {
                output.close();
            } catch (IOException ex) {
               Logger.getLogger(JacksonFootball.class.getName()).log(Level.SEVERE,
                                                                     null, ex);
            }
        }
        
    }
    
    /**
     * Loads all game data from the game database
     */
    private void loadGameDatabase(){
        
        ObjectInputStream input = null;
         
        try {
             
            input = new ObjectInputStream(
                    new FileInputStream("gameDatabase"));
            
            seasonString = (String)input.readObject();
             
            //Infinite loop to run until EOFException is caught
            for(int i = 0; i < 1; i--){
                 
                Game tmpGame = (Game)input.readObject();
                boolean exists = false;
                for(int j = 0; j < games.size(); j++){
                    
                    if(games.get(j).getTeam1Name().equals(tmpGame.getTeam1Name()) &&
                       games.get(j).getTeam2Name().equals(tmpGame.getTeam2Name()))
                        exists = true;
                    
                }
                 
                if(!exists)
                    games.add(tmpGame);
                 
            }
             
        }catch(EOFException ex){
            try {
                input.close();
            } catch (IOException ex1) {
               Logger.getLogger(JacksonFootball.class.getName()).log(Level.SEVERE,
                                                                     null, ex1);
            }
 
        }catch(FileNotFoundException ex){
            
            addNewGames();
            
        }catch(IOException | ClassNotFoundException ex){
            Logger.getLogger(JacksonFootball.class.getName()).log(Level.SEVERE,
                                                                  null, ex);
        }
        
    }
    
    /**
     * Writes all game data to the game database
     */
    private void writeGameDatabase(){

        ObjectOutputStream output = null;
         
        try {
            output = new ObjectOutputStream(
                     new FileOutputStream("gameDatabase", false));
             
            output.writeObject(seasonString);
            
            //Writes all memory data into the file
            for(int i = 0; i < games.size(); i++)
                output.writeObject(games.get(i));
             
        } catch (FileNotFoundException ex) {
            System.out.println("Error: database not found!");
        } catch (IOException ex) {
            Logger.getLogger(JacksonFootball.class.getName()).log(Level.SEVERE,
                                                                  null, ex);
        }finally{
            try {
                output.close();
            } catch (IOException ex) {
               Logger.getLogger(JacksonFootball.class.getName()).log(Level.SEVERE,
                                                                     null, ex);
            }
        }
        
    }
    
    /**
     * Runs on startup if not game database file is detected, allows for user to
     * enter all season game information, then redirects to the initial player
     * stage
     */
    private void addNewGames(){
        
        //Sets stage with objects
        Stage stage = new Stage();
        VBox stageVBox = new VBox();
        
        ImageView background = new ImageView(
                               new Image("image/startupBackground.jpg"));
        background.fitWidthProperty().bind(stage.widthProperty());
        background.fitHeightProperty().bind(stage.heightProperty());

        stageVBox.setSpacing(10);
        stageVBox.setAlignment(Pos.TOP_CENTER);
        
        Text welcome = new Text("Welcome to a new season of");
        welcome.setStyle("-fx-font-weight:bold");
        welcome.setFont(new Font("Courier", 30));
        welcome.setFill(Color.WHITE);
        Text welcomeP2 = new Text("JACKSON FOOTBALL!");
        welcomeP2.setStyle("-fx-font-weight:bold");
        welcomeP2.setFont(new Font("Courier", 35));
        welcomeP2.setFill(Color.WHITE);
        welcomeP2.setStroke(Color.BLACK);
        
        ImageView footballLogo = new ImageView(
                                 new Image("image/footballLogo.png"));
        footballLogo.setFitHeight(200);
        footballLogo.setFitWidth(200);
        
        //Holds the season prompt and text field
        HBox seasonHBox = new HBox();
        seasonHBox.setSpacing(10);
        seasonHBox.setAlignment(Pos.CENTER);
        Text season = new Text("Enter this season's year:");
        season.setStyle("-fx-font-weight:bold");
        season.setFont(textFont);
        season.setFill(Color.WHITE);
        TextField enteredSeason = new TextField();
        Button seasonSubmit = new Button("Submit");
        seasonSubmit.setOnMouseClicked(e ->{
            
            seasonString = enteredSeason.getText();
            enteredSeason.setText("SUBMITTED");
            enteredSeason.setEditable(false);
            seasonSubmit.setVisible(false);
            
        });
        seasonHBox.getChildren().addAll(season, enteredSeason, seasonSubmit);
        
        //Holds the game submission objects
        Text enterGame = new Text("Enter each game in order: (exclude Championship)");
        enterGame.setFont(textFont);
        enterGame.setFill(Color.WHITE);
        enterGame.setStyle("-fx-font-weight:bold");
        HBox enterGameHBox = new HBox();
        enterGameHBox.setSpacing(10);
        enterGameHBox.setAlignment(Pos.CENTER);
        CheckBox isSemifinal = new CheckBox();
        //Makes sure only two games can be selected as semifinal
        isSemifinal.setOnMouseClicked(e ->{
            
            if(!ifSemifinalNeeded())
                isSemifinal.setSelected(false);
            
        });
        Text semifinal = new Text("*Check if semifinal*");
        semifinal.setFill(Color.WHITE);
        semifinal.setFont(new Font("Courier", 15));
        VBox semifinalVBox = new VBox();
        semifinalVBox.setSpacing(5);
        semifinalVBox.setAlignment(Pos.CENTER);
        semifinalVBox.getChildren().addAll(semifinal, isSemifinal);
        
        TextField game1Field = new TextField("");
        Text verses = new Text(" vs.");
        verses.setStyle("-fx-font-weight:bold");
        verses.setFont(textFont);
        verses.setFill(Color.WHITE);
        TextField game2Field = new TextField("");
        
        //Handles submit and undo buttons, and messages
        Button gameSubmit = new Button("Submit");
        Button undoGame = new Button("Undo");
        Text gameSubmitMessage = new Text();
        undoGame.setVisible(false);
        undoGame.setOnMouseClicked(e ->{

            gameSubmitMessage.setText(games.get(games.size() - 1).getTeam1Name() +
                                      " vs. " +
                                      games.get(games.size() - 1).getTeam2Name() +
                                      " REMOVED");
            games.remove(games.size() - 1);
            undoGame.setVisible(false);
            
        });
        
        gameSubmitMessage.setFont(textFont);
        gameSubmitMessage.setFill(Color.WHITE);
        gameSubmitMessage.setStyle("-fx-font-weight:bold");
        gameSubmit.setOnMouseClicked(e ->{
            
            if(!"".equals(game1Field.getText()) && !"".equals(game2Field.getText())){
                
                if(isSemifinal.isSelected() && ifSemifinalNeeded()){
                    
                    games.add(new Game(game1Field.getText(), game2Field.getText(),
                                       true));
                    isSemifinal.setSelected(false);
                    
                    
                }else
                    games.add(new Game(game1Field.getText(), game2Field.getText()));
                
                gameSubmitMessage.setText(game1Field.getText() + " vs. " +
                                          game2Field.getText() + " SUBMITTED");
                undoGame.setVisible(true);
                
            }
            
            game1Field.setText("");
            game2Field.setText("");
            
        });
        enterGameHBox.getChildren().addAll(semifinalVBox, game1Field, verses,
                                           game2Field, gameSubmit);
        
        HBox submitUndo = new HBox();
        submitUndo.setAlignment(Pos.CENTER);
        submitUndo.setSpacing(5);
        submitUndo.getChildren().addAll(gameSubmitMessage, undoGame);

        //Error message for completing before data is entered
        Text errorMessage = new Text();
        errorMessage.setVisible(false);
        errorMessage.setFont(textFont);
        errorMessage.setFill(Color.WHITE);
        errorMessage.setStroke(Color.RED);
        errorMessage.setStyle("-fx-font-weight:bold");
        Button finish = new Button("Game Schedule Complete");
        finish.setOnMouseClicked(e ->{
            
            if("".equals(seasonString)){
                
                errorMessage.setText("*must set season date*");
                errorMessage.setVisible(true);
                
            }else if(games.isEmpty()){
                
                errorMessage.setText("*must add games*");
                errorMessage.setVisible(true);
                
            }else{
                
                games.add(new Game("TBD", "TBD"));
                addPrimaryPlayers();
                stage.close();
                
                
            }
        
        });

        stageVBox.getChildren().addAll(welcome, welcomeP2, footballLogo,
                  seasonHBox,enterGame, enterGameHBox, submitUndo, finish,
                  errorMessage);
        StackPane pane = new StackPane();
        pane.getChildren().addAll(background, stageVBox);
        
        Scene newGameScene = new Scene(pane, 600, 525);
        stage.setScene(newGameScene);
        stage.setTitle("Jackson Football");
        stage.showAndWait();
        
    }
    
    /**
     * Window that prompts user for all initial player information
     */
    private void addPrimaryPlayers(){
        
        Stage primaryPlayersStage = new Stage();
        VBox stageVBox = new VBox();
        stageVBox.setSpacing(10);
        stageVBox.setAlignment(Pos.CENTER);
        ImageView playerBackground = new ImageView(
                                     new Image("image/startupBackground.jpg"));
        playerBackground.fitWidthProperty().bind(primaryPlayersStage.widthProperty());
        playerBackground.fitHeightProperty().bind(primaryPlayersStage.heightProperty());
        
        Text primaryPlayersTitle = new Text("Add a new player");
        primaryPlayersTitle.setStyle("-fx-font-weight:bold");
        primaryPlayersTitle.setUnderline(true);
        primaryPlayersTitle.setFill(Color.WHITE);
        primaryPlayersTitle.setFont(new Font("Courier", 20));
        Button newPlayerButton = new Button("Add player");
        newPlayerButton.setPrefSize(100, 40);
        Button goToPrimaryStage = new Button("Finish");
        goToPrimaryStage.setVisible(false);
        goToPrimaryStage.setPrefSize(100, 40);
        goToPrimaryStage.setFont(new Font(20));
        newPlayerButton.setOnMouseClicked(e ->{
            
            addPlayer();
            primaryPlayersTitle.setText(players.get(players.size() - 1).getName() + " added!");
            primaryPlayersTitle.setUnderline(false);
            newPlayerButton.setText("Add another");
            goToPrimaryStage.setVisible(true);
            
        });
        //Goes to main stage
        goToPrimaryStage.setOnMouseClicked(e ->{primaryPlayersStage.close();});
        
        //Set Stage
        stageVBox.getChildren().addAll(primaryPlayersTitle, newPlayerButton,
                                       goToPrimaryStage);
        StackPane pane = new StackPane();
        pane.setAlignment(Pos.CENTER);
        pane.getChildren().addAll(playerBackground, stageVBox);
        Scene primaryPlayersScene = new Scene(pane, 200, 200);
        primaryPlayersStage.setScene(primaryPlayersScene);
        primaryPlayersStage.setTitle("Jackson Football");
        primaryPlayersStage.showAndWait();
        
    }
    
    /**
     * Allows for updating the leaderboard after adding a new player
     * @param leaderboard
     * @param leaderboardVBox 
     */
    private void addPlayerHandler(GridPane leaderboard, VBox leaderboardVBox){
        
        addPlayer();
        
        //Updates the leaderboard with the new player information
        Node remove = leaderboardVBox.getChildren().remove(leaderboardVBox.getChildren().size() - 1);
        remove = null;
        leaderboard = refreshLeaderboardGridPane();
        leaderboardVBox.getChildren().add(leaderboard);
        
    }
    
    /**
     * Prompts the user for information to create a new player in the player
     * database
     */
    private void addPlayer(){
        
        Stage playerStage = new Stage();
        ImageView playerBackground = new ImageView(
                                     new Image("image/startupBackground.jpg"));
        playerBackground.fitWidthProperty().bind(playerStage.widthProperty());
        playerBackground.fitHeightProperty().bind(playerStage.heightProperty());
        Text playerText = new Text("Add a new player");
        playerText.setUnderline(true);
        playerText.setFont(textFont);
        playerText.setFill(Color.WHITE);
        playerText.setStyle("-fx-font-weight:bold");
        StackPane tmpPane = new StackPane();
        tmpPane.setAlignment(Pos.CENTER);
        tmpPane.getChildren().add(playerText);
        
        VBox playerInfoVBox = new VBox();
        playerInfoVBox.setAlignment(Pos.CENTER);
        playerInfoVBox.setSpacing(10);
        HBox playerNameHBox = new HBox();
        playerNameHBox.setAlignment(Pos.CENTER);
        Text nameText = new Text("Enter the player's name:");
        nameText.setFont(textFont);
        nameText.setFill(Color.WHITE);
        TextField playerName = new TextField();
        Text gameChoiceText = new Text("Choose each projected winner:");
        gameChoiceText.setFont(textFont);
        gameChoiceText.setFill(Color.WHITE);
        Button submitName = new Button("Submit");
        playerNameHBox.setSpacing(10);
        playerNameHBox.getChildren().addAll(nameText, playerName, submitName);
        HBox buttonHBox = new HBox();
        HBox gameInfo = new HBox();
        
        submitName.setOnMouseClicked(e ->{
            
            if(!"".equals(playerName.getText())){
                
                players.add(new Player(playerName.getText()));
                buttonHBox.setVisible(true);
                gameInfo.setVisible(true);
                playerName.setText("SUBMITTED");
                isPlayerCreated = true;
                
            }

        });

        gameInfo.setVisible(false);
        gameInfo.setAlignment(Pos.CENTER);
        gameInfo.setSpacing(5);
        
        //Handles the new player information
        Text game1Text = new Text(games.get(0).getTeam1Name());
        Text game2Text = new Text(games.get(0).getTeam2Name());
        Text versesText = new Text("vs.");
        game1Text.setFont(textFont);
        game2Text.setFont(textFont);
        versesText.setFont(textFont);
        game1Text.setFill(Color.WHITE);
        game2Text.setFill(Color.WHITE);
        versesText.setFill(Color.WHITE);
        gameInfo.getChildren().addAll(game1Text, versesText, game2Text);
        
        buttonHBox.setVisible(false);
        buttonHBox.setAlignment(Pos.CENTER);
        buttonHBox.setSpacing(100);
        CheckBox game1Choice = new CheckBox();
        CheckBox game2Choice = new CheckBox();
        buttonHBox.getChildren().addAll(game1Choice, game2Choice);

        HBox finalCombinedScoreHBox = new HBox();
        finalCombinedScoreHBox.setVisible(false);
        finalCombinedScoreHBox.setAlignment(Pos.CENTER);
        Text finalCombinedScoreText = new Text("Final combined score:");
        finalCombinedScoreText.setFont(textFont);
        finalCombinedScoreText.setFill(Color.WHITE);
        TextField finalCombinedScoreField = new TextField();
        finalCombinedScoreHBox.getChildren().addAll(finalCombinedScoreText,
                                                    finalCombinedScoreField);
        
        game1Choice.setOnMouseClicked(e ->{
            
            if(isPlayerCreated){
                
                gameCounter++;
            
                if(gameCounter < games.size()){//??????
                
                    if(games.size() == gameCounter + 1)
                        finalCombinedScoreHBox.setVisible(true);
                        
                    game1Text.setText(games.get(gameCounter).getTeam1Name());
                    game2Text.setText(games.get(gameCounter).getTeam2Name());
                    players.get(players.size() - 1).addChoice(1);
                    game1Choice.setSelected(false);

                }else{
                
                    if(!"".equals(finalCombinedScoreField.getText()) &&
                       !"*enter score*".equals(finalCombinedScoreField.getText())){
                        
                    players.get(players.size() - 1).addChoice(1);
                    players.get(players.size() - 1).setFinalCombinedScore(Integer.parseInt(finalCombinedScoreField.getText()));
                    isPlayerCreated = false;
                    gameCounter = 0;
                    //Update????????????????????????????????????
                    
                    playerStage.close();
                        
                    }else{
                        
                        gameCounter--;
                        game1Choice.setSelected(false);
                        finalCombinedScoreField.setText("*enter score*");
                        
                    }

                }
   
            }

        });
        game2Choice.setOnMouseClicked(e ->{
            
            if(isPlayerCreated){
                
                gameCounter++;
            
                if(gameCounter < games.size()){
                    
                    if(games.size() == gameCounter + 1)
                        finalCombinedScoreHBox.setVisible(true);
                
                    game1Text.setText(games.get(gameCounter).getTeam1Name());
                    game2Text.setText(games.get(gameCounter).getTeam2Name());
                    players.get(players.size() - 1).addChoice(2);
                    game2Choice.setSelected(false);
                
                }else{
                
                    if(!"".equals(finalCombinedScoreField.getText()) &&
                       !"*enter score*".equals(finalCombinedScoreField.getText())){
                        
                    players.get(players.size() - 1).addChoice(2);
                    players.get(players.size() - 1).setFinalCombinedScore(Integer.parseInt(finalCombinedScoreField.getText()));
                    isPlayerCreated = false;
                    gameCounter = 0;
                    //Update????????????????????????????????????
                    
                    playerStage.close();
                        
                    }else{
                        
                        gameCounter--;
                        game2Choice.setSelected(false);
                        finalCombinedScoreField.setText("*enter score*");
                        
                    }

                }
                
            }
  
        });
        
        playerInfoVBox.getChildren().addAll(tmpPane, playerNameHBox,
                                            gameChoiceText, gameInfo,
                                            buttonHBox, finalCombinedScoreHBox);
            
        StackPane backgroundPane = new StackPane();
        backgroundPane.getChildren().addAll(playerBackground, playerInfoVBox);
        Scene playerScene = new Scene(backgroundPane, 600, 400);
        playerStage.setScene(playerScene);
        playerStage.setTitle("Jackson Football");
        playerStage.showAndWait();
        
    }
    
    /**
     * True if there is less than 2 semifinal games added to the game database
     * @return 
     */
    private boolean ifSemifinalNeeded(){
        
        int counter = 0;
        
        for(int i = 0; i < games.size(); i++){
            
            if(games.get(i).getIfSemifinal())
                counter++;
            
        }
        
        return counter < 2;
        
    }
    
    //Returns the index of the next game score to be entered
    /**
     * Returns the index of the next game score to be entered, -1 if all games
     * entered.
     * @return 
     */
    private int getNextGameIndex(){
        
        for(int i = 0; i < games.size(); i++){
            
            if(games.get(i).getResults() == 0)
                return i;
            
        }
        
        return -1;

    }
    
    /**
     * Refreshes the results gridpane with the most current information,
     * recoloring text objects if there is a win verses a loss
     * @return 
     */
    private GridPane refreshResultsGridPane(){
        
        GridPane results = new GridPane();
        results.setPadding(new Insets(5));
        results.setBackground(new Background(new BackgroundFill(Color.GREY,
                                             new CornerRadii(10), Insets.EMPTY)));
        results.setHgap(10);
        ColumnConstraints leftResultsColumn = new ColumnConstraints();
        leftResultsColumn.setHalignment(HPos.RIGHT);
        results.getColumnConstraints().add(0, leftResultsColumn);
        for(int i = 0; i < games.size(); i++){
            
            Text tmpText1 = new Text(games.get(i).getTeam1Name());
            Text tmpText2 = new Text(games.get(i).getTeam2Name());
            tmpText1.setFont(new Font("Courier", 13));
            tmpText2.setFont(new Font("Courier", 13));
            
            switch (games.get(i).getResults()){
                case 0:
                    tmpText1.setFill(Color.WHITE);
                    tmpText2.setFill(Color.WHITE);
                    break;
                case 1:
                    tmpText1.setFill(Color.GREEN);
                    tmpText2.setFill(Color.RED);
                    break;
                default:
                    tmpText1.setFill(Color.RED);
                    tmpText2.setFill(Color.GREEN);
                    break;
            }

            results.add(tmpText1, 0, i);
            results.add(new Text("vs."), 1, i);
            results.add(tmpText2, 2, i);
            
        }
        return results;
        
    }
    
    /**
     * Refreshes the leaderboard gridpane with the most current information,
     * reorganizing the placing based on win count
     * @return 
     */
    private GridPane refreshLeaderboardGridPane(){
        
        GridPane leaderboard = new GridPane();
        leaderboard.setPadding(new Insets(5));
        leaderboard.setHgap(10);
        leaderboard.setBackground(new Background(new BackgroundFill(Color.GREY,
                                                 new CornerRadii(10), Insets.EMPTY)));
        leaderboard.setAlignment(Pos.CENTER);
        Text rankingText = new Text("Ranking");
        Text playerText = new Text("Player");
        Text winText = new Text("Wins");
        rankingText.setFont(textFont);
        playerText.setFont(textFont);
        winText.setFont(textFont);
        rankingText.setFill(Color.WHITE);
        playerText.setFill(Color.WHITE);
        winText.setFill(Color.WHITE);
        rankingText.setUnderline(true);
        playerText.setUnderline(true);
        winText.setUnderline(true);
        ColumnConstraints leftLeaderboardColumn = new ColumnConstraints();
        ColumnConstraints centerLeaderboardColumn = new ColumnConstraints();
        ColumnConstraints rightLeaderboardColumn = new ColumnConstraints();
        leftLeaderboardColumn.setHalignment(HPos.CENTER);
        centerLeaderboardColumn.setHalignment(HPos.CENTER);
        rightLeaderboardColumn.setHalignment(HPos.CENTER);
        centerLeaderboardColumn.setMinWidth(60);
        leaderboard.getColumnConstraints().add(0, leftLeaderboardColumn);
        leaderboard.getColumnConstraints().add(1, centerLeaderboardColumn);
        leaderboard.getColumnConstraints().add(2, rightLeaderboardColumn);
        leaderboard.add(rankingText, 0, 0);
        leaderboard.add(playerText, 1, 0);
        leaderboard.add(winText, 2, 0);
        players.forEach((player) -> {
            
            updatePlayerWinCount(player);
            
        });
        Collections.sort(players);
        
        for(int i = 0; i < players.size(); i++){

            Text tmpTextRank = new Text(Integer.toString(i + 1));
            tmpTextRank.setStyle("-fx-font-weight:bold");
            Text tmpTextName = new Text(players.get(players.size() - 1 - i).getName());
            Text tmpTextWins = new Text(Integer.toString(players.get(players.size() - 1 - i).getWins()));
            tmpTextRank.setFont(new Font("Courier", 18));
            tmpTextName.setFont(new Font("Courier", 16));
            tmpTextWins.setFont(new Font("Courier", 16));
            
            switch(i + 1){
                
                case 1:
                    tmpTextRank.setFill(Color.GOLD);
                    tmpTextRank.setFont(new Font("Courier", 26));
                    break;
                case 2:
                    tmpTextRank.setFill(Color.SILVER);
                    tmpTextRank.setFont(new Font("Courier", 22));
                    break;
                case 3:
                    tmpTextRank.setFill(Color.DARKGOLDENROD);
                    tmpTextRank.setFont(new Font("Courier", 22));
                    break;
                default:
                    tmpTextRank.setFill(Color.WHITE);
                
            }
            tmpTextName.setFill(Color.WHITE);
            tmpTextWins.setFill(Color.WHITE);
            
            leaderboard.add(tmpTextRank, 0, i + 1);
            leaderboard.add(tmpTextName, 1, i + 1);
            leaderboard.add(tmpTextWins, 2, i + 1);
            
        }
        
        return leaderboard;
        
    }
    
    /**
     * Sets the game entered's score and refreshed the results gridpane. This
     * method handles all tie breakers and cases. Number of parameters is used
     * for updating UI objects in the main page.
     * @param results
     * @param gameCheck
     * @param resultsVBox
     * @param game1Text
     * @param game2Text
     * @param gameChoiceNum
     * @param addPlayersButton
     * @param display1
     * @param display2
     * @param addResultsText
     * @param combinedScoreField
     * @param leaderboard
     * @param lbVBox 
     */
    private void setAndRefreshResults(GridPane results, CheckBox gameCheck,
                                      VBox resultsVBox, Text game1Text,
                                      Text game2Text, int gameChoiceNum,
                                      Button addPlayersButton, HBox display1,
                                      HBox display2, Text addResultsText,
                                      TextField combinedScoreField,
                                      GridPane leaderboard, VBox lbVBox){
        
        Node remove = resultsVBox.getChildren().remove(resultsVBox.getChildren().size() - 1);
        remove = null;
        Node remove1 = lbVBox.getChildren().remove(lbVBox.getChildren().size() - 1);
        remove1 = null;
        
        addPlayersButton.setVisible(false);
        
        games.get(getNextGameIndex()).setWinningTeam(gameChoiceNum);
            
        if(getNextGameIndex() != -1){
                
            game1Text.setText(games.get(getNextGameIndex()).getTeam1Name());
            if(getNextGameIndex() + 1 == games.size())
                display2.getChildren().get(display2.getChildren().size() - 2).setVisible(true);                

            game2Text.setText(games.get(getNextGameIndex()).getTeam2Name());
            gameCheck.setSelected(false);
            results = refreshResultsGridPane();
            resultsVBox.getChildren().add(results);
            
            leaderboard = refreshLeaderboardGridPane();
            lbVBox.getChildren().add(leaderboard);
        
            if(isSemifinalsPlayed()){

                int counter = 0;
            
                for(Game game:games){
                
                    if(game.getIfSemifinal()){
                    
                        if(game.getResults() == 1){
                        
                            if(counter == 0)
                                games.get(games.size() - 1).setTeam1Name(game.getTeam1Name());
                            else
                                games.get(games.size() - 1).setTeam2Name(game.getTeam1Name());
                        
                        }else{
                        
                            if(counter == 0)
                                games.get(games.size() - 1).setTeam1Name(game.getTeam2Name());
                            else
                                games.get(games.size() - 1).setTeam2Name(game.getTeam2Name());
                    
                        }
                    
                        counter++;

                    }
                
                }

            }
                
        }else{

            display1.setVisible(false);
            display2.setVisible(false);
            addResultsText.setVisible(false);
            games.get(games.size() - 1).setWinningTeam(gameChoiceNum);

            //Handles tie breaker math
            if(isTie()){

                Stage tieStage = new Stage();
                ImageView background = new ImageView(
                               new Image("image/startupBackground.jpg"));
                background.fitWidthProperty().bind(tieStage.widthProperty());
                background.fitHeightProperty().bind(tieStage.heightProperty());
                
                
                VBox tieVBox = new VBox();
                tieVBox.setAlignment(Pos.CENTER);
                tieVBox.setSpacing(10);
                Text gridTitle = new Text("Tie Breaker Final Scores");
                gridTitle.setUnderline(true);
                gridTitle.setFill(Color.WHITE);
                gridTitle.setStyle("-fx-font-weight:bold");
                gridTitle.setFont(new Font("Courier", 25));
                tieVBox.getChildren().add(gridTitle);
                GridPane tieBreakerGrid = new GridPane();
                tieBreakerGrid.setPadding(new Insets(5));
                tieBreakerGrid.setHgap(10);
                ColumnConstraints column = new ColumnConstraints();
                column.setHalignment(HPos.CENTER);
                for(int i = 0; i < getTieCount(); i++)
                    tieBreakerGrid.getColumnConstraints().add(i, column);
                
                tieBreakerGrid.setBackground(new Background(new BackgroundFill(Color.GREY,
                                                            new CornerRadii(10), Insets.EMPTY)));
                tieBreakerGrid.setAlignment(Pos.CENTER);

                for(int i = 0; i < getTieCount(); i++){

                    Text tmpTieText = new Text(players.get(players.size() - getTieCount() + i).getName());
                    tmpTieText.setFont(textFont);
                    tmpTieText.setUnderline(true);
                    tmpTieText.setFill(Color.WHITE);
                    tieBreakerGrid.add(tmpTieText, i, 0);
                    
                    Text tmpTieScore = new Text(Integer.toString(players.get(players.size() - getTieCount() + 1).getFinalCombinedScore()));
                    tmpTieScore.setFill(Color.WHITE);
                    tmpTieScore.setFont(textFont);
                    tieBreakerGrid.add(tmpTieScore, i, 1);
                    
                }
                tieVBox.getChildren().add(tieBreakerGrid);
                
                Text finalScoreText = new Text("");
                finalScoreText.setText("The combined score: " + combinedScoreField.getText());
                finalScoreText.setFont(new Font("Courier", 25));
                finalScoreText.setFill(Color.WHITE);
                tieVBox.getChildren().add(finalScoreText);

                StackPane pane = new StackPane();
                pane.getChildren().addAll(background, tieVBox);
                Scene tieScene = new Scene(pane, 300, 300);
                tieStage.setScene(tieScene);
                tieStage.setTitle("Jackson Football");
                tieStage.showAndWait();
                
            }
            
            results = refreshResultsGridPane();
            resultsVBox.getChildren().add(results);
            leaderboard = refreshLeaderboardGridPane();
            lbVBox.getChildren().add(leaderboard);
      
        }

    }

    /**
     * True if both semifinals have been played already
     * @return 
     */
    private boolean isSemifinalsPlayed(){
        
        int num = 0;
        for(Game game:games){
            
            if(game.getIfSemifinal() && game.getResults() != 0)
                num++;
            
        }
        
        return num == 2;
        
    }
    
    /**
     * True if there is a tie for first place after all games have been entered
     * @return 
     */
    private boolean isTie(){
        
        int highestScore = 0;
        
        for(Player player:players){
            
            if(player.getWins() > highestScore){
                
                highestScore = player.getWins();
                
            }
            
        }
        
        int counter = 0;
        
        for(Player player:players){
            
            if(player.getWins() == highestScore)
                counter++;
            
        }
        
        return counter > 1;
        
    }
    
    /**
     * Returns the amount of players that have tied for first place
     * @return 
     */
    private int getTieCount(){
        
        int highestScore = 0;
        
        for(Player player:players){
            
            if(player.getWins() > highestScore){
                
                highestScore = player.getWins();
                
            }
            
        }
        
        int counter = 0;
        
        for(Player player:players){
            
            if(player.getWins() == highestScore)
                counter++;
            
        }
        
        return counter;
        
    }
    
    /**
     * Updates the given player's win count, by traversing the game list and
     * adding up choice matches
     * @param player 
     */
    private void updatePlayerWinCount(Player player){
        
        int counter = 0;
        
        for(int i = 0; i < games.size(); i++){
            
            if(games.get(i).getResults() == player.getChoices().get(i))
                counter++;
            
        }
        
        player.setWinCount(counter);
        
    }

    /**
     * Launches the application
     * @param args 
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}