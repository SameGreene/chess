package chess;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    private final ChessPosition startPosition;
    private final ChessPosition endPostion;
    private final ChessPiece.PieceType promotionPiece;
    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPostion = endPosition;
        this.promotionPiece = promotionPiece;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return this.startPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return this.endPostion;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return this.promotionPiece;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash = startPosition.getRow() * 157 + startPosition.getColumn();
        hash += endPostion.getRow() * 31 + endPostion.getColumn();
        if(promotionPiece != null){
            switch (promotionPiece){
                case QUEEN:
                    hash += 11;
                    break;
                case BISHOP:
                    hash += 17;
                    break;
                case KNIGHT:
                    hash += 23;
                    break;
                case ROOK:
                    hash += 29;
                    break;
                default:
                    hash += 1;
            }
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this){
            return true;
        }

        if(!(obj instanceof ChessMove)){
            return false;
        }

        ChessMove c = (ChessMove) obj;

        return this.startPosition.equals(c.startPosition) && this.endPostion.equals(c.endPostion) && promotionPiece == c.promotionPiece;
    }

    @Override
    public String toString() {
        return "Move{" +
                startPosition +
                ", " + endPostion +
                ", " + promotionPiece +
                "}\n";
    }
}
