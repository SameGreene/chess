package chess;

import java.util.Arrays;
import java.util.Collection;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard implements Cloneable{

    private ChessPiece[][] board = new ChessPiece[8][8];
    public ChessBoard() {}

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow() - 1][position.getColumn() - 1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */

    public void resetBoard() {
//        Hard reset board into black vs. white with proper pieces
//        Instead of doing board[x][y] = chessPiece, you can do board[x][y] = King
        board = new ChessPiece[8][8];

        // White team
        this.addPiece(new ChessPosition(1,1), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        this.addPiece(new ChessPosition(1, 2), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        this.addPiece(new ChessPosition(1, 3), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        this.addPiece(new ChessPosition(1, 4), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        this.addPiece(new ChessPosition(1, 5), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
        this.addPiece(new ChessPosition(1, 6), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        this.addPiece(new ChessPosition(1, 7), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        this.addPiece(new ChessPosition(1, 8), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        this.addPiece(new ChessPosition(2,1), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        this.addPiece(new ChessPosition(2,2), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        this.addPiece(new ChessPosition(2,3), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        this.addPiece(new ChessPosition(2,4), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        this.addPiece(new ChessPosition(2,5), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        this.addPiece(new ChessPosition(2,6), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        this.addPiece(new ChessPosition(2,7), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        this.addPiece(new ChessPosition(2,8), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));

        // Black team
        this.addPiece(new ChessPosition(8,1), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        this.addPiece(new ChessPosition(8, 2), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        this.addPiece(new ChessPosition(8, 3), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        this.addPiece(new ChessPosition(8, 4), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
        this.addPiece(new ChessPosition(8, 5), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
        this.addPiece(new ChessPosition(8, 6), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        this.addPiece(new ChessPosition(8, 7), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        this.addPiece(new ChessPosition(8, 8), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        this.addPiece(new ChessPosition(7,1), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        this.addPiece(new ChessPosition(7,2), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        this.addPiece(new ChessPosition(7,3), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        this.addPiece(new ChessPosition(7,4), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        this.addPiece(new ChessPosition(7,5), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        this.addPiece(new ChessPosition(7,6), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        this.addPiece(new ChessPosition(7,7), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        this.addPiece(new ChessPosition(7,8), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(board, that.board);
    }

    // COLORS
    String reset = "\u001B[0m";
    String black = "\u001B[30m";
    String red = "\u001B[31m";
    String blue = "\u001B[34m";
    String bgWhite = "\u001B[47m";
    String bgGreen = "\u001B[42m";
    String bgYellow = "\u001B[43m";
    String bgBlack = "\u001B[40m";

    public String toString(String teamColor) {
        StringBuilder sb = new StringBuilder();

        sb = buildBoard(teamColor, sb, reset, black, red, blue, bgWhite, bgBlack);

        return sb.toString();
    }

    private StringBuilder buildBoard(String teamColor, StringBuilder sb, String reset, String black, String red, String blue, String bgWhite, String bgBlack) {
        sb.append(black).append(String.format("%4s", " ")).append("a  b  c  d  e  f  g  h").append(reset).append("\n");

        if (teamColor == null || teamColor.equals("WHITE")) {
            for (int i = 7; i >= 0; i--) {
                appendRow(sb, i, reset, black, red, blue, bgWhite, bgBlack);
            }
        } else {
            for (int i = 0; i < 8; i++) {
                appendRow(sb, i, reset, black, red, blue, bgWhite, bgBlack);
            }
        }

        sb.append(black).append(String.format("%4s", " ")).append("a  b  c  d  e  f  g  h").append(reset).append("\n");
        return sb;
    }

    public String toStringHighlighted(String teamColor, Collection<ChessMove> possibleMoves, ChessPosition yellowPos) {
        StringBuilder sb = new StringBuilder();

        sb.append(black).append(String.format("%4s", " ")).append("a  b  c  d  e  f  g  h").append(reset).append("\n");

        if (teamColor == null || teamColor.equals("WHITE")) {
            for (int i = 7; i >= 0; i--) {
                sb = appendHighlightedRow(sb, i, reset, black, red, blue, bgWhite, bgBlack, possibleMoves, yellowPos);
            }
        } else {
            for (int i = 0; i < 8; i++) {
                sb = appendHighlightedRow(sb, i, reset, black, red, blue, bgWhite, bgBlack, possibleMoves, yellowPos);
            }
        }

        sb.append(black).append(String.format("%4s", " ")).append("a  b  c  d  e  f  g  h").append(reset).append("\n");
        return sb.toString();
    }

    private StringBuilder appendHighlightedRow(StringBuilder sb, int i, String reset, String black, String red, String blue, String bgWhite, String bgBlack, Collection<ChessMove> possibleMoves, ChessPosition yellowPos) {
        sb.append(black).append(String.format("%2d", i + 1)).append(" ");
        for (int j = 0; j < 8; j++) {
            ChessPosition currentPosition = new ChessPosition(i + 1, j + 1);
            String bg = ((i + j) % 2 == 0) ? bgWhite : bgBlack;
            sb.append(bg);
            if (isPossibleMove(currentPosition, possibleMoves)) {
                sb.append(bgGreen);
            }
            else if (currentPosition.equals(yellowPos)){
                sb.append(bgYellow);
            }
            if (board[i][j] == null) {
                sb.append(String.format("%3s", " ")).append(reset); // Increased space for empty squares
            } else {
                String color = (board[i][j].getTeamColor() == ChessGame.TeamColor.WHITE) ? red : blue;
                char pieceChar;
                switch (board[i][j].getPieceType()) {
                    case PAWN: pieceChar = 'P'; break;
                    case ROOK: pieceChar = 'R'; break;
                    case KNIGHT: pieceChar = 'N'; break;
                    case BISHOP: pieceChar = 'B'; break;
                    case KING: pieceChar = 'K'; break;
                    case QUEEN: pieceChar = 'Q'; break;
                    default: pieceChar = ' '; break;
                }
                sb.append(color).append(" ").append(pieceChar).append(" ").append(reset); // Added a space before and after each piece
            }
        }
        sb.append(black).append(" ").append(String.format("%2d", i + 1)).append(reset).append("\n");

        return sb;
    }

    private boolean isPossibleMove(ChessPosition position, Collection<ChessMove> possibleMoves) {
        if (possibleMoves != null) {
            for (ChessMove move : possibleMoves) {
                if (move.getEndPosition().equals(position)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void appendRow(StringBuilder sb, int i, String reset, String black, String red, String blue, String bgWhite, String bgBlack) {
        sb.append(black).append(String.format("%2d", i + 1)).append(" ");
        for (int j = 0; j < 8; j++) {
            String bg = ((i + j) % 2 == 0) ? bgWhite : bgBlack;
            if (board[i][j] == null) {
                sb.append(bg).append(String.format("%3s", " ")).append(reset); // Increased space for empty squares
            } else {
                String color = (board[i][j].getTeamColor() == ChessGame.TeamColor.WHITE) ? red : blue;
                char pieceChar;
                switch (board[i][j].getPieceType()) {
                    case PAWN: pieceChar = 'P'; break;
                    case ROOK: pieceChar = 'R'; break;
                    case KNIGHT: pieceChar = 'N'; break;
                    case BISHOP: pieceChar = 'B'; break;
                    case KING: pieceChar = 'K'; break;
                    case QUEEN: pieceChar = 'Q'; break;
                    default: pieceChar = ' '; break;
                }
                sb.append(bg).append(color).append(" ").append(pieceChar).append(" ").append(reset); // Added a space before and after each piece
            }
        }
        sb.append(black).append(" ").append(String.format("%2d", i + 1)).append(reset).append("\n");
    }

    @Override
    public ChessBoard clone() {
        try {
            ChessBoard clone = (ChessBoard) super.clone();
            clone.board = new ChessPiece[8][8];

            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if (this.board[row][col] != null) {
                        clone.board[row][col] = this.board[row][col].clone();
                    }
                }
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);  // This should not happen
        }
    }
}
