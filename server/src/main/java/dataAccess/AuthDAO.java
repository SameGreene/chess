package dataAccess;

import model.AuthData;

public interface AuthDAO {
    void createAuth(AuthData auth);
    void removeAuth(int index);
    model.AuthData getAuth(int index);
    int getSize();
    void clearAuthList();
}
