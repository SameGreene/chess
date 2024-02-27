package service;

import dataAccess.AuthDAO;
import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;
import request.RegisterRequest;
import response.RegisterResponse;

import java.util.UUID;

public class UserService {
    public RegisterResponse regRespond(RegisterRequest req, UserDAO userObj, AuthDAO authObj){
        boolean userExists = false;
        String username = null;
        String authToken = null;
        String message = "";
        int status = 200;

        if(req.getUsername() == null || req.getPassword() == null || req.getEmail() == null){
            username = null;
            authToken = null;
            message = "ERROR - Bad request";
            status = 400;
            return new RegisterResponse(username, authToken, message, status);
        }

        for(int i = 0; i < userObj.userList.size(); i = i + 1){
            if(userObj.userList.get(i).username().equals(req.getUsername())){
                userExists = true;
                username = null;
                authToken = null;
                message = "ERROR - User already exists";
                status = 403;
                return new RegisterResponse(username, authToken, message, status);
            }
        }

        if(!userExists){
            UserData userDataToAdd = new UserData(req.getUsername(), req.getPassword(), req.getEmail());
            AuthData authDataToAdd = new AuthData(UUID.randomUUID().toString(), req.getUsername());
            username = req.getUsername();
            userObj.createUser(userDataToAdd);
            authObj.createAuth(authDataToAdd);
            authToken = authDataToAdd.authToken();
        }

        return new RegisterResponse(username, authToken, message, status);
    }
}
