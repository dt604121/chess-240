package chess;

import java.util.ArrayList;
import java.util.Collection;

// TODO: implement the Knight!
public class KnightMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        // Find all combinations of the L..
        // move the row +/- 2
        // L -> row + 2 and col + 1
        // L -> row + 2 and col - 1
        // L -> row - 2 and col + 1
        // L -> row - 2 and col - 1

        // move the row +/- 1
        // L -> row + 1 and col + 2
        // L -> row + 1 and col - 2
        // L -> row - 1 and col - 2
        // L -> row - 1 and col + 2

        for (int row = myPosition.getRow(), col = myPosition.getColumn(); row < 9 && col < 9; row += 2, col += 1){
            ChessPosition position = new ChessPosition(row, col);
            ChessPiece originalPiece = board.getPiece(myPosition);
            ChessPiece currentPiece = board.getPiece(myPosition);

            if (currentPiece == null){
                ChessMove validMove = new ChessMove(myPosition, position, null);
                validMoves.add(validMove);
            }
            else{
                // friendly fire?
                if (currentPiece.getTeamColor() == originalPiece.getTeamColor()){
                    break;
                }
                // capture enemy?
                ChessMove validMove = new ChessMove(myPosition, position, null);
                validMoves.add(validMove);
                break;
            }
        }

        return validMoves;
    }
}
