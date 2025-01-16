package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        // rook row += # -> forward
        PieceMovesCalculator.moveToEnd(board, myPosition, validMoves, 1, 0);
        PieceMovesCalculator.moveToEnd(board, myPosition, validMoves, 0, 1);
        PieceMovesCalculator.moveToEnd(board, myPosition, validMoves, -1, 0);
        PieceMovesCalculator.moveToEnd(board, myPosition, validMoves, 0, -1);

        return validMoves;
    }
}
