package server;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import handler.ClearHandler;
import handler.RegisterHandler;
import spark.*;

public class Server {

    UserDAO userObj = new UserDAO();
    AuthDAO authObj = new AuthDAO();
    GameDAO gameObj = new GameDAO();
    public static void main (String[] args) {
        Server myServer = new Server();
        myServer.run(Integer.parseInt(args[0]));
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Define routes
        Spark.post("/db", ((request, response) -> new ClearHandler().handle(request, response, userObj, authObj, gameObj)));
        Spark.post("/user", ((request, response) -> new RegisterHandler().handle(request, response, userObj, authObj)));

        // Register your endpoints and handle exceptions here.

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
