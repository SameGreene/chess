package handler;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import service.DBService;
import spark.Request;
import spark.Response;

public class ClearHandler {
    public Object handle(Request request, Response response, UserDAO userObj, AuthDAO authObj, GameDAO gameObj){
        Gson myGson = new Gson();
        DBService myDBService = new DBService();
        return myGson.toJson(myDBService.clearRespond(userObj, authObj, gameObj));
    }
}
