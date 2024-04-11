package websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinObserver;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.util.Timer;


@WebSocket
public class WebSocketHandler {

    // Text colors for messages
    String defaultColor = "\u001B[0m";
    String blueColor = "\u001B[34m";

    private final ConnectionManager manager = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case JOIN_PLAYER -> {
                JoinPlayer joinPlayerCommand = new Gson().fromJson(message, JoinPlayer.class);
                joinGameAsPlayer(joinPlayerCommand.getAuthString(), joinPlayerCommand.getGameID(), joinPlayerCommand.getPlayerColor(), session);
            }

            case MAKE_MOVE -> enter(action.visitorName(), session);
            case LEAVE -> enter(action.visitorName(), session);
            case RESIGN -> exit(action.visitorName());
        }
    }

    private void joinGameAsPlayer(String authToken, int gameID, ChessGame.TeamColor playerColor, Session session) throws IOException {
        manager.add(authToken, session);
        var joinMessage = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, "User " + defaultColor + "has joined the game.");
        manager.broadcast(authToken, joinMessage);
    }

    private void joinGameAsObserver(String authToken, int gameID, Session session) throws IOException {
        manager.add(authToken, session);
        var observeMessage = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, "User " + defaultColor + "is now observing.");
        manager.broadcast(authToken, observeMessage);
    }

//    private void exit(String visitorName) throws IOException {
//        manager.remove(visitorName);
//        var message = String.format("%s left the shop", visitorName);
//        var serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
//        man.broadcast(visitorName, serverMessage);
//    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.println("A client has connected");
        manager.add("REPLACE_AUTHTOKEN", session);
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        manager.remove("REPLACE_AUTHTOKEN");
    }

}