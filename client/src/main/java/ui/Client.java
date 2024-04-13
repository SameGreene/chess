package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;
import java.util.List;

public class Client implements NotificationHandler {

    public static WebSocketFacade webSocketFacade;
    public static ServerFacade serverFacade;
    public static NotificationHandler notificationHandler;

    public Client(String[] args) throws Exception {
        webSocketFacade = new WebSocketFacade("http://localhost:" + args[0], this);
        serverFacade = new ServerFacade("http://localhost", Integer.parseInt(args[0]));
    }

    // TEXT COLORS
    private final static String defaultColor = "\u001B[0m";
    private final static String blueColor = "\u001B[34m";

    private static boolean isPlayer;

    private static ChessBoard board = null;
    private static String teamColor = null;
    private static int gameID;
    private static ChessGame chessGame;

    // STATES
    // 0 - PRE-LOGIN
    // 1 - POST-LOGIN
    // 2 - GAMEPLAY
    // 3 - WAITING
    private static int userState;

    public static void main(String[] args) throws Exception {
        Client client = new Client(args);
        // Print out welcome screen
        System.out.println("Welcome to CS 240 Chess\nType 'help' to get started");

        // Start in pre-login
        userState = 0;

        // User input Scanner
        Scanner userInput = new Scanner(System.in);

        // TEXT COLORS
        String defaultColor = "\u001B[0m";
        String blueColor = "\u001B[34m";

        ChessBoard board = null;
        String teamColor = null;

        isPlayer = false;

        while (true) {
            switch (userState) {
                // Not logged in
                case 0:
                    preLogin(args, client, userInput);
                    break;

                // Logged in
                case 1:
                    loggedIn(args, client, userInput);
                    break;

                // In a game
                case 2:
                    inGame(args, client, userInput);
                    break;

                case 3:
                    waiting(args, client, userInput);
                    break;

                // Error encountered. Exit the program
                default:
                    System.out.println("Unknown state error occurred. Exiting...");
                    return;
            }
        }
    }

