package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KingMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        // functionality of a queen but w/only 1 move a.k.a row + 1 and col + 1 away from myPosition
        // make sure the move is valid
            // opponent can't capture the king
            // calculate if the king is in danger.. do a check to make sure they move if so
        return validMoves;
    }
}
