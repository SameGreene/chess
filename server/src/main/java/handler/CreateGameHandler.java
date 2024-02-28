package handler;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import request.CreateGameRequest;
import response.CreateGameResponse;
import service.GameService;
import spark.Request;
import spark.Response;

public class CreateGameHandler {

    public Object handle(Request request, Response response, AuthDAO authObj, GameDAO gameObj){
        Gson myGson = new Gson();
        CreateGameRequest myRequest = myGson.fromJson(request.body(), CreateGameRequest.class);
        String authToken = request.headers("authorization");
        GameService myGameService = new GameService();
        CreateGameResponse myCreateGameResponse = myGameService.createGameRespond(myRequest, authToken, authObj, gameObj);
        response.status(myCreateGameResponse.status);
        return myGson.toJson(myCreateGameResponse);
    }
}
