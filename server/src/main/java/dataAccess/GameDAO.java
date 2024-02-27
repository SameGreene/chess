package dataAccess;

import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.List;

public class GameDAO {

    public List<GameData> gameList = new ArrayList<>();
    public void createGame(GameData newGame){
        gameList.add(newGame);
    }

    public GameData getGame(int gameID){
        GameData retGame = null;

        for (int i = 0; i < gameList.size(); i = i + 1){
            if(gameList.get(i).gameID() == gameID){
                retGame = gameList.get(i);
            }
        }

        return retGame;
    }

    public void listGames(){

    }

    public void updateGame(int gameID){

    }

    public void clearGameList(){
        gameList.clear();
    }
}
