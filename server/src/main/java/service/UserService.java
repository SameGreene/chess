package service;

import dataAccess.AuthDAO;
import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;
import request.RegisterRequest;
import response.RegisterResponse;

import java.util.UUID;

public class UserService {
    public RegisterResponse respond(RegisterRequest req, UserDAO userObj, AuthDAO authObj){
        UserData userDataToAdd = new UserData(req.getUsername(), req.getPassword(), req.getEmail());
        AuthData authDataToAdd = new AuthData(UUID.randomUUID().toString(), req.getUsername());
        userObj.createUser(userDataToAdd);
        authObj.createAuth(authDataToAdd);
        RegisterResponse regResponse = new RegisterResponse(req.getUsername(), authDataToAdd.authToken(), null);
        return regResponse;
    }
}
