package websocket;

import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, Connection> connections = new ConcurrentHashMap<>();

    public void add(String authToken, Session session, int gameID) {
        var connection = new Connection(authToken, session, gameID);
        connections.put(gameID, connection);
    }

    public void remove(String authToken) {
        connections.remove(authToken);
    }

    public void broadcastAll(ServerMessage serverMessage, int gameID) throws IOException {
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (c.gameID == gameID) {
                c.send(serverMessage);
                }
            }
        }
    }

    public void broadcastUser(ServerMessage serverMessage, int gameID, String includeAuthToken) throws IOException {
        for (var c : connections.values()) {
            if (c.session.isOpen() && c.authToken == includeAuthToken) {
                if (c.gameID == gameID) {
                    c.send(serverMessage);
                }
            }
        }
    }

    public void broadcastAllButOne(ServerMessage serverMessage, int gameID, String excludeAuthToken) throws IOException {
        for (var c : connections.values()) {
            if (c.session.isOpen() && c.authToken != excludeAuthToken) {
                if (c.gameID == gameID) {
                    c.send(serverMessage);
                }
            }
        }
    }
}
