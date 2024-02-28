package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public class ChessPiece implements Cloneable {

    private PieceType pieceType;
    private ChessGame.TeamColor pieceColor;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceType = type;
        this.pieceColor = pieceColor;
    }

    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    public PieceType getPieceType() {
        return this.pieceType;
    }

    // Return all possible moves given a piece's position on the board
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> potentialMoves = new HashSet<>();

        switch (this.pieceType) {
            case KING:
                potentialMoves = kingMoves(board, myPosition);
                break;
            case QUEEN:
                potentialMoves = queenMoves(board, myPosition);
                break;
            case BISHOP:
                potentialMoves = bishopMoves(board, myPosition);
                break;
            case KNIGHT:
                potentialMoves = knightMoves(board, myPosition);
                break;
            case ROOK:
                potentialMoves = rookMoves(board, myPosition);
                break;
            case PAWN:
                potentialMoves = pawnMoves(board, myPosition);
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

        for (ChessPosition pos : potentialPositions) {
            if (obCheck(pos, myBoard)) {
                continue;
            } else {
                // Check if pos isn't in enemy king's bubble. If it is, continue
                // Column sweeps
                for (int i = pos.getRow() - 2; i < pos.getRow() + 2; i++) {
                    // 2 left column
                    ChessPosition checkPos = new ChessPosition(i, pos.getColumn() - 2);
                    // Make sure checkPos is in bounds
                    if (outOfRange(myBoard, currentPos, potentialMoves, pos, checkPos)) continue;
                    // 1 left column
                    checkPos = new ChessPosition(i, pos.getColumn() - 1);
                    // Make sure checkPos is in bounds
                    if (outOfRange(myBoard, currentPos, potentialMoves, pos, checkPos)) continue;
                    // 1 right column
                    checkPos = new ChessPosition(i, pos.getColumn() + 1);
                    // Make sure checkPos is in bounds
                    if (outOfRange(myBoard, currentPos, potentialMoves, pos, checkPos)) continue;
                    // 2 right column
                    checkPos = new ChessPosition(i, pos.getColumn() + 2);
                    if (outOfRange(myBoard, currentPos, potentialMoves, pos, checkPos)) continue;
                }
                // Row sweeps
                for (int j = pos.getColumn() - 2; j < pos.getColumn() + 2; j++) {
                    // 2 up row
                    ChessPosition checkPos = new ChessPosition(pos.getRow() + 2, j);
                    if (outOfRange(myBoard, currentPos, potentialMoves, pos, checkPos)) continue;
                    // 1 up row
                    checkPos = new ChessPosition(pos.getRow() + 1, j);
                    if (outOfRange(myBoard, currentPos, potentialMoves, pos, checkPos)) continue;
                    // 1 down row
                    checkPos = new ChessPosition(pos.getRow() - 1, j);
                    if (outOfRange(myBoard, currentPos, potentialMoves, pos, checkPos)) continue;
                    // 2 down row
                    checkPos = new ChessPosition(pos.getRow() - 2, j);
                    if (outOfRange(myBoard, currentPos, potentialMoves, pos, checkPos)) continue;
                }
            }
        }

        return potentialMoves;
    }

    public boolean outOfRange(ChessBoard myBoard, ChessPosition currentPos, Collection<ChessMove> potentialMoves, ChessPosition pos, ChessPosition checkPos) {
        if (checkPos.getRow() < 1 || checkPos.getRow() > 8 || checkPos.getColumn() < 1 || checkPos.getColumn() > 8) {
            return true;
        }
        if (myBoard.getPiece(checkPos) == null || (myBoard.getPiece(checkPos) != null && myBoard.getPiece(checkPos).pieceType != PieceType.KING)) {
            potentialMoves.add(new ChessMove(currentPos, pos, null));
        }
        return false;
    }

    private Collection<ChessMove> queenMoves(ChessBoard myBoard, ChessPosition currentPos) {
        int currentRow = currentPos.getRow();
        int currentCol = currentPos.getColumn();

        Collection<ChessMove> potentialMoves = new HashSet<>();
        Collection<ChessMove> potentialDMoves = new HashSet<>();

        potentialMoves = rowColCheck(myBoard, currentPos, currentRow, currentCol);
        potentialDMoves = diagCheck(myBoard, currentPos, currentRow, currentCol);
        potentialMoves.addAll(potentialDMoves);
        return potentialMoves;
    }

    private Collection<ChessMove> bishopMoves(ChessBoard myBoard, ChessPosition currentPos) {
        int currentRow = currentPos.getRow();
        int currentCol = currentPos.getColumn();

        return diagCheck(myBoard, currentPos, currentRow, currentCol);
    }

    private Collection<ChessMove> knightMoves(ChessBoard myBoard, ChessPosition currentPos) {
        int currentRow = currentPos.getRow();
        int currentCol = currentPos.getColumn();

        Collection<ChessMove> potentialMoves = new HashSet<>();
        Collection<ChessPosition> potentialPositions = new HashSet<>();

        // Each move is formatted Dir1Dir2 where Dir1 moves 1 space and Dir2 moves 2
        // UpLeft
        potentialPositions.add(new ChessPosition(currentRow + 1, currentCol - 2));
        // LeftUp
        potentialPositions.add(new ChessPosition(currentRow + 2, currentCol - 1));
        // UpRight
        potentialPositions.add(new ChessPosition(currentRow + 1, currentCol + 2));
        // RightUp
        potentialPositions.add(new ChessPosition(currentRow + 2, currentCol + 1));
        // DownLeft
        potentialPositions.add(new ChessPosition(currentRow - 1, currentCol - 2));
        // LeftDown
        potentialPositions.add(new ChessPosition(currentRow - 2, currentCol - 1));
        // DownRight
        potentialPositions.add(new ChessPosition(currentRow - 1, currentCol + 2));
        // RightDown
        potentialPositions.add(new ChessPosition(currentRow - 2, currentCol + 1));

        for (ChessPosition pos : potentialPositions) {
            if (obCheck(pos, myBoard)) {
                continue;
            } else {
                potentialMoves.add(new ChessMove(currentPos, pos, null));
            }
        }

        return potentialMoves;
    }

    private Collection<ChessMove> rookMoves(ChessBoard myBoard, ChessPosition currentPos) {
        int currentRow = currentPos.getRow();
        int currentCol = currentPos.getColumn();

        return rowColCheck(myBoard, currentPos, currentRow, currentCol);
    }

    private Collection<ChessMove> pawnMoves(ChessBoard myBoard, ChessPosition currentPos) {
        int currentRow = currentPos.getRow();
        int currentCol = currentPos.getColumn();

        Collection<ChessPosition> potentialPositions = new HashSet<>();
        Collection<ChessMove> potentialMoves = new HashSet<>();

        // If pawn is in start position, it can move 2 squares
        // If pawn moves into opposing team's back line, it can receive a promotion

        // White team
        if (myBoard.getPiece(currentPos).pieceColor == ChessGame.TeamColor.WHITE) {
            // Look up once
            if (currentRow < 8) {
                ChessPosition above = new ChessPosition(currentRow + 1, currentCol);
                if (myBoard.getPiece(above) == null) {
                    potentialPositions.add(above);
                    ChessPosition twoAbove = new ChessPosition(currentRow + 2, currentCol);
                    if (currentRow == 2 && myBoard.getPiece(twoAbove) == null) {
                        // Look up twice if once is safe
                        potentialPositions.add(twoAbove);
                    }
                }
            }
            // Look at diagonals. Check that the piece there is an enemy
            if (currentRow < 8 && currentCol > 1) {
                ChessPosition upLeft = new ChessPosition(currentRow + 1, currentCol - 1);
                if (myBoard.getPiece(upLeft) != null && myBoard.getPiece(upLeft).pieceColor != this.pieceColor) {
                    potentialPositions.add(upLeft);
                }
            }
            if (currentRow < 8 && currentCol < 8) {
                ChessPosition upRight = new ChessPosition(currentRow + 1, currentCol + 1);
                if (myBoard.getPiece(upRight) != null && myBoard.getPiece(upRight).pieceColor != this.pieceColor) {
                    potentialPositions.add(upRight);
                }
            }
        }
        // Black team
        else if (myBoard.getPiece(currentPos).pieceColor == ChessGame.TeamColor.BLACK) {
            // Look down once
            if (currentRow > 1) {
                ChessPosition below = new ChessPosition(currentRow - 1, currentCol);
                if (myBoard.getPiece(below) == null) {
                    potentialPositions.add(below);
                    ChessPosition twoBelow = new ChessPosition(currentRow - 2, currentCol);
                    if (currentRow == 7 && myBoard.getPiece(twoBelow) == null) {
                        // Look down twice if once is safe
                        potentialPositions.add(twoBelow);
                    }
                }
            }
            // Look at diagonals. Check that the piece there is an enemy
            if (currentRow > 1 && currentCol > 1) {
                ChessPosition downLeft = new ChessPosition(currentRow - 1, currentCol - 1);
                if (myBoard.getPiece(downLeft) != null && myBoard.getPiece(downLeft).pieceColor != this.pieceColor) {
                    potentialPositions.add(downLeft);
                }
            }
            if (currentRow > 1 && currentCol < 8) {
                ChessPosition downRight = new ChessPosition(currentRow - 1, currentCol + 1);
                if (myBoard.getPiece(downRight) != null && myBoard.getPiece(downRight).pieceColor != this.pieceColor) {
                    potentialPositions.add(downRight);
                }
            }
        }

        for (ChessPosition pos : potentialPositions) {
            if (obCheck(pos, myBoard)) {
                continue;
            } else {
                // Check if pawn will move into enemy row. If so, you can do 4 additional moves
                if ((currentPos.getRow() == 7 && pos.getRow() == 8) || (currentPos.getRow() == 2 && pos.getRow() == 1)) {
                    potentialMoves.add(new ChessMove(currentPos, pos, PieceType.QUEEN));
                    potentialMoves.add(new ChessMove(currentPos, pos, PieceType.BISHOP));
                    potentialMoves.add(new ChessMove(currentPos, pos, PieceType.KNIGHT));
                    potentialMoves.add(new ChessMove(currentPos, pos, PieceType.ROOK));
                } else {
                    potentialMoves.add(new ChessMove(currentPos, pos, null));
                }
            }
        }

        return potentialMoves;
    }

    // HELPERS
    private Collection<ChessMove> rowColCheck(ChessBoard myBoard, ChessPosition currentPos, int currentRow, int currentCol) {
        Collection<ChessMove> potentialMoves = new HashSet<>();
        // Row checking UP
        for (int i = currentRow + 1; i <= 8; i++) {
            ChessPosition potentialPosition = new ChessPosition(i, currentCol);
            processPotMove(myBoard, currentPos, potentialPosition, potentialMoves);
            if (myBoard.getPiece(potentialPosition) != null) break;
        }
        // Row checking DOWN
        for (int i = currentRow - 1; i > 0; i--) {
            ChessPosition potentialPosition = new ChessPosition(i, currentCol);
            processPotMove(myBoard, currentPos, potentialPosition, potentialMoves);
            if (myBoard.getPiece(potentialPosition) != null) break;
        }
        // Column checking LEFT
        for (int j = currentCol - 1; j > 0; j--) {
            ChessPosition potentialPosition = new ChessPosition(currentRow, j);
            processPotMove(myBoard, currentPos, potentialPosition, potentialMoves);
            if (myBoard.getPiece(potentialPosition) != null) break;
        }
        // Column checking RIGHT
        for (int j = currentCol + 1; j <= 8; j++) {
            ChessPosition potentialPosition = new ChessPosition(currentRow, j);
            processPotMove(myBoard, currentPos, potentialPosition, potentialMoves);
            if (myBoard.getPiece(potentialPosition) != null) break;
        }
        return potentialMoves;
    }

    private Collection<ChessMove> diagCheck(ChessBoard myBoard, ChessPosition currentPos, int currentRow, int currentCol) {
        Collection<ChessMove> potentialMoves = new HashSet<>();

        // Diagonal checking UP-LEFT
        for (int i = currentRow + 1, j = currentCol - 1; i <= 8 && j > 0; i++, j--) {
            ChessPosition potentialPosition = new ChessPosition(i, j);
            processPotMove(myBoard, currentPos, potentialPosition, potentialMoves);
            if (myBoard.getPiece(potentialPosition) != null) break;
        }
        // Diagonal checking UP-RIGHT
        for (int i = currentRow + 1, j = currentCol + 1; i <= 8 && j <= 8; i++, j++) {
            ChessPosition potentialPosition = new ChessPosition(i, j);
            processPotMove(myBoard, currentPos, potentialPosition, potentialMoves);
            if (myBoard.getPiece(potentialPosition) != null) break;
        }
        // Diagonal checking DOWN-LEFT
        for (int i = currentRow - 1, j = currentCol - 1; i > 0 && j > 0; i--, j--) {
            ChessPosition potentialPosition = new ChessPosition(i, j);
            processPotMove(myBoard, currentPos, potentialPosition, potentialMoves);
            if (myBoard.getPiece(potentialPosition) != null) break;
        }
        // Diagonal checking DOWN-RIGHT
        for (int i = currentRow - 1, j = currentCol + 1; i > 0 && j <= 8; i--, j++) {
            ChessPosition potentialPosition = new ChessPosition(i, j);
            processPotMove(myBoard, currentPos, potentialPosition, potentialMoves);
            if (myBoard.getPiece(potentialPosition) != null) break;
        }

        return potentialMoves;
    }

    public void processPotMove(ChessBoard myBoard, ChessPosition currPos, ChessPosition potPos, Collection<ChessMove> potentialMoves) {
        ChessPiece potPiece = myBoard.getPiece(potPos);
        if (potPiece != null) {
            if (potPiece.pieceColor != this.pieceColor) {
                ChessMove potMove = new ChessMove(currPos, potPos, null);
                potentialMoves.add(potMove);
            }
            return;
        }

        ChessMove potMove = new ChessMove(currPos, potPos, null);
        potentialMoves.add(potMove);
    }

    private boolean obCheck(ChessPosition pos, ChessBoard myBoard) {
        boolean ob = false;
        // Remove out of bounds positions
        if (pos.getRow() < 1 || pos.getRow() > 8 || pos.getColumn() < 1 || pos.getColumn() > 8) {
            ob = true;
        }
        // Check if same team's piece is in the way
        else if (myBoard.getPiece(pos) != null && myBoard.getPiece(pos).pieceColor == this.pieceColor) {
            ob = true;
        }

        return ob;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceType == that.pieceType && pieceColor == that.pieceColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceType, pieceColor);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceType=" + pieceType +
                ", pieceColor=" + pieceColor +
                '}';
    }

    @Override
    public ChessPiece clone() {
        try {
            return (ChessPiece) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }
}
