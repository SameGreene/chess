package dataAccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.List;

public class MemoryAuthDAO implements AuthDAO{
    public List<AuthData> authList = new ArrayList<>();
    @Override
    public void createAuth(AuthData auth) {
        authList.add(auth);
    }

    @Override
    public void removeAuth(int index) {
        authList.remove(index);
    }

    @Override
    public AuthData getAuth(int index) {
        authList.get(index);
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
