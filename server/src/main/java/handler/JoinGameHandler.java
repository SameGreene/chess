package handler;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import request.JoinGameRequest;
import response.JoinGameResponse;
import service.GameService;
import spark.Request;
import spark.Response;

public class JoinGameHandler {

    public Object handle(Request request, Response response, AuthDAO authObj, GameDAO gameObj){
        Gson myGson = new Gson();
        JoinGameRequest myRequest = myGson.fromJson(request.body(), JoinGameRequest.class);
        String authToken = request.headers("authorization");
        GameService myGameService = new GameService();
        JoinGameResponse myJoinGameResponse = myGameService.joinGameRespond(myRequest, authToken, authObj, gameObj);
        response.status(myJoinGameResponse.status);
        return myGson.toJson(myJoinGameResponse);
    }
}
