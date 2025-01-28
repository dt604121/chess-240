package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        // functionality of a queen but w/only 1 move a.k.a row + 1 and col + 1 away from myPosition
        PieceMovesCalculator.moveOnce(board, myPosition, validMoves, 1, 1);
        // lower left x
        PieceMovesCalculator.moveOnce(board, myPosition, validMoves, -1, -1);
        // upper left x
        PieceMovesCalculator.moveOnce(board, myPosition, validMoves, 1, -1);
        // lower right x
        PieceMovesCalculator.moveOnce(board, myPosition, validMoves, -1, 1);
        PieceMovesCalculator.moveOnce(board, myPosition, validMoves, 1, 0);
        PieceMovesCalculator.moveOnce(board, myPosition, validMoves, 0, 1);
        PieceMovesCalculator.moveOnce(board, myPosition, validMoves, -1, 0);
        PieceMovesCalculator.moveOnce(board, myPosition, validMoves, 0, -1);
        return validMoves;
    }
}
