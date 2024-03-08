package dataAccess;

import model.AuthData;
import model.UserData;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.UUID;

public class SQLAuthDAO implements AuthDAO{

    private int authIndex = 0;
    @Override
    public void createAuth(AuthData authData){
        AuthData newAuth = new AuthData(UUID.randomUUID().toString(), authData.username());

        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        try (PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO auth (ID, authToken, username) VALUES(?, ?, ?)")) {
            preparedStatement.setInt(1, this.authIndex);
            preparedStatement.setString(2, newAuth.authToken());
            preparedStatement.setString(3, newAuth.username());
            preparedStatement.executeUpdate();
            this.authIndex++;
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
        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM auth WHERE ID = (?)")) {
            preparedStatement.setInt(1, index);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    String authToken = resultSet.getString("authToken");
                    String username = resultSet.getString("username");
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
    @Override
    public int getSize() {
        return this.authIndex;
    }
    @Override
    public void clearAuthList() {
        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        try (PreparedStatement preparedStatement = conn.prepareStatement("TRUNCATE TABLE users")) {
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
