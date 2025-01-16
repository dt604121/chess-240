package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QueenMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        // functionality of the rook and bishop..
        // call rookMoves() and bishopMoves()? (row +/- #) or (row -/+ # col +/- #)
        // look into combining two lists
        // validity..
        return validMoves;
    }
}
