package service;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import response.ClearResponse;

public class DBService {
    public ClearResponse clearRespond(UserDAO userObj, AuthDAO authObj, GameDAO gameObj){
        userObj.clearUserList();
        authObj.clearAuthList();
        gameObj.clearGameList();
        ClearResponse clearResponse = new ClearResponse(null);
        return clearResponse;
    }
}
