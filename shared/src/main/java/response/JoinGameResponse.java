package response;

import chess.ChessGame;

public class JoinGameResponse {
    public String message;
    public int status;
    public ChessGame game;

    public JoinGameResponse(String message, int status, ChessGame game) {
        this.message = message;
        this.status = status;
        this.game = game;
    }
}