package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator implements PieceMovesCalculator {
    // TODO: implement the King!
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        // functionality of a queen but w/only 1 move a.k.a row + 1 and col + 1 away from myPosition
        // hmm.. how do we actually go about doing this?
        // make sure the move is valid
            // opponent can't capture the king
            // calculate if the king is in danger.. do a check to make sure they move if so
        return validMoves;
    }
}
