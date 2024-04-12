package websocket;

import chess.ChessGame;
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
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;


@WebSocket (maxIdleTime = 1000000)
public class WebSocketHandler {

    // Text colors for messages
    private final String defaultColor = "\u001B[0m";
    private final String blueColor = "\u001B[34m";
    public UserDAO userObj = new SQLUserDAO();
    public AuthDAO authObj = new SQLAuthDAO();
    public GameDAO gameObj = new SQLGameDAO();

    private final ConnectionManager manager = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case JOIN_PLAYER -> {
                JoinPlayer joinPlayerCommand = new Gson().fromJson(message, JoinPlayer.class);
                joinGameAsPlayer(joinPlayerCommand.getAuthString(), joinPlayerCommand.getGameID(), joinPlayerCommand.getPlayerColor(), session);
            }

//            case MAKE_MOVE -> enter(action.visitorName(), session);
//            case LEAVE -> enter(action.visitorName(), session);
//            case RESIGN -> exit(action.visitorName());
        }
    }

    private void joinGameAsPlayer(String authToken, int gameID, ChessGame.TeamColor playerColor, Session session) throws IOException {
        manager.add(authToken, session, gameID);

        // Check that the provided authToken is in the database
        boolean foundAuth = false;
        for (int i = 0; i <= authObj.getSize(); i++){
            if (authObj.getAuthByID(i) != null && authObj.getAuthByID(i).authToken().equals(authToken)) {
                foundAuth = true;
            }
        }

        if (!foundAuth) {
            var errorMessage = new Error(ServerMessage.ServerMessageType.ERROR, "Bad AuthToken");
            manager.broadcastUser(errorMessage, gameID, authToken);
            return;
        }

        // Check if game exists
        GameData gameData = gameObj.getGame(gameID - 1);
        if (gameData != null) {
            ChessGame game = gameObj.getGame(gameID - 1).game();
            if (game != null && gameData.gameID() == gameID) {
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
            } else {
                var errorMessage = new Error(ServerMessage.ServerMessageType.ERROR, "Bad GameID");
                manager.broadcastUser(errorMessage, gameID, authToken);
            }
        } else {
            var errorMessage = new Error(ServerMessage.ServerMessageType.ERROR, "Bad GameID");
            manager.broadcastUser(errorMessage, gameID, authToken);
        }

        // Notify everyone else that the player has joined
        var joinMessage = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, "User " + blueColor + authObj.getUser(authToken)
                + defaultColor + " has joined the game.");
        manager.broadcastAllButOne(joinMessage, gameID, authToken);
    }

    private void joinGameAsObserver(String authToken, int gameID, Session session) throws IOException {
        manager.add(authToken, session, gameID);
        var observeMessage = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, "User " + blueColor + authObj.getUser(authToken)
                + defaultColor + " is now observing.");
        manager.broadcastAll(observeMessage, gameID);
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