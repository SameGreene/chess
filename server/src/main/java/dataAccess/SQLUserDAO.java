package dataAccess;

import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLUserDAO implements UserDAO {
    @Override
    public void createUser(UserData newUser) {
        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        try (PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO auth (username, password, email) VALUES(?, ?, ?)")) {
            preparedStatement.setString(1, newUser.username());
            preparedStatement.setString(2, newUser.password());
            preparedStatement.setString(3, newUser.email());
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
    public void removeUser(int index) {
        // Not implemented
        return;
    }

    @Override
    public UserData getUser(int index) {
        return null;
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public void clearUserList() {

    }
}
