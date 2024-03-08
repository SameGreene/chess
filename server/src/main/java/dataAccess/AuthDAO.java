package dataAccess;

import model.AuthData;

public interface AuthDAO {
    void createAuth(AuthData authData);
    void removeAuth(AuthData authData);
    AuthData getAuthByID(int index);
    model.AuthData getAuth(String username);
    int getSize();
    void clearAuthList();
}
