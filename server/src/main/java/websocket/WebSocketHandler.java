package websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
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
import webSocketMessages.userCommands.JoinObserver;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.MakeMove;
import webSocketMessages.userCommands.UserGameCommand;

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
                makeMove(makeMoveCommand.getAuthString(), makeMoveCommand.getGameID(), session, makeMoveCommand.getMove());
            }

//            case MAKE_MOVE -> enter(action.visitorName(), session);
//            case LEAVE -> enter(action.visitorName(), session);
//            case RESIGN -> exit(action.visitorName());
        }
    }

    private void makeMove(String authToken, int gameID, Session session, ChessMove moveToMake) throws IOException, InvalidMoveException {
        // Perform checks
        authCheck(authToken, gameID);
        GameData gameData = gameObj.getGame(gameID - 1);
        ChessGame game = null;
        if (gameData != null) {
            game = gameData.game();
        }

        // Is the move valid?
        Collection <ChessMove> validMoves = game.validMoves(moveToMake.getStartPosition());
        if (validMoves != null && validMoves.contains(moveToMake)) {
            // Make the move
            game.makeMove(moveToMake);
            // Load the game for everyone
            var loadGameMessage = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, game);
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

        // Is the user that is trying to join on the requested team?
        // If the requesting player is black, and their username matches the blackUsername, good to go
        // Same for white
        String reqUser = authObj.getUser(authToken);
        String whiteUser = gameData.whiteUsername();
        String blackUser = gameData.blackUsername();
        if ((playerColor == ChessGame.TeamColor.BLACK && reqUser.equals(blackUser)) || (playerColor == ChessGame.TeamColor.WHITE && reqUser.equals(whiteUser))) {
            // Send back a load game message with a game inside it to the user who just joined
            var loadGameMessage = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, game);
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

    private void joinGameAsObserver(String authToken, int gameID, Session session) throws IOException {
        manager.add(authToken, session, gameID);

        // Perform checks
        authCheck(authToken, gameID);
        GameData gameData = gameObj.getGame(gameID - 1);
        gameCheck(authToken, gameID, gameData);

        if (foundAuth) {
            var observeMessage = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, gameObj.getGame(gameID - 1).game());
            manager.broadcastUser(observeMessage, gameID, authToken);

            var joinMessage = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, "User " + blueColor + authObj.getUser(authToken)
                    + defaultColor + " has joined the game.");
            manager.broadcastAllButOne(joinMessage, gameID, authToken);
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

//    private void exit(String visitorName) throws IOException {
//        manager.remove(visitorName);
//        var message = String.format("%s left the shop", visitorName);
//        var serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
//        man.broadcast(visitorName, serverMessage);
//    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        manager.remove(session);
    }

}