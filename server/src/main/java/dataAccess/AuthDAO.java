package dataAccess;

import model.AuthData;

public interface AuthDAO {
    void createAuth(String username) throws DataAccessException;
    void removeAuth(int index);
    model.AuthData getAuth(int index);
    int getSize();
    void clearAuthList();
}
