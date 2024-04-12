package dataAccess;

import model.UserData;

import java.util.ArrayList;
import java.util.List;

public class MemoryUserDAO implements UserDAO{
    public List<UserData> userList = new ArrayList<>();

    @Override
    public void updateIndex(){}
    @Override
    public void createUser(UserData newUser) {
        userList.add(newUser);
    }
    @Override
    public UserData getUser(int index) {
        return userList.get(index);
    }
    @Override
    public int getSize() {
        return userList.size();
    }
    @Override
    public void clearUserList() {
        userList.clear();
    }
}
