package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        // Find all combinations of the L..
        // move the row +/- 2
        // L -> row + 2 and col + 1
        PieceMovesCalculator.moveOnce(board, myPosition, validMoves, 2, 1);
        // L -> row + 2 and col - 1
        PieceMovesCalculator.moveOnce(board, myPosition, validMoves, 2, -1);
        // L -> row - 2 and col + 1
        PieceMovesCalculator.moveOnce(board, myPosition, validMoves, -2, 1);
        // L -> row - 2 and col - 1
        PieceMovesCalculator.moveOnce(board, myPosition, validMoves, -2, -1);

        // move the row +/- 1
        // L -> row + 1 and col + 2
        PieceMovesCalculator.moveOnce(board, myPosition, validMoves, 1, 2);
        // L -> row + 1 and col - 2
        PieceMovesCalculator.moveOnce(board, myPosition, validMoves, 1, -2);
        // L -> row - 1 and col - 2
        PieceMovesCalculator.moveOnce(board, myPosition, validMoves, -1, -2);
        // L -> row - 1 and col + 2
        PieceMovesCalculator.moveOnce(board, myPosition, validMoves, -1, 2);

        return validMoves;
    }
}
