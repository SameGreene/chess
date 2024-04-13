package websocket;

import chess.*;
import com.google.gson.Gson;
import dataAccess.*;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.Collection;


@WebSocket (maxIdleTime = 1000000)
public class WebSocketHandler {

    // Text colors for messages
    private final String defaultColor = "\u001B[0m";
    private final String blueColor = "\u001B[34m";
    public UserDAO userObj = new SQLUserDAO();
    public AuthDAO authObj = new SQLAuthDAO();
    public GameDAO gameObj = new SQLGameDAO();
    private boolean foundAuth;

    private final ConnectionManager manager = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, InvalidMoveException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case JOIN_PLAYER -> {
                JoinPlayer joinPlayerCommand = new Gson().fromJson(message, JoinPlayer.class);
                joinGameAsPlayer(joinPlayerCommand.getAuthString(), joinPlayerCommand.getGameID(), joinPlayerCommand.getPlayerColor(), session);
            }
            case JOIN_OBSERVER -> {
                JoinObserver joinObserverCommand = new Gson().fromJson(message, JoinObserver.class);
                joinGameAsObserver(joinObserverCommand.getAuthString(), joinObserverCommand.getGameID(), session);
            }
            case MAKE_MOVE -> {
                MakeMove makeMoveCommand = new Gson().fromJson(message, MakeMove.class);
                makeMove(makeMoveCommand.getAuthString(), makeMoveCommand.getGameID(), makeMoveCommand.getMove());
            }
            case RESIGN -> {
                Resign resignCommand = new Gson().fromJson(message, Resign.class);
                resign(resignCommand.getAuthString(), resignCommand.getGameID());
            }
            case LEAVE -> {
                Leave leaveCommand = new Gson().fromJson(message, Leave.class);
                leave(leaveCommand.getAuthString(), leaveCommand.getGameID(), session);
            }
        }
    }

    private void leave(String authToken, int gameID, Session session) throws IOException {
        var leaveMessage = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, "User " + blueColor + authObj.getUser(authToken)
                + defaultColor + " has left the game.");
        manager.broadcastAllButOne(leaveMessage, gameID, authToken);
        manager.remove(session);
    }

    private void resign(String authToken, int gameID) throws IOException {
        // Perform checks
        authCheck(authToken, gameID);
        GameData gameData = gameObj.getGame(gameID - 1);
        ChessGame game = null;
        if (gameData != null) {
            game = gameData.game();
        }

        // Is the game over?
        if (gameData.game().isGameOver()) {
            var errorMessage = new Error(ServerMessage.ServerMessageType.ERROR, "The game is already over.");
            manager.broadcastUser(errorMessage, gameID, authToken);
        }
        else {
            // Is the player resigning a valid player?
            if (authObj.getUser(authToken).equals(gameData.whiteUsername()) || authObj.getUser(authToken).equals(gameData.blackUsername())) {
                // Set gameOver flag of the game and update it in the DAO
                game.setGameOver(true);
                gameObj.setGame(gameID - 1, gameData);

                var resignMessage = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, "User " + blueColor + authObj.getUser(authToken)
                        + defaultColor + " has resigned.");
                manager.broadcastAll(resignMessage, gameID);
            } else {
                var errorMessage = new Error(ServerMessage.ServerMessageType.ERROR, "The observer cannot resign.");
                manager.broadcastUser(errorMessage, gameID, authToken);
            }
        }
    }

    private void makeMove(String authToken, int gameID, ChessMove moveToMake) throws IOException, InvalidMoveException {
        // Perform checks
        authCheck(authToken, gameID);
        GameData gameData = gameObj.getGame(gameID - 1);
        ChessGame game = null;
        if (gameData != null) {
            game = gameData.game();
        }

        // Is the game over?
        if (gameData.game().isGameOver()) {
            var errorMessage = new Error(ServerMessage.ServerMessageType.ERROR, "The game is already over.");
            manager.broadcastUser(errorMessage, gameID, authToken);
        }
        // Are we in checkmate?
        else if (gameData.game().isInCheckmate(ChessGame.TeamColor.WHITE) || gameData.game().isInCheckmate(ChessGame.TeamColor.BLACK)) {
            var errorMessage = new Error(ServerMessage.ServerMessageType.ERROR, "Game over.");
            manager.broadcastUser(errorMessage, gameID, authToken);
            // Set gameOver flag of the game and update it in the DAO
            game.setGameOver(true);
            gameObj.setGame(gameID - 1, gameData);
        }
        else {
            String currentUser = authObj.getUser(authToken);
            // Is it the player's turn?
            if (game.getTeamTurn() != null && ((game.getTeamTurn() == ChessGame.TeamColor.BLACK && gameData.blackUsername().equals(currentUser)) ||
                    (game.getTeamTurn() == ChessGame.TeamColor.WHITE && gameData.whiteUsername().equals(currentUser)))) {
                // Is the move valid?
                Collection <ChessMove> validMoves = game.validMoves(moveToMake.getStartPosition());
                if (validMoves != null && validMoves.contains(moveToMake)) {
                    // Make the move
                    game.makeMove(moveToMake);
                    gameObj.setGame(gameID - 1, gameData);
                    // Load the game for everyone
                    var loadGameMessage = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, game, game.getTeamTurn());
                    manager.broadcastAll(loadGameMessage, gameID);
                    // Notify everyone else that a move was made by the player
                    var moveMessage = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, "User " + blueColor + authObj.getUser(authToken)
                            + defaultColor + " has made a move.");
                    manager.broadcastAllButOne(moveMessage, gameID, authToken);
                }
                else {
                    // Move can't be made. Notify user of invalid move.
                    var errorMessage = new Error(ServerMessage.ServerMessageType.ERROR, "Invalid move");
                    manager.broadcastUser(errorMessage, gameID, authToken);
                }
            }
            else {
                // Not the player's turn
                var errorMessage = new Error(ServerMessage.ServerMessageType.ERROR, "It's not your turn.");
                manager.broadcastUser(errorMessage, gameID, authToken);
            }
        }
    }

    private void joinGameAsPlayer(String authToken, int gameID, ChessGame.TeamColor playerColor, Session session) throws IOException {
        manager.add(authToken, session, gameID);

        // Perform checks
        authCheck(authToken, gameID);
        GameData gameData = gameObj.getGame(gameID - 1);
        ChessGame game = null;
        if (gameData != null) {
            game = gameData.game();
        }
        gameCheck(authToken, gameID, gameData);

        // Is the game over?
        if (gameData.game().isGameOver()) {
            var errorMessage = new Error(ServerMessage.ServerMessageType.ERROR, "The game is already over.");
            manager.broadcastUser(errorMessage, gameID, authToken);
        }

        else {
            // Is the user that is trying to join on the right team?
            // If the requesting player is black, and their username matches the blackUsername, good to go
            // Same for white
            String reqUser = authObj.getUser(authToken);
            String whiteUser = gameData.whiteUsername();
            String blackUser = gameData.blackUsername();
            if ((playerColor == ChessGame.TeamColor.BLACK && reqUser.equals(blackUser)) || (playerColor == ChessGame.TeamColor.WHITE && reqUser.equals(whiteUser))) {
                // Send back a load game message with a game inside it to the user who just joined
                var loadGameMessage = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, game, playerColor);
                manager.broadcastUser(loadGameMessage, gameID, authToken);
            } else {
                var errorMessage = new Error(ServerMessage.ServerMessageType.ERROR, "You are not on that team.");
                manager.broadcastUser(errorMessage, gameID, authToken);
            }

            // Notify everyone else that the player has joined
            var joinMessage = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, "User " + blueColor + authObj.getUser(authToken)
                    + defaultColor + " has joined the game.");
            manager.broadcastAllButOne(joinMessage, gameID, authToken);
        }
    }

    private void joinGameAsObserver(String authToken, int gameID, Session session) throws IOException {
        manager.add(authToken, session, gameID);

        // Perform checks
        authCheck(authToken, gameID);
        GameData gameData = gameObj.getGame(gameID - 1);
        gameCheck(authToken, gameID, gameData);

        // Is the game over?
        if (gameData.game().isGameOver()) {
            var errorMessage = new Error(ServerMessage.ServerMessageType.ERROR, "The game is already over. Type 'help' for a list of commands.");
            manager.broadcastUser(errorMessage, gameID, authToken);
        }

        else {
            if (foundAuth) {
                var observeMessage = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, gameObj.getGame(gameID - 1).game(), null);
                manager.broadcastUser(observeMessage, gameID, authToken);

                var joinMessage = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, "User " + blueColor + authObj.getUser(authToken)
                        + defaultColor + " has joined the game.");
                manager.broadcastAllButOne(joinMessage, gameID, authToken);
            }
        }
    }

    private void gameCheck(String authToken, int gameID, GameData gameData) throws IOException{
        ChessGame game = null;
        if (gameData != null) {
            game = gameObj.getGame(gameID - 1).game();
            if (game != null && gameData.gameID() == gameID) {

            } else {
                var errorMessage = new Error(ServerMessage.ServerMessageType.ERROR, "Bad GameID");
                manager.broadcastUser(errorMessage, gameID, authToken);
            }
        } else {
            var errorMessage = new Error(ServerMessage.ServerMessageType.ERROR, "Bad GameID");
            manager.broadcastUser(errorMessage, gameID, authToken);
        }
    }

    private void authCheck(String authToken, int gameID) throws IOException {
        // Check that the provided authToken is in the database
        foundAuth = false;
        for (int i = 0; i <= authObj.getSize(); i++){
            if (authObj.getAuthByID(i) != null && authObj.getAuthByID(i).authToken().equals(authToken)) {
                foundAuth = true;
            }
        }

        if (!foundAuth) {
            var errorMessage = new Error(ServerMessage.ServerMessageType.ERROR, "Bad AuthToken");
            manager.broadcastUser(errorMessage, gameID, authToken);
        }
    }
}