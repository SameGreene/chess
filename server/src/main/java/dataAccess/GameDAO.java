package dataAccess;

import model.GameData;

import java.util.List;

public interface GameDAO {
    void updateIndex();
    void createGame(GameData game);
    int getCurrentID();
    GameData getGame(int index);
    void setGame(int index, GameData game);
    int getSize();
    void clearGameList();
    List<GameData> returnGameList();
}
