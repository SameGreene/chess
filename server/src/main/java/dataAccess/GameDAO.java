package dataAccess;

import chess.ChessGame;
import model.GameData;
import java.util.ArrayList;
import java.util.List;

public class GameDAO {

    public List<GameData> gameList = new ArrayList<>();
    public int currentID = 1;

    public void clearGameList(){
        gameList.clear();
    }
}
