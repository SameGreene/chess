package dataAccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.List;

public class MemoryAuthDAO implements AuthDAO {
    public List<AuthData> authList = new ArrayList<>();

    @Override
    public void updateIndex(){}
    @Override
    public void createAuth(AuthData authData){
        authList.add(authData);
    }

    @Override
    public void removeAuth(AuthData authData){
        authList.remove(authData);
    }

    public AuthData getAuthByID(int index){
        return authList.get(index);
    }

    @Override
    public AuthData getAuth(String username) {
        for (int i = 0; i < authList.size(); i = i + 1) {
            if (authList.get(i).username().equals(username)){
                return authList.get(i);
            }
            else{
                return null;
            }
        }
        return null;
    }

    @Override
    public String getUser(String authToken) {
        for (int i = 0; i < authList.size(); i = i + 1) {
            if (authList.get(i).username().equals(authToken)){
                return authList.get(i).username();
            }
            else{
                return null;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return authList.size();
    }

    @Override
    public void clearAuthList() {
        authList.clear();
    }
}
