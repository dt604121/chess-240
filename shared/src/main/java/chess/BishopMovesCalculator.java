package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        // use current position to calculate all valid moves!
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        // bishop: moves diagonally row -/+ 1 col +/- 1 (as long as it's valid)

        // look ahead to the next diagonal -> left x
        // loop until one of the following happens:
        for (int row = myPosition.getRow() + 1, col = myPosition.getColumn() + 1; row < 9 && col < 9; row += 1, col += 1){
            ChessPosition position = new ChessPosition(row, col);
            ChessPiece originalPiece = board.getPiece(myPosition);
            ChessPiece currentPiece = board.getPiece(position);
            if ( currentPiece == null) {
                ChessMove validMove = new ChessMove(myPosition, position, null);
                validMoves.add(validMove);
            }
            // about to run into a friendly piece depending on color..
            else {
                if (currentPiece.getTeamColor() == originalPiece.getTeamColor()) {
                    break;
                }
                // can capture an enemy
                ChessMove validMove = new ChessMove(myPosition, position, null);
                validMoves.add(validMove);
                break;
            }
        }

        return validMoves;
    }
}
