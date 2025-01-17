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
        /* a) make an override of each of the functions to only move once (get rid of the for loop)
        b) create new functions (from the copy of the ones I've done) but remove the for loop
        c) do something else entirely
        ** join then together as done in queen **
         */
        // make sure the move is valid
            // opponent can't capture the king
            // check the piece type and run it's designated function to do the check on if it could?
            // calculate if the king is in danger.. do a check to make sure they move
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