    private static void waiting(String[] args, Client client, Scanner userInput) {
        while (userState == 3) {
            System.out.println("Attempting to join...");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void inGame(String[] args, Client client, Scanner userInput) {
        String[] splitGameInput = userInput.nextLine().split("\\s+");

        // redraw
        if (splitGameInput[0].equals("redraw")) {
            System.out.println(board.toString(teamColor));
        }
        // leave
        else if (splitGameInput[0].equals("leave")) {
            try {
                client.webSocketFacade.leaveGame(serverFacade.getAuthToken(), gameID);
                userState = 1;
                System.out.println("Left game. Type 'help' for a list of commands.");
            } catch (Exception e) {
                System.out.println("Failed to leave game. Please type 'help' for a list of commands.");
            }

        }
        // move
        else if (splitGameInput[0].equals("move")){
            // Is it a valid player?
            if (isPlayer) {
                // Translate input from commandline into a ChessMove
                String from = splitGameInput[1];
                ChessPosition fromPos = new ChessPosition(from.charAt(1) - '0', from.charAt(0) - 'a' + 1);
                String to = splitGameInput[2];
                ChessPosition toPos = new ChessPosition(to.charAt(1) - '0', to.charAt(0) - 'a' + 1);

                try {
                    client.webSocketFacade.makeMove(serverFacade.getAuthToken(), gameID, new ChessMove(fromPos, toPos, null));
                } catch (Exception e){System.out.println("Failed to process move move. Ensure it is a valid move.");}
            }
            else {
                System.out.println("Cannot move as an observer. Type 'help' for a list of commands");
            }
        }
        // resign
        else if (splitGameInput[0].equals("resign")){
            try {
                if (isPlayer) {
                    client.webSocketFacade.resign(serverFacade.getAuthToken(), gameID);
                    userState = 1;
                    System.out.println("Resigned. Type 'help' for a list of commands.");
                    // Mark game as game over and leave back to lobby
                    isPlayer = false;
                }
                else {
                    System.out.println("Cannot resign as an observer. Type 'help' for a list of commands");
                }
            } catch (Exception e) {
                System.out.println("Failed to resign. Please type 'help' for a list of commands.");
            }
        }
        // highlight
        else if (splitGameInput[0].equals("highlight")){
            // Highlight all possible moves given a piece
                // Local. No server interaction needed
            String pieceToCheck = splitGameInput[1];
            ChessPosition piecePos = new ChessPosition(pieceToCheck.charAt(0) - 'a' + 1, pieceToCheck.charAt(1) - '0');
            Collection<ChessMove> possibleMoves = chessGame.validMoves(piecePos);
            ChessBoard highlightedBoard = board;

            System.out.println(highlightedBoard.toStringHighlighted("WHITE", possibleMoves));
        }
        // help
        else if (splitGameInput[0].equals("help")) {
            System.out.println("redraw" + blueColor + " - Redraw the board in its current state" + defaultColor);
            System.out.println("leave" + blueColor + " - Leave the current match" + defaultColor);
            System.out.println("move <FROM_HERE> <TO_HERE> (ColumnRow, i.e. d7)" + blueColor + " - Move a piece" + defaultColor);
            System.out.println("resign" + blueColor + " - Admit defeat" + defaultColor);
            System.out.println("highlight <PIECE_HERE>" + blueColor + " - Highlight all possible moves for a given piece" + defaultColor);
            System.out.println("help" + blueColor + " - List available commands" + defaultColor);
        }

        // error handling
        else {
            System.out.println("ERROR: Unknown command or incorrect syntax. Please type 'help' for a list of commands.");
        }
    }

    private static void preLogin(String[] args, Client client, Scanner userInput) {
        String[] splitPreInput = userInput.nextLine().split("\\s+");
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
    }

    private static void loggedIn(String[] args, Client client, Scanner userInput) {
        String[] splitPostInput = userInput.nextLine().split("\\s+");
        // create
        if (splitPostInput[0].equals("create") && splitPostInput.length == 2) {
            String gameName = splitPostInput[1];

            try {
                CreateGameResponse response = client.serverFacade.create(gameName);
                // Successful. Print success message
                System.out.println("Successfully created game " + blueColor + gameName + defaultColor);
            } catch (Exception e) {System.out.println("Failed to create game. Be sure you gave the game a unique name. Please type 'help' for a list of commands.");}
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
            } catch (Exception e) {System.out.println("Couldn't retrieve list of games. Please type 'help' for a list of commands.");}
        }
        // join with team color
        else if (splitPostInput[0].equals("join") && splitPostInput.length == 3) {
            int joinGameID = Integer.parseInt(splitPostInput[1]);
            teamColor = splitPostInput[2];
            gameID = joinGameID;

            try {
                JoinGameResponse response = client.serverFacade.join(teamColor, gameID);
                ChessGame.TeamColor teamColorType = ChessGame.TeamColor.valueOf(teamColor.toUpperCase());
                try {
                    client.webSocketFacade.joinGameAsPlayer(serverFacade.getAuthToken(), gameID, teamColorType);
                    isPlayer = true;
                    userState = 3;
                } catch (Exception e){System.out.println("WebSocket connection failed. Please type 'help' for a list of commands.");}
            } catch (Exception e){System.out.println("HTTP request failed. Please type 'help' for a list of commands.");}
        }
        // observe
        else if ((splitPostInput[0].equals("observe") && splitPostInput.length == 2) || (splitPostInput[0].equals("join") && splitPostInput.length == 2)) {
            int joinGameID = Integer.parseInt(splitPostInput[1]);
            gameID = joinGameID;

            try {
                JoinGameResponse response =  client.serverFacade.join(null, gameID);
                try {
                    client.webSocketFacade.joinGameAsObserver(serverFacade.getAuthToken(), gameID);
                    isPlayer = false;
                    userState = 3;
                } catch (Exception e){System.out.println("WebSocket connection failed. Please type 'help' for a list of commands.");}
            } catch (Exception e){System.out.println("Failed to observe. Please type 'help' for a list of commands.");}
        }
        // logout
        else if (splitPostInput[0].equals("logout")) {
            try {
                client.serverFacade.logout();
                System.out.println("Successfully logged out.");
                System.out.println("Type 'help' to get started.");
                userState = 0;
            } catch (Exception e){System.out.println("Failed to log out. Please type 'help' for a list of commands.");}
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
        else {System.out.println("ERROR: Unknown command or incorrect syntax. Please type 'help' for a list of commands.");}
    }

    @Override
    public void notify(String game) {
        ServerMessage serverMessage = new Gson().fromJson(game, ServerMessage.class);
        switch (serverMessage.getServerMessageType()) {
            case LOAD_GAME -> {
                LoadGame loadGameMessage = new Gson().fromJson(game, LoadGame.class);
                board = loadGameMessage.getGameBoard();
                chessGame = loadGameMessage.getChessGame();
                if (userState != 2) {
                    if (isPlayer) {
                        System.out.println("Joined successfully. Type 'help' for a list of commands.");
                    }
                    else {
                        System.out.println("Observing successfully. Type 'help' for a list of commands.");
                    }
                }
                userState = 2;
                System.out.println(board.toString(teamColor));
            }
            case NOTIFICATION -> {
                Notification notification = new Gson().fromJson(game, Notification.class);
                System.out.println(notification.getMessage());

                // When a player resigns
            }
            case ERROR -> {
                Error errorMessage = new Gson().fromJson(game, Error.class);
                System.out.println(errorMessage.getErrorMessage());

                // Are we trying to join a game?
                if (userState == 3) {
                    // Send them back to the lobby
                    userState = 1;
                }
            }
        }
    }
}
