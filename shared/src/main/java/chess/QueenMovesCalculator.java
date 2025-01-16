package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        // functionality of the rook and bishop..
        // call rookMoves and bishopMoves? (row +/- #) or (row -/+ # col +/- #)
        RookMovesCalculator rookCalculator = new RookMovesCalculator();
        BishopMovesCalculator bishopCalculator = new BishopMovesCalculator();

        Collection<ChessMove> rookMoves = rookCalculator.pieceMoves(board, myPosition);
        Collection<ChessMove> bishopMoves = bishopCalculator.pieceMoves(board, myPosition);

        // look into combining two lists
        validMoves.addAll(rookMoves);
        validMoves.addAll(bishopMoves);
        return validMoves;
    }
}
