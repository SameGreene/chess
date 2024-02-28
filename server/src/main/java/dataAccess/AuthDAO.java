package dataAccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.List;

public class AuthDAO {

    public List<AuthData> authList = new ArrayList<>();

    public void createAuth(AuthData auth){
        authList.add(auth);
    }

    public void clearAuthList(){
        authList.clear();
    }
}
