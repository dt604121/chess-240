package chess;

import java.util.ArrayList;
import java.util.Collection;

public interface PieceMovesCalculator {
    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);
    static void moveToEnd(ChessBoard board, ChessPosition myPosition, ArrayList<ChessMove> validMoves,
                          int rowDirection, int colDirection) {
        // start the offset -> look ahead
        for (int row = myPosition.getRow() + rowDirection, col = myPosition.getColumn() + colDirection;
             row < 9 && row >= 1 && col < 9 && col >= 1; row += rowDirection, col += colDirection){
            ChessPosition position = new ChessPosition(row, col);
            ChessPiece originalPiece = board.getPiece(myPosition);
            ChessPiece currentPiece = board.getPiece(position);
            // open space?
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
