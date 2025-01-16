package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        // use current position to calculate all valid moves!
        // bishop: moves diagonally row -/+ 1 col +/- 1 (as long as it's valid)
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        // look ahead to the next diagonal -> upper right x
        PieceMovesCalculator.moveToEnd(board, myPosition, validMoves, 1, 1);
        // lower left x
        PieceMovesCalculator.moveToEnd(board, myPosition, validMoves, -1, -1);
        // upper left x
        PieceMovesCalculator.moveToEnd(board, myPosition, validMoves, 1, -1);
        // lower right x
        PieceMovesCalculator.moveToEnd(board, myPosition, validMoves, -1, 1);

        return validMoves;
    }
}