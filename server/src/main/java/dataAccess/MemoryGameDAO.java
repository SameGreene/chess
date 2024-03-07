package dataAccess;

import model.GameData;

import java.util.ArrayList;
import java.util.List;

public class MemoryGameDAO implements GameDAO{
    public List<GameData> gameList = new ArrayList<>();
    public int currentID = 0;
    @Override
    public void createGame(GameData game) {
        gameList.add(game);
        currentID += 1;
    }
    @Override
    public int getCurrentID(){
        return currentID;
    }
    @Override
    public void removeGame(int index) {
        gameList.remove(index);
    }
    @Override
    public GameData getGame(int index) {
        return gameList.get(index);
    }
    @Override
    public void setGame(int index, GameData game) {
        gameList.set(index, game);
    }
    @Override
    public int getSize() {
        return gameList.size();
    }
    @Override
    public void clearGameList() {
        gameList.clear();
    }
    @Override
    public List<GameData> returnGameList(){
        return gameList;
    }
}
