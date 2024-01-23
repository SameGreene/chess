package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private PieceType pieceType;
    private ChessGame.TeamColor pieceColor;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceType = type;
        this.pieceColor = pieceColor;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> potentialMoves = new HashSet<>();

        switch (this.pieceType){
            case KING:
                System.out.println("K");
                break;
            case QUEEN:
                System.out.println("Q");
                break;
            case BISHOP:
                System.out.println("B");
                break;
            case KNIGHT:
                System.out.println("N");
                break;
            case ROOK:
                potentialMoves = rookMoves(board, myPosition);
                break;
            case PAWN:
                System.out.println("P");
                break;
            default: return null;
        }

        return potentialMoves;
    }

    private Collection<ChessMove> rookMoves(ChessBoard myBoard, ChessPosition currentPos) {
        int currentRow = currentPos.getRow();
        int currentCol = currentPos.getColumn();

        Collection<ChessMove> potentialMoves = new HashSet<>();

        // Row checking UP
        for (int i = currentRow + 1; i <= 8; i++) {
            ChessPosition potentialPosition = new ChessPosition(i, currentCol);
            if (myBoard.getPiece(potentialPosition) != null && myBoard.getPiece(potentialPosition).pieceColor != this.pieceColor) {
                ChessMove potentialMove = new ChessMove(currentPos, potentialPosition, null);
                potentialMoves.add(potentialMove);
                break;
            }
            else if (myBoard.getPiece(potentialPosition) != null && myBoard.getPiece(potentialPosition).pieceColor == this.pieceColor) {
                break;
            }
            else if (myBoard.getPiece(potentialPosition) == null){
                ChessMove potentialMove = new ChessMove(currentPos, potentialPosition, null);
                potentialMoves.add(potentialMove);
            }
        }
        // Row checking DOWN
        for (int i = currentRow - 1; i > 0; i--) {
            ChessPosition potentialPosition = new ChessPosition(i, currentCol);
            if (myBoard.getPiece(potentialPosition) != null && myBoard.getPiece(potentialPosition).pieceColor != this.pieceColor) {
                ChessMove potentialMove = new ChessMove(currentPos, potentialPosition, null);
                potentialMoves.add(potentialMove);
                break;
            }
            else if (myBoard.getPiece(potentialPosition) != null && myBoard.getPiece(potentialPosition).pieceColor == this.pieceColor) {
                break;
            }
            else if (myBoard.getPiece(potentialPosition) == null){
                ChessMove potentialMove = new ChessMove(currentPos, potentialPosition, null);
                potentialMoves.add(potentialMove);
            }
        }

        // Column checking UP
        for (int j = currentCol + 1; j <= 8; j++) {
            ChessPosition potentialPosition = new ChessPosition(currentRow, j);
            if (myBoard.getPiece(potentialPosition) != null && myBoard.getPiece(potentialPosition).pieceColor != this.pieceColor) {
                ChessMove potentialMove = new ChessMove(currentPos, potentialPosition, null);
                potentialMoves.add(potentialMove);
                break;
            }
            else if (myBoard.getPiece(potentialPosition) != null && myBoard.getPiece(potentialPosition).pieceColor == this.pieceColor) {
                break;
            }
            else if (myBoard.getPiece(potentialPosition) == null){
                ChessMove potentialMove = new ChessMove(currentPos, potentialPosition, null);
                potentialMoves.add(potentialMove);
            }
        }
        // Column checking DOWN
        for (int j = currentCol - 1; j > 0; j--) {
            ChessPosition potentialPosition = new ChessPosition(currentRow, j);
            if (myBoard.getPiece(potentialPosition) != null && myBoard.getPiece(potentialPosition).pieceColor != this.pieceColor) {
                ChessMove potentialMove = new ChessMove(currentPos, potentialPosition, null);
                potentialMoves.add(potentialMove);
                break;
            }
            else if (myBoard.getPiece(potentialPosition) != null && myBoard.getPiece(potentialPosition).pieceColor == this.pieceColor) {
                break;
            }
            else if (myBoard.getPiece(potentialPosition) == null){
                ChessMove potentialMove = new ChessMove(currentPos, potentialPosition, null);
                potentialMoves.add(potentialMove);
            }
        }

        return potentialMoves;
    }
}
