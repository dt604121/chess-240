package chess;

import java.util.ArrayList;
import java.util.Collection;

import static chess.ChessGame.TeamColor.WHITE;

public class PawnMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        // first turn is row + 1  unless we can go 2 (accounted for w/i the function)
        ChessPiece piece = board.getPiece(myPosition);
        if (piece.getTeamColor() == WHITE) {
            // White - moving up
            PieceMovesCalculator.movePawn(board, myPosition, validMoves,
                    1, -1, 0, 1);
        }
        else {
            // Black - moving down
            PieceMovesCalculator.movePawn(board, myPosition, validMoves,
                    -1, -1, 0, 1);
        }
        return validMoves;
    }
}
