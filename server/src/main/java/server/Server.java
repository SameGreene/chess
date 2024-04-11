package server;

import dataAccess.*;
import handler.*;
import spark.*;
import websocket.WebSocketHandler;

public class Server {

    public UserDAO userObj = new SQLUserDAO();
    public AuthDAO authObj = new SQLAuthDAO();
    public GameDAO gameObj = new SQLGameDAO();

    public static void main(String[] args) {
        Server myServer = new Server();
        myServer.run(Integer.parseInt(args[0]));
    }

    public int run(int desiredPort) {

        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Establish WebSocket
        Spark.webSocket("/connect", WebSocketHandler.class);

        try {
            DatabaseManager.createDatabase();
            userObj.updateIndex();
            authObj.updateIndex();
            gameObj.updateIndex();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", ((request, response) -> new ClearHandler().handle(request, response, userObj, authObj, gameObj)));
        Spark.post("/user", ((request, response) -> new RegisterHandler().handle(request, response, userObj, authObj)));
        Spark.post("/session", ((request, response) -> new LoginHandler().handle(request, response, userObj, authObj)));
        Spark.delete("/session", ((request, response) -> new LogoutHandler().handle(request, response, authObj)));
        Spark.get("/game", ((request, response) -> new ListGamesHandler().handle(request, response, authObj, gameObj)));
        Spark.post("/game", ((request, response) -> new CreateGameHandler().handle(request, response, authObj, gameObj)));
        Spark.put("/game", ((request, response) -> new JoinGameHandler().handle(request, response, authObj, gameObj)));

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
