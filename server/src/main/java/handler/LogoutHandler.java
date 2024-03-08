package handler;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import response.LogoutResponse;
import service.UserService;
import spark.Request;
import spark.Response;

public class LogoutHandler {
    public Object handle(Request request, Response response, AuthDAO authObj) throws DataAccessException {
        Gson myGson = new Gson();
        String authToken = request.headers("authorization");
        UserService myUserService = new UserService();
        LogoutResponse myLogoutResponse = myUserService.logoutRespond(authToken, authObj);
        response.status(myLogoutResponse.status);
        return myGson.toJson(myLogoutResponse);
    }
}
