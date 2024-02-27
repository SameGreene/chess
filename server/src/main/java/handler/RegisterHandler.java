package handler;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.UserDAO;
import request.RegisterRequest;
import response.RegisterResponse;
import service.UserService;
import spark.Request;
import spark.Response;

public class RegisterHandler {
    public Object handle (Request request, Response response, UserDAO userObj, AuthDAO authObj){
        Gson myGson = new Gson();
        RegisterRequest myRequest = myGson.fromJson(request.body(), RegisterRequest.class);
        UserService myUserService = new UserService();
        RegisterResponse myRegResponse = myUserService.regRespond(myRequest, userObj, authObj);
        response.status(myRegResponse.status);
        return myGson.toJson(myRegResponse);
    }
}
