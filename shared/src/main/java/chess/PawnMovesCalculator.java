package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        // row + 1 (unless 1st turn then + 2 option)
        // check for promotion
            // team color.. (row 8 for white and row 1 for black)
        // implied validity
            // capturing diagonally.. etc.
        return validMoves;
    }
}
