package dataAccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{
    public List<AuthData> authList = new ArrayList<>();
    @Override
    public void createAuth(String username) throws DataAccessException{
        AuthData authToAdd = new AuthData(UUID.randomUUID().toString(), username);
        authList.add(authToAdd);
    }
    @Override
    public void removeAuth(int index) {
        authList.remove(index);
    }
    @Override
    public AuthData getAuth(int index) {
        return authList.get(index);
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
