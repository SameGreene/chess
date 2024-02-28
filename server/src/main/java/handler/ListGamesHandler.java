package handler;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import response.ListGamesResponse;
import service.GameService;
import spark.Request;
import spark.Response;

public class ListGamesHandler {

    public Object handle(Request request, Response response, AuthDAO authObj, GameDAO gameObj){
        Gson myGson = new Gson();
        String authToken = request.headers("authorization");
        GameService myGameService = new GameService();
        ListGamesResponse myListGamesResponse = myGameService.listGamesRespond(authToken, authObj, gameObj);
        response.status(myListGamesResponse.status);
        return myGson.toJson(myListGamesResponse);
    }
}
