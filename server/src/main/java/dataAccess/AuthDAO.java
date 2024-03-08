package dataAccess;

import model.AuthData;

public interface AuthDAO {
    void createAuth(AuthData authData) throws DataAccessException;
    void removeAuth(AuthData authData) throws DataAccessException;
    AuthData getAuthByID(int index);
    model.AuthData getAuth(String username);
    int getSize();
    void clearAuthList();
}
