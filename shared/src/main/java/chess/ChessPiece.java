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

    private Collection<ChessMove> queenMoves(ChessBoard myBoard, ChessPosition currentPos) {
        int currentRow = currentPos.getRow();
        int currentCol = currentPos.getColumn();

        Collection<ChessMove> potentialMoves = new HashSet<>();
        Collection<ChessPosition> potentialPositions = new HashSet<>();

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
        // Column checking LEFT
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
        // Column checking RIGHT
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
        // Diagonal checking UP-LEFT
        for (int i = currentRow + 1, j = currentCol - 1; i <= 8 && j > 0; i++, j--) {
            ChessPosition potentialPosition = new ChessPosition(i, j);
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
        // Diagonal checking UP-RIGHT
        for (int i = currentRow + 1, j = currentCol + 1; i <= 8 && j <= 8; i++, j++) {
            ChessPosition potentialPosition = new ChessPosition(i, j);
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
        // Diagonal checking DOWN-LEFT
        for (int i = currentRow - 1, j = currentCol - 1; i > 0 && j > 0; i--, j--) {
            ChessPosition potentialPosition = new ChessPosition(i, j);
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
        // Diagonal checking DOWN-RIGHT
        for (int i = currentRow - 1, j = currentCol + 1; i > 0 && j <= 8; i--, j++) {
            ChessPosition potentialPosition = new ChessPosition(i, j);
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

    private Collection<ChessMove> bishopMoves(ChessBoard myBoard, ChessPosition currentPos) {
        int currentRow = currentPos.getRow();
        int currentCol = currentPos.getColumn();

        Collection<ChessMove> potentialMoves = new HashSet<>();
        Collection<ChessPosition> potentialPositions = new HashSet<>();

        // Diagonal checking UP-LEFT
        for (int i = currentRow + 1, j = currentCol - 1; i <= 8 && j > 0; i++, j--) {
            ChessPosition potentialPosition = new ChessPosition(i, j);
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
        // Diagonal checking UP-RIGHT
        for (int i = currentRow + 1, j = currentCol + 1; i <= 8 && j <= 8; i++, j++) {
            ChessPosition potentialPosition = new ChessPosition(i, j);
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
        // Diagonal checking DOWN-LEFT
        for (int i = currentRow - 1, j = currentCol - 1; i > 0 && j > 0; i--, j--) {
            ChessPosition potentialPosition = new ChessPosition(i, j);
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
        // Diagonal checking DOWN-RIGHT
        for (int i = currentRow - 1, j = currentCol + 1; i > 0 && j <= 8; i--, j++) {
            ChessPosition potentialPosition = new ChessPosition(i, j);
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
        // Column checking LEFT
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
        // Column checking RIGHT
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

        return potentialMoves;
    }

    private Collection<ChessMove> pawnMoves(ChessBoard myBoard, ChessPosition currentPos) {
        int currentRow = currentPos.getRow();
        int currentCol = currentPos.getColumn();

        Collection<ChessPosition> potentialPositions = new HashSet<>();
        Collection<ChessMove> potentialMoves = new HashSet<>();

        // If pawn is in start position, it can move 2 squares
        // If pawn moves into opposing team's back line, it can receive a promotion
        // TODO: EN PASSANT

        // White team
        if (myBoard.getPiece(currentPos).pieceColor == ChessGame.TeamColor.WHITE) {
            // Look up once
            ChessPosition above = new ChessPosition(currentRow + 1, currentCol);
            if(myBoard.getPiece(above) == null){
                potentialPositions.add(above);
                ChessPosition twoAbove = new ChessPosition(currentRow + 2, currentCol);
                if (currentRow == 2 && myBoard.getPiece(twoAbove) == null) {
                    // Look up twice if once is safe
                    potentialPositions.add(twoAbove);
                }
            }
            // Look at diagonals. Check that the piece there is an enemy
            ChessPosition upLeft = new ChessPosition(currentRow + 1, currentCol - 1);
            ChessPosition upRight = new ChessPosition(currentRow + 1, currentCol + 1);
            if (myBoard.getPiece(upLeft) != null && myBoard.getPiece(upLeft).pieceColor != this.pieceColor) {
                potentialPositions.add(upLeft);
            }
            if (myBoard.getPiece(upRight) != null && myBoard.getPiece(upRight).pieceColor != this.pieceColor) {
                potentialPositions.add(upRight);
            }
        }
        // Black team
        else if (myBoard.getPiece(currentPos).pieceColor == ChessGame.TeamColor.BLACK) {
            // Look down once
            ChessPosition below = new ChessPosition(currentRow - 1, currentCol);
            if(myBoard.getPiece(below) == null){
                potentialPositions.add(below);
                ChessPosition twoBelow = new ChessPosition(currentRow - 2, currentCol);
                if (currentRow == 7 && myBoard.getPiece(twoBelow) == null) {
                    // Look down twice if once is safe
                    potentialPositions.add(twoBelow);
                }
            }
            // Look at diagonals. Check that the piece there is an enemy
            ChessPosition downLeft = new ChessPosition(currentRow - 1, currentCol - 1);
            ChessPosition downRight = new ChessPosition(currentRow - 1, currentCol + 1);
            if (myBoard.getPiece(downLeft) != null && myBoard.getPiece(downLeft).pieceColor != this.pieceColor) {
                potentialPositions.add(downLeft);
            }
            if (myBoard.getPiece(downRight) != null && myBoard.getPiece(downRight).pieceColor != this.pieceColor) {
                potentialPositions.add(downRight);
            }
        }

        for (ChessPosition pos: potentialPositions) {
            // Remove out of bounds positions
            if (pos.getRow() < 1 || pos.getRow() > 8 || pos.getColumn() < 1 || pos.getColumn() > 8) {
                continue;
            }
            // Check if any piece is in the way
            if (myBoard.getPiece(pos) != null && myBoard.getPiece(pos).pieceColor == this.pieceColor) {
                continue;
            }
            else{
                // Check if pawn will move into enemy row. If so, you can do 4 additional moves
                if ((currentPos.getRow() == 7 && pos.getRow() == 8) || (currentPos.getRow() == 2 && pos.getRow() == 1)) {
                    if (myBoard.getPiece(pos) != null && myBoard.getPiece(pos).pieceColor == ChessGame.TeamColor.WHITE) {
                        potentialMoves.add(new ChessMove(currentPos, pos, PieceType.QUEEN));
                        potentialMoves.add(new ChessMove(currentPos, pos, PieceType.BISHOP));
                        potentialMoves.add(new ChessMove(currentPos, pos, PieceType.KNIGHT));
                        potentialMoves.add(new ChessMove(currentPos, pos, PieceType.ROOK));
                    }
                    else if (myBoard.getPiece(pos) != null && myBoard.getPiece(pos).pieceColor == ChessGame.TeamColor.BLACK) {
                        potentialMoves.add(new ChessMove(currentPos, pos, PieceType.QUEEN));
                        potentialMoves.add(new ChessMove(currentPos, pos, PieceType.BISHOP));
                        potentialMoves.add(new ChessMove(currentPos, pos, PieceType.KNIGHT));
                        potentialMoves.add(new ChessMove(currentPos, pos, PieceType.ROOK));
                    }
                    else if (myBoard.getPiece(pos) == null) {
                        potentialMoves.add(new ChessMove(currentPos, pos, PieceType.QUEEN));
                        potentialMoves.add(new ChessMove(currentPos, pos, PieceType.BISHOP));
                        potentialMoves.add(new ChessMove(currentPos, pos, PieceType.KNIGHT));
                        potentialMoves.add(new ChessMove(currentPos, pos, PieceType.ROOK));
                    }
                }
                else{
                    potentialMoves.add(new ChessMove(currentPos, pos, null));
                }
            }
        }

        return potentialMoves;
    }
}
