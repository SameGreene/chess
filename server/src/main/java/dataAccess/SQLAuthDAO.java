package dataAccess;

import model.AuthData;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.UUID;

public class SQLAuthDAO implements AuthDAO{
    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        AuthData newAuth = new AuthData(UUID.randomUUID().toString(), authData.username());

        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        try (PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO auth (authToken, username) VALUES(?, ?)")) {
            preparedStatement.setString(1, newAuth.authToken());
            preparedStatement.setString(2, newAuth.username());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
    @Override
    public void removeAuth(AuthData authData) throws DataAccessException {
        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        try (PreparedStatement preparedStatement = conn.prepareStatement("DELTE FROM auth WHERE authToken = (?)")) {
            preparedStatement.setString(1, authData.authToken());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
    @Override
    public AuthData getAuth(String username) {
        return null;
    }
    public AuthData getAuthByID(int index){
        return null;
    }
    @Override
    public int getSize() {
        return 0;
    }
    @Override
    public void clearAuthList() {

    }
}
