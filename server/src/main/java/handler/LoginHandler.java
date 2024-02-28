package handler;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.UserDAO;
import request.LoginRequest;
import response.LoginResponse;
import service.UserService;
import spark.Request;
import spark.Response;

public class LoginHandler {

    public Object handle(Request request, Response response, UserDAO userObj, AuthDAO authObj) {
        Gson myGson = new Gson();
        LoginRequest myRequest = myGson.fromJson(request.body(), LoginRequest.class);
        UserService myUserService = new UserService();
        LoginResponse myLoginResponse = myUserService.loginRespond(myRequest, userObj, authObj);
        response.status(myLoginResponse.status);
        return myGson.toJson(myLoginResponse);
    }
}
