package webSocketMessages.serverMessages;

import chess.ChessGame;

public class LoadGame extends ServerMessage{
    ChessGame game;
    String message;

    public LoadGame(ServerMessageType type, ChessGame game) {
        super(type);
        this.game = game;
    }
}
