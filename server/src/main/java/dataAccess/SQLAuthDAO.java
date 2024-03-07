package dataAccess;

import model.AuthData;

import java.sql.*;
import java.util.UUID;

public class SQLAuthDAO implements AuthDAO{
    @Override
    public void createAuth(String username) throws DataAccessException {
        AuthData newAuth = new AuthData(UUID.randomUUID().toString(), username);
        Connection conn = DatabaseManager.getConnection();
        try (PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO AuthTokens (AuthToken, username) VALUES(?, ?)")) {
            preparedStatement.setString(1, newAuth.authToken());
            preparedStatement.setString(2, newAuth.username());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
    @Override
    public void removeAuth(int index) {

    }
    @Override
    public AuthData getAuth(int index) {
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
