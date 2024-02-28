package server;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import handler.*;
import spark.*;

public class Server {

    UserDAO userObj = new UserDAO();
    AuthDAO authObj = new AuthDAO();
    GameDAO gameObj = new GameDAO();

    public static void main(String[] args) {
        Server myServer = new Server();
        myServer.run(Integer.parseInt(args[0]));
    }

    public int run(int desiredPort) {

        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

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
