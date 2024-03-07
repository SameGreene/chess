package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import response.ClearResponse;

public class DBService {
    public ClearResponse clearRespond(UserDAO userObj, AuthDAO authObj, GameDAO gameObj) throws DataAccessException {
        userObj.clearUserList();
        authObj.clearAuthList();
        gameObj.clearGameList();
        ClearResponse clearResponse = new ClearResponse(null);
        return clearResponse;
    }
}
