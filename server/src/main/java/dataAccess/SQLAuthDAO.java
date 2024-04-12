package dataAccess;

import model.AuthData;

import java.sql.*;

public class SQLAuthDAO implements AuthDAO {

    public static int authIndex = 0;

    public void updateIndex() {
        String sql = "SELECT MAX(ID) AS max_id FROM auth";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                this.authIndex = rs.getInt("max_id") + 1;
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createAuth(AuthData authData) {

        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        try (PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO auth (ID, authToken, username) VALUES(?, ?, ?)")) {
            preparedStatement.setInt(1, authIndex);
            authIndex = authIndex + 1;
            preparedStatement.setString(2, authData.authToken());
            preparedStatement.setString(3, authData.username());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        tryClosing(conn);
    }

    @Override
    public void removeAuth(AuthData authData) {
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
            throw new RuntimeException(e);
        }
    }

    private void tryClosing(Connection conn) {
        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
                if (resultSet.next()) {
                    String authToken = resultSet.getString("authToken");
                    tryClosing(conn);
                    return new AuthData(authToken, username);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public String getUser(String authToken) {
        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM auth WHERE authToken = (?)")) {
            preparedStatement.setString(1, authToken);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String username = resultSet.getString("username");

                    try {
                        conn.close();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    return username;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        tryClosing(conn);

        return null;
    }

    public AuthData getAuthByID(int index) {
        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM auth WHERE ID = (?)")) {
            preparedStatement.setInt(1, index);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String authToken = resultSet.getString("authToken");
                    String username = resultSet.getString("username");

                    try {
                        conn.close();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    return new AuthData(authToken, username);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        tryClosing(conn);

        return null;
    }

    @Override
    public int getSize() {
        return this.authIndex + 1;
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
            throw new RuntimeException(e);
        }

        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        authIndex = 0;
    }
}
