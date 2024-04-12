package webSocketMessages.serverMessages;

import chess.ChessGame;
import com.google.gson.Gson;

public class LoadGame extends ServerMessage{
    String game;

    public LoadGame(ServerMessageType type, ChessGame game) {
        super(type);
        this.game = new Gson().toJson(game);
    }


}
