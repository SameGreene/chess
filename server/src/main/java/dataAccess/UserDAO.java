package dataAccess;

import java.util.ArrayList;
import java.util.List;

import model.UserData;
public class UserDAO {
    public List<UserData> userList = new ArrayList<>();
    public void createUser(UserData newUser){
        userList.add(newUser);
    }

    public UserData getUser(String username){
        UserData retUser = null;

        for (int i = 0; i < userList.size(); i = i + 1){
            if(userList.get(i).username() == username){
                retUser = userList.get(i);
            }
        }

        return retUser;
    }
}
