package dataAccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.List;

public interface AuthDAO {
    void createAuth(AuthData auth);
    void removeAuth(int index);
    model.AuthData getAuth(int index);
    int getSize();
    void clearAuthList();
}
