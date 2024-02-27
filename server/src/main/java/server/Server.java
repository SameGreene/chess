package server;

import handler.RegisterHandler;
import spark.*;

public class Server {

    public static void main (String[] args) {
        Server myServer = new Server();
        myServer.run(Integer.parseInt(args[0]));
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Define routes
        Spark.post("/user", ((request, response) -> new RegisterHandler().handle(request, response)));

        // Register your endpoints and handle exceptions here.

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
