package service;

import dataAccess.AuthDAO;
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
    public RegisterResponse regRespond(RegisterRequest req, UserDAO userObj, AuthDAO authObj) {
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

        for (int i = 0; i < userObj.userList.size(); i = i + 1) {
            if (userObj.userList.get(i).username().equals(req.getUsername())) {
                username = null;
                authToken = null;
                message = "ERROR - User already exists";
                status = 403;
                return new RegisterResponse(username, authToken, message, status);
            }
        }

        UserData userDataToAdd = new UserData(req.getUsername(), req.getPassword(), req.getEmail());
        AuthData authDataToAdd = new AuthData(UUID.randomUUID().toString(), req.getUsername());
        username = req.getUsername();
        userObj.createUser(userDataToAdd);
        authObj.createAuth(authDataToAdd);
        authToken = authDataToAdd.authToken();

        return new RegisterResponse(username, authToken, message, status);
    }

    public LoginResponse loginRespond(LoginRequest req, UserDAO userObj, AuthDAO authObj) {
        String username = req.getUsername();
        String authToken = "";

        for (int i = 0; i < userObj.userList.size(); i = i + 1) {
            if (userObj.userList.get(i).username().equals(req.getUsername()) && req.password.equals(userObj.userList.get(i).password())) {
                authToken = UUID.randomUUID().toString();
                authObj.authList.add(new AuthData(authToken, username));
                return new LoginResponse(username, authToken, "", 200);
            }
            else {
                return new LoginResponse(null, null, "ERROR - Unauthorized", 401);
            }
        }

        return new LoginResponse(null, null, "ERROR - User does not exist", 401);
    }

    public LogoutResponse logoutRespond(String authToken, AuthDAO authObj) {
        for (int i = 0; i < authObj.authList.size(); i = i + 1) {
            if (authToken.equals(authObj.authList.get(i).authToken())) {
                authObj.authList.remove(i);
                return new LogoutResponse(null, 200);
            }
        }

        return new LogoutResponse("ERROR - Unauthorized", 401);
    }
}
