package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard chessBoard;
    private TeamColor currentTurn;
    public ChessGame() {
        this.chessBoard = new ChessBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currentTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece currPiece = chessBoard.getPiece(startPosition);
        if(currPiece.pieceMoves(chessBoard, startPosition).isEmpty()){
            return null;
        }
        else{
            return currPiece.pieceMoves(chessBoard, startPosition);
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPos = move.getStartPosition();
        ChessPosition endPos = move.getEndPosition();
        ChessPiece.PieceType promoPiece = move.getPromotionPiece();

        // Get current piece
        ChessPiece currPiece = this.chessBoard.getPiece(startPos);

        // Check that it's the piece's turn
        if(currPiece.getTeamColor() == getTeamTurn()) {
            Collection<ChessMove> valMoves = validMoves(startPos);
            // Is move valid?
            if (valMoves == null) {
                throw new InvalidMoveException("ERROR: NO VALID MOVES EXIST");
            }
            else if (!(valMoves.contains(move))) {
                throw new InvalidMoveException("ERROR: INVALID MOVE");
            }
            // Are we in check?
            if (isInCheck(getTeamTurn())) {
                // Will this move take us out of check?
                if (this.chessBoard.getPiece(endPos) != null && this.chessBoard.getPiece(endPos).getTeamColor() != currentTurn) {
                    // Create alternate universe game to see if move will take us out of check
                    ChessGame altGame = this;
                    altGame.chessBoard.addPiece(endPos, currPiece);
                    altGame.chessBoard.addPiece(startPos, null);
                    if (altGame.isInCheck(getTeamTurn())) {
                        throw new InvalidMoveException("ERROR: CURRENTLY IN CHECK");
                    }
                }
                else{
                    throw new InvalidMoveException("ERROR: CURRENTLY IN CHECK");
                }
            }
            // Nothing is stopping the move. Let it happen!
            else {
                this.chessBoard.addPiece(endPos, currPiece);
                this.chessBoard.addPiece(startPos, null);
            }
        }
        else{
            throw new InvalidMoveException("ERROR: WRONG TURN");
        }
        // Change turn to the other team
        currentTurn = currPiece.getTeamColor() == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        TeamColor attackingTeam = teamColor == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;

        boolean inCheck = false;
        for (int i = 1; i < 8; i ++){
            for (int j = 1; j < 8; j++){
                ChessPosition attackingPosition = new ChessPosition(i, j);
                ChessPiece attackingPiece = this.chessBoard.getPiece(attackingPosition);
                if (attackingPiece != null && attackingPiece.getTeamColor() == attackingTeam){
                    Collection<ChessMove> attackingMoves = attackingPiece.pieceMoves(this.chessBoard, attackingPosition);
                    for (ChessMove move : attackingMoves){
                        if (this.chessBoard.getPiece(move.getEndPosition()) != null && this.chessBoard.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.KING && this.chessBoard.getPiece(move.getEndPosition()).getTeamColor() != attackingTeam){
                            inCheck = true;
                        }
                    }
                }
            }
        }
        return inCheck;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.chessBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.chessBoard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return Objects.deepEquals(chessBoard, chessGame.chessBoard) && currentTurn == chessGame.currentTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chessBoard, currentTurn);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "chessBoard=" + chessBoard +
                ", currentTurn=" + currentTurn +
                '}';
    }
}
