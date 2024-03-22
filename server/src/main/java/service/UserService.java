package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;
import request.LoginRequest;
import request.RegisterRequest;
import response.LoginResponse;
import response.LogoutResponse;
import response.RegisterResponse;

import java.util.UUID;

public class UserService {
    public RegisterResponse regRespond(RegisterRequest req, UserDAO userObj, AuthDAO authObj) throws DataAccessException {
        String username = null;
        String authToken = null;
        String message = "";
        int status = 200;

        if (req.getUsername() == null || req.getPassword() == null || req.getEmail() == null) {
            username = null;
            authToken = null;
            message = "ERROR - Bad request";
            status = 400;
            return new RegisterResponse(username, authToken, message, status);
        }

        for (int i = 0; i < userObj.getSize(); i = i + 1) {
            if(userObj.getUser(i) == null) continue;
            if (userObj.getUser(i).username().equals(req.getUsername())) {
                message = "ERROR - User already exists";
                status = 403;
                return new RegisterResponse(null, null, message, status);
            }
        }

        UserData userDataToAdd = new UserData(req.getUsername(), req.getPassword(), req.getEmail());
        username = req.getUsername();
        userObj.createUser(userDataToAdd);
        AuthData authDataToAdd = new AuthData(UUID.randomUUID().toString(), req.getUsername());
        authToken = authDataToAdd.authToken();
        authObj.createAuth(authDataToAdd);

        return new RegisterResponse(username, authToken, message, status);
    }

    public LoginResponse loginRespond(LoginRequest req, UserDAO userObj, AuthDAO authObj) throws DataAccessException {
        String username = req.getUsername();
        String authToken = "";

        for (int i = 0; i < userObj.getSize(); i = i + 1) {
            if (userObj.getUser(i) == null) continue;
            if (userObj.getUser(i).username().equals(req.getUsername()) && req.password.equals(userObj.getUser(i).password())) {
                authToken = UUID.randomUUID().toString();
                authObj.createAuth(new AuthData(authToken, username));
                return new LoginResponse(username, authToken, "", 200);
            }
        }

        return new LoginResponse(null, null, "ERROR - User does not exist", 401);
    }

    public LogoutResponse logoutRespond(String authToken, AuthDAO authObj) throws DataAccessException {
        for (int i = 0; i < authObj.getSize(); i = i + 1) {
            if (authObj.getAuthByID(i) == null) continue;
            if (authObj.getAuthByID(i).authToken().equals(authToken)) {
                authObj.removeAuth(authObj.getAuthByID(i));
                return new LogoutResponse(null, 200);
            }
        }

        return new LogoutResponse("ERROR - Unauthorized", 401);
    }
}
