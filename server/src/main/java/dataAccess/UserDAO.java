package dataAccess;

import model.UserData;

public interface UserDAO {
    void updateIndex();
    void createUser(UserData newUser);
    model.UserData getUser(int index);
    int getSize();
    void clearUserList();
}
