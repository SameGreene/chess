package dataAccess;

import java.util.ArrayList;
import java.util.List;

import model.UserData;
public class UserDAO {
    public List<UserData> userList = new ArrayList<>();
    public void createUser(UserData newUser){
        userList.add(newUser);
    }

    public void clearUserList(){
        userList.clear();
    }
}
