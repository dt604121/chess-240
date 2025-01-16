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
        PieceMovesCalculator.moveToEnd(board, myPosition, validMoves, 2, 1);
        // L -> row + 2 and col - 1
        PieceMovesCalculator.moveToEnd(board, myPosition, validMoves, 2, -1);
        // L -> row - 2 and col + 1
        PieceMovesCalculator.moveToEnd(board, myPosition, validMoves, -2, 1);
        // L -> row - 2 and col - 1
        PieceMovesCalculator.moveToEnd(board, myPosition, validMoves, -2, -1);

        // move the row +/- 1
        // L -> row + 1 and col + 2
        PieceMovesCalculator.moveToEnd(board, myPosition, validMoves, 1, 2);
        // L -> row + 1 and col - 2
        PieceMovesCalculator.moveToEnd(board, myPosition, validMoves, 1, -2);
        // L -> row - 1 and col - 2
        PieceMovesCalculator.moveToEnd(board, myPosition, validMoves, -1, -2);
        // L -> row - 1 and col + 2
        PieceMovesCalculator.moveToEnd(board, myPosition, validMoves, -1, 2);

        return validMoves;
    }
    static void moveL(ChessBoard board, ChessPosition myPosition, ArrayList<ChessMove> validMoves,
                          int rowDirection, int colDirection) {
        // start the offset -> look ahead
        int row = myPosition.getRow() + rowDirection;
        int col = myPosition.getColumn() + colDirection;
        if (row < 9 && row >= 1 && col < 9 && col >= 1) {
            ChessPosition position = new ChessPosition(row, col);
            ChessPiece originalPiece = board.getPiece(myPosition);
            ChessPiece currentPiece = board.getPiece(position);
            // open space?
            if (currentPiece == null) {
                ChessMove validMove = new ChessMove(myPosition, position, null);
                validMoves.add(validMove);
            }
            // about to run into a friendly piece depending on color..
            else {
                if (currentPiece.getTeamColor() == originalPiece.getTeamColor()) {
                    return;
                }
                // can capture an enemy
                ChessMove validMove = new ChessMove(myPosition, position, null);
                validMoves.add(validMove);
            }
        }
    }
}
