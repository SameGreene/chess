package dataAccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    public List<AuthData> authList = new ArrayList<>();

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        authList.add(authData);
    }

    @Override
    public void removeAuth(AuthData authData) throws DataAccessException {
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
    public int getSize() {
        return authList.size();
    }

    @Override
    public void clearAuthList() {
        authList.clear();
    }
}
