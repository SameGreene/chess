package handler;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.UserDAO;
import request.RegisterRequest;
import service.UserService;
import spark.Request;
import spark.Response;

public class RegisterHandler {
    public Object handle (Request request, Response response, UserDAO userObj, AuthDAO authObj){
        Gson myGson = new Gson();
        RegisterRequest myRequest = myGson.fromJson(request.body(), RegisterRequest.class);
        UserService myUserService = new UserService();
        return myGson.toJson(myUserService.respond(myRequest, userObj, authObj));
    }
}
