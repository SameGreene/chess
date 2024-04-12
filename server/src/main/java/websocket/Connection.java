package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;

public class Connection {
    public String authToken;
    public Session session;
    public Integer gameID;

    public Connection(String authToken, Session session, int gameID) {
        this.authToken = authToken;
        this.session = session;
        this.gameID = gameID;
    }

    public void send(ServerMessage msg) throws IOException {
        try {
            session.getRemote().sendString(new Gson().toJson(msg));
        }
        catch (IOException e) {
            System.out.println("Error: Failed to send message");
        }
    }
}