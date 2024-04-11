package dataAccess;

import model.AuthData;

public interface AuthDAO {
    void updateIndex();
    void createAuth(AuthData authData);
    void removeAuth(AuthData authData);
    AuthData getAuthByID(int index);
    model.AuthData getAuth(String username);
    String getUser(String authToken);
    int getSize();
    void clearAuthList();
}
