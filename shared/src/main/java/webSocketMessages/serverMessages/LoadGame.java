package webSocketMessages.serverMessages;

import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.Gson;

public class LoadGame extends ServerMessage{
    String game;
    ChessGame chessGame;
    ChessGame.TeamColor playerColor;

    public LoadGame(ServerMessageType type, ChessGame game, ChessGame.TeamColor playerColor) {
        super(type);
        this.game = new Gson().toJson(game);
        this.chessGame = game;
        this.playerColor = playerColor;
    }

    public String getPlayerColor() {
        if (playerColor == null) {
            return null;
        }
        else {
            return playerColor.toString();
        }
    }

    public ChessBoard getGameBoard() {
        return chessGame.getBoard();
    }

    public ChessGame getChessGame() {
        return chessGame;
    }

    public String getGame(String teamColor) {
        if (teamColor == null) {
            return chessGame.getBoard().toString("WHITE");
        }
        else {
            return chessGame.getBoard().toString(teamColor);
        }
    }
}
