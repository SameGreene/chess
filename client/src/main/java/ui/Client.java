package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import chess.ChessBoard;
import response.CreateGameResponse;
import response.JoinGameResponse;
import response.ListGamesResponse;
import response.LoginResponse;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;

import javax.swing.tree.AbstractLayoutCache;
import java.awt.*;
import java.util.Scanner;
import java.util.List;

public class Client implements NotificationHandler {

    public static WebSocketFacade webSocketFacade;
    public static ServerFacade serverFacade;
    public static NotificationHandler notificationHandler;

    public Client(String[] args) throws Exception {
//        webSocketFacade = new WebSocketFacade("http://localhost", );
        serverFacade = new ServerFacade("http://localhost", Integer.parseInt(args[0]));
    }

    public static void main(String[] args) throws Exception {
        Client client = new Client(args);
        // Print out welcome screen
        System.out.println("Welcome to CS 240 Chess\nType 'help' to get started");

        // STATES
        // 0 - PRE-LOGIN
        // 1 - POST-LOGIN
        // 2 - GAMEPLAY
        int userState = 0;

        // Player vs Observer
        boolean isPlayer = false;

        // User input Scanner
        Scanner userInput = new Scanner(System.in);

        // TEXT COLORS
        String defaultColor = "\u001B[0m";
        String blueColor = "\u001B[34m";

        while (true) {
            switch (userState) {
                // Not logged in
                case 0:
                    String preInput = userInput.nextLine();
                    String[] splitPreInput = preInput.split("\\s+");
                    // register
                    if (splitPreInput[0].equals("register") && splitPreInput.length == 4) {
                        String username = splitPreInput[1];
                        String password = splitPreInput[2];
                        String email = splitPreInput[3];

                        try {
                            client.serverFacade.register(username, password, email);
                            // Successful. Print success message and move to post state
                            System.out.println("Logged in as " + blueColor + username + defaultColor);
                            System.out.println("Type 'help' for a list of available options.");
                            userState = 1;
                        } catch (Exception e) {
                            System.out.println("Failed to register. Be sure you have a unique username.");
                        }
                    }
                    // login
                    else if (splitPreInput[0].equals("login") && splitPreInput.length == 3) {
                        String username = splitPreInput[1];
                        String password = splitPreInput[2];

                        try {
                            LoginResponse response = client.serverFacade.login(username, password);
                            // Successful. Print success message and move to post state
                            System.out.println("Logged in as " + blueColor + username + defaultColor);
                            System.out.println("Type 'help' for a list of available options.");
                            userState = 1;
                        } catch (Exception e) {
                            System.out.println("Failed to log in. Check your credentials and try again.");
                        }
                    }
                    // quit
                    else if (splitPreInput[0].equals("quit")) {
                        System.out.println("Exiting... Goodbye!");
                        return;
                    }
                    // help
                    else if (splitPreInput[0].equals("help")) {
                        System.out.println("register <USERNAME> <PASSWORD> <EMAIL>" + blueColor + " - Register a new user" + defaultColor);
                        System.out.println("login <USERNAME> <PASSWORD>" + blueColor + " - Login with existing credentials" + defaultColor);
                        System.out.println("quit" + blueColor + " - Exit the game" + defaultColor);
                        System.out.println("help" + blueColor + " - List available commands" + defaultColor);
                    }
                    // error handling
                    else {
                        System.out.println("ERROR: Unknown command or incorrect syntax. Please type 'help' for a list of commands");
                    }
                    break;

                // Logged in
                case 1:
                    String postInput = userInput.nextLine();
                    String[] splitPostInput = postInput.split("\\s+");
                    // create
                    if (splitPostInput[0].equals("create") && splitPostInput.length == 2) {
                        String gameName = splitPostInput[1];

                        try {
                            CreateGameResponse response = client.serverFacade.create(gameName);
                            // Successful. Print success message
                            System.out.println("Successfully created game " + blueColor + gameName + defaultColor);
                        } catch (Exception e) {
                            System.out.println("Failed to create game. Be sure you gave the game a unique name. Please type 'help' for a list of commands.");
                        }
                    }
                    // list
                    else if (splitPostInput[0].equals("list")) {
                        try {
                            ListGamesResponse response = client.serverFacade.list();
                            // Successful. List all games
                            System.out.println("Available Games\n----------------");
                            List<GameData> gameList = response.getGames();

                            int listNum = 1;
                            for (GameData game : gameList) {

                                System.out.println(listNum + ". Game ID - " + blueColor + game.gameID() + defaultColor + " | Game Name - " +
                                       blueColor + game.gameName() + defaultColor + " | White Player - " + blueColor +
                                        game.whiteUsername() + defaultColor + " | Black Player - " + blueColor + game.blackUsername() +
                                        defaultColor + " |");
                                listNum++;
                            }
                        } catch (Exception e) {
                            System.out.println("Couldn't retrieve list of games. Please type 'help' for a list of commands.");
                        }
                    }
                    // join with team color
                    else if (splitPostInput[0].equals("join") && splitPostInput.length == 3) {
                        int gameID = Integer.parseInt(splitPostInput[1]);
                        String teamColor = splitPostInput[2];

                        try {
                            JoinGameResponse response = client.serverFacade.join(teamColor, gameID);
                            ChessGame.TeamColor teamColorType = ChessGame.TeamColor.valueOf(teamColor.toUpperCase());
                            client.webSocketFacade.joinGameAsPlayer(serverFacade.getAuthToken(), gameID, teamColorType);
                            ChessBoard board = response.game.getBoard();
                            System.out.println(board.toString(teamColor));
                            isPlayer = true;
                            userState = 2;
                        } catch (Exception e){
                            System.out.println("Failed to join. Be sure you picked a valid Game ID. The game could be full, or the team color you specified is taken. Please type 'help' for a list of commands.");
                        }
                    }
                    // observe
                    else if ((splitPostInput[0].equals("observe") && splitPostInput.length == 2) || (splitPostInput[0].equals("join") && splitPostInput.length == 2)) {
                        int gameID = Integer.parseInt(splitPostInput[1]);

                        try {
                            JoinGameResponse response =  client.serverFacade.join(null, gameID);
                            client.webSocketFacade.joinGameAsObserver(serverFacade.getAuthToken(), gameID);
                            ChessBoard board = response.game.getBoard();
                            board.resetBoard();
                            System.out.println(board.toString("WHITE"));
                            userState = 2;
                        } catch (Exception e){
                            System.out.println("Failed to observe. Please type 'help' for a list of commands.");
                        }
                    }
                    // logout
                    else if (splitPostInput[0].equals("logout")) {
                        try {
                            client.serverFacade.logout();
                            // Successful. Print success message and move to pre stage
                            System.out.println("Successfully logged out.");
                            System.out.println("Type 'help' to get started.");
                            userState = 0;
                        } catch (Exception e){
                            System.out.println("Failed to log out. Please type 'help' for a list of commands.");
                        }
                    }
                    // quit
                    else if (splitPostInput[0].equals("quit")) {
                        System.out.println("Exiting... Goodbye!");
                        return;
                    }
                    // help
                    else if (splitPostInput[0].equals("help")) {
                        System.out.println("create <GAME_NAME>" + blueColor + " - Create a game" + defaultColor);
                        System.out.println("list" + blueColor + " - Show available games" + defaultColor);
                        System.out.println("join <ID> [WHITE|BLACK|<empty>]" + blueColor + " - Join game by ID" + defaultColor);
                        System.out.println("observe <ID>" + blueColor + " - Watch a game" + defaultColor);
                        System.out.println("logout" + blueColor + " - Logout" + defaultColor);
                        System.out.println("quit" + blueColor + " - Exit the game" + defaultColor);
                        System.out.println("help" + blueColor + " - List available commands" + defaultColor);
                    }
                    // error handling
                    else {
                        System.out.println("ERROR: Unknown command or incorrect syntax. Please type 'help' for a list of commands.");
                    }
                    break;

                // In a game
                case 2:
                    String gameInput = userInput.nextLine();
                    String[] splitGameInput = gameInput.split("\\s+");

                    // redraw
                    if (splitGameInput[0].equals("redraw")) {

                    }
                    // leave
                    else if (splitGameInput[0].equals("leave")) {
//                        System.out.println("Exiting... Goodbye!");
//                        return;
                    }
                    // move
                    else if (splitGameInput[0].equals("move")){
                        // Translate input from commandline into a ChessMove

                    }
                    // resign
                    else if (splitGameInput[0].equals("resign")){

                    }
                    // highlight
                    else if (splitGameInput[0].equals("resign")){

                    }
                    // help
                    else if (splitGameInput[0].equals("help")) {
                        System.out.println("redraw" + blueColor + " - Redraw the board in its current state" + defaultColor);
                        System.out.println("leave" + blueColor + " - Leave the current match" + defaultColor);
                        System.out.println("move <FROM_HERE> <TO_HERE>" + blueColor + " - Move a piece" + defaultColor);
                        System.out.println("resign" + blueColor + " - Admit defeat" + defaultColor);
                        System.out.println("highlight" + blueColor + " - Highlight all possible moves" + defaultColor);
                        System.out.println("help" + blueColor + " - List available commands" + defaultColor);
                    }

                    // error handling
                    else {
                        System.out.println("ERROR: Unknown command or incorrect syntax. Please type 'help' for a list of commands.");
                    }
                    break;

                // Error encountered. Exit the program
                default:
                    System.out.println("Unknown state error occurred. Exiting...");
                    return;
            }
        }
    }

    @Override
    public void notify(String message) {
        ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
        switch (serverMessage.getServerMessageType()) {
            case LOAD_GAME -> {
                LoadGame loadGameMessage = new Gson().fromJson(message, LoadGame.class);
                // Load game
            }
            case NOTIFICATION -> {
                Notification notification = new Gson().fromJson(message, Notification.class);
                // Notify
            }
            case ERROR -> {
                Error errorMessage = new Gson().fromJson(message, Error.class);
                // Error
            }
        }
    }
}
