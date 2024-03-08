package dataAccess;

import model.AuthData;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.UUID;

public class SQLAuthDAO implements AuthDAO{
    @Override
    public void createAuth(AuthData authData){
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
            try {
                throw new DataAccessException(e.getMessage());
            } catch (DataAccessException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    @Override
    public void removeAuth(AuthData authData){
        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        try (PreparedStatement preparedStatement = conn.prepareStatement("DELETE FROM auth WHERE authToken = (?)")) {
            preparedStatement.setString(1, authData.authToken());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            try {
                throw new DataAccessException(e.getMessage());
            } catch (DataAccessException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    @Override
    public AuthData getAuth(String username) {
        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM auth WHERE username = (?)")) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    String authToken = resultSet.getString("authToken");
                    return new AuthData(authToken, username);
                }
            }
        } catch (SQLException e) {
            try {
                throw new DataAccessException(e.getMessage());
            } catch (DataAccessException ex) {
                throw new RuntimeException(ex);
            }
        }

        return null;
    }
    public AuthData getAuthByID(int index){
        return null;
    }
    @Override
    public int getSize() {
        int size = 0;

        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT COUNT(*) FROM auth")) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    size = resultSet.getInt(1);
                    return size;
                }
            }
        } catch (SQLException e) {
            try {
                throw new DataAccessException(e.getMessage());
            } catch (DataAccessException ex) {
                throw new RuntimeException(ex);
            }
        }

        return size;
    }
    @Override
    public void clearAuthList() {
        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        try (PreparedStatement preparedStatement = conn.prepareStatement("TRUNCATE TABLE auth")) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            try {
                throw new DataAccessException(e.getMessage());
            } catch (DataAccessException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
