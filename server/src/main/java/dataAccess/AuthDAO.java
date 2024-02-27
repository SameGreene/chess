package dataAccess;

import model.AuthData;
import model.UserData;

import java.util.ArrayList;
import java.util.List;

public class AuthDAO {

    public List<AuthData> authList = new ArrayList<>();

    public void createAuth(AuthData auth){
        authList.add(auth);
    }

    public AuthData getAuth(String authToken){
        AuthData retAuth = null;

        for (int i = 0; i < authList.size(); i = i + 1){
            if(authList.get(i).authToken() == authToken){
                retAuth = authList.get(i);
            }
        }

        return retAuth;
    }

    public void deleteAuth(String authToken){

    }
}
