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

        switch (this.pieceType) {
            case KING:
                potentialMoves = kingMoves(board, myPosition);
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
            default:
                return null;
        }

        return potentialMoves;
    }

    private Collection<ChessMove> kingMoves(ChessBoard myBoard, ChessPosition currentPos) {
        int currentRow = currentPos.getRow();
        int currentCol = currentPos.getColumn();

        Collection<ChessMove> potentialMoves = new HashSet<>();
        Collection<ChessPosition> potentialPositions = new HashSet<>();

        // UpLeft
        potentialPositions.add(new ChessPosition(currentRow + 1, currentCol - 1));
        // Up
        potentialPositions.add(new ChessPosition(currentRow + 1, currentCol));
        // UpRight
        potentialPositions.add(new ChessPosition(currentRow + 1, currentCol + 1));
        // Left
        potentialPositions.add(new ChessPosition(currentRow, currentCol - 1));
        // Right
        potentialPositions.add(new ChessPosition(currentRow, currentCol + 1));
        // DownLeft
        potentialPositions.add(new ChessPosition(currentRow - 1, currentCol - 1));
        // Down
        potentialPositions.add(new ChessPosition(currentRow - 1, currentCol));
        // DownRight
        potentialPositions.add(new ChessPosition(currentRow - 1, currentCol + 1));

        for (ChessPosition pos: potentialPositions) {
            // Remove out of bounds positions
            if (pos.getRow() < 1 || pos.getRow() > 8 || pos.getColumn() < 1 || pos.getColumn() > 8) {
                continue;
            }
            // Check if same team's piece is in the way
            if (myBoard.getPiece(pos) != null && myBoard.getPiece(pos).pieceColor == this.pieceColor) {
                continue;
            }
            else{
                potentialMoves.add(new ChessMove(currentPos, pos, null));
            }
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
            } else if (myBoard.getPiece(potentialPosition) != null && myBoard.getPiece(potentialPosition).pieceColor == this.pieceColor) {
                break;
            } else if (myBoard.getPiece(potentialPosition) == null) {
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
            } else if (myBoard.getPiece(potentialPosition) != null && myBoard.getPiece(potentialPosition).pieceColor == this.pieceColor) {
                break;
            } else if (myBoard.getPiece(potentialPosition) == null) {
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
            } else if (myBoard.getPiece(potentialPosition) != null && myBoard.getPiece(potentialPosition).pieceColor == this.pieceColor) {
                break;
            } else if (myBoard.getPiece(potentialPosition) == null) {
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
            } else if (myBoard.getPiece(potentialPosition) != null && myBoard.getPiece(potentialPosition).pieceColor == this.pieceColor) {
                break;
            } else if (myBoard.getPiece(potentialPosition) == null) {
                ChessMove potentialMove = new ChessMove(currentPos, potentialPosition, null);
                potentialMoves.add(potentialMove);
            }
        }

        return potentialMoves;
    }
}
