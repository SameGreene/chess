package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLGameDAO implements GameDAO {
    public int currentID = 0;

    public void updateIndex() {
        String sql = "SELECT MAX(ID) AS max_id FROM games";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                this.currentID = rs.getInt("max_id") + 1;
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createGame(GameData game) {

        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        try (PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO games (ID, gameID, whiteUsername, blackUsername, gameName, game) VALUES(?, ?, ?, ?, ?, ?)")) {
            preparedStatement.setInt(1, currentID);
            currentID = currentID + 1;
            preparedStatement.setInt(2, game.gameID());
            preparedStatement.setString(3, game.whiteUsername());
            preparedStatement.setString(4, game.blackUsername());
            preparedStatement.setString(5, game.gameName());
            // Serialize Game object to JSON
            Gson gson = new Gson();
            String jsonGame;
            jsonGame = gson.toJson(game.game());
            preparedStatement.setString(6, jsonGame);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            try {
                throw new DataAccessException(e.getMessage());
            } catch (DataAccessException ex) {
                throw new RuntimeException(ex);
            }
        }

        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getCurrentID() {
        return currentID + 1;
    }

    @Override
    public GameData getGame(int index) {
        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM games WHERE ID = (?)")) {
            preparedStatement.setInt(1, index);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    int gameID = resultSet.getInt("gameID");
                    String whiteUsername = resultSet.getString("whiteUsername");
                    String blackUsername = resultSet.getString("blackUsername");
                    String gameName = resultSet.getString("gameName");
                    String jsonGame = resultSet.getString("game");
                    Gson gson = new Gson();
                    ChessGame gameFromJSON = gson.fromJson(jsonGame, ChessGame.class);

                    try {
                        conn.close();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    return new GameData(gameID, whiteUsername, blackUsername, gameName, gameFromJSON);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        tryClosing(conn);

        return null;
    }

    private void tryClosing(Connection conn) {
        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setGame(int index, GameData game) {
        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        try (PreparedStatement preparedStatement = conn.prepareStatement("UPDATE games SET gameID = (?), whiteUsername = (?), blackUsername = (?), gameName = (?), game = (?) WHERE ID = (?)")) {
            preparedStatement.setInt(1, game.gameID());
            preparedStatement.setString(2, game.whiteUsername());
            preparedStatement.setString(3, game.blackUsername());
            preparedStatement.setString(4, game.gameName());
            // Serialize Game object to JSON
            Gson gson = new Gson();
            String jsonGame;
            jsonGame = gson.toJson(game.game());
            preparedStatement.setString(5, jsonGame);
            preparedStatement.setInt(6, index);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getSize() {
        return currentID + 1;
    }

    @Override
    public void clearGameList() {
        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        try (PreparedStatement preparedStatement = conn.prepareStatement("TRUNCATE TABLE games")) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            try {
                throw new DataAccessException(e.getMessage());
            } catch (DataAccessException ex) {
                throw new RuntimeException(ex);
            }
        }

        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        currentID = 0;
    }

    @Override
    public List<GameData> returnGameList() {
        List<GameData> gameList = new ArrayList<>();

        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM games")) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while(resultSet.next()) {
                    int gameID = resultSet.getInt("gameID");
                    String whiteUsername = resultSet.getString("whiteUsername");
                    String blackUsername = resultSet.getString("blackUsername");
                    String gameName = resultSet.getString("gameName");
                    String jsonGame = resultSet.getString("game");
                    Gson gson = new Gson();
                    ChessGame gameFromJSON = gson.fromJson(jsonGame, ChessGame.class);
                    gameList.add(new GameData(gameID, whiteUsername, blackUsername, gameName, gameFromJSON));
                }
            }
        } catch (SQLException e) {
            try {
                throw new DataAccessException(e.getMessage());
            } catch (DataAccessException ex) {
                throw new RuntimeException(ex);
            }
        }

        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return gameList;
    }
}
