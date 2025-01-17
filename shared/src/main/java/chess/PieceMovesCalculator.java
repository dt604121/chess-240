package chess;

import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.util.ArrayList;
import java.util.Collection;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

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

    static void moveOnce(ChessBoard board, ChessPosition myPosition, ArrayList<ChessMove> validMoves,
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

    static void movePawn(ChessBoard board, ChessPosition myPosition, ArrayList<ChessMove> validMoves,
                         int rowDirection, int colDirectionLeft, int colDirectionMiddle, int colDirectionRight) {
        // start the offset -> look ahead
        int row = myPosition.getRow() + rowDirection;
        int col = myPosition.getColumn() + colDirectionMiddle;
        int twoRows = row + rowDirection;
        ChessPosition position = new ChessPosition(row, col);
        ChessPiece originalPiece = board.getPiece(myPosition);
        ChessPiece currentPiece = board.getPiece(position);
        ChessPosition nextPosition = new ChessPosition(twoRows, col);
        ChessPiece nextPiece = board.getPiece(nextPosition);
        ChessPosition leftPosition = new ChessPosition(row, colDirectionLeft);
        ChessPiece leftCol = board.getPiece(leftPosition);
        ChessPosition rightPosition = new ChessPosition(row, colDirectionRight);
        ChessPiece rightCol = board.getPiece(rightPosition);
        ChessPosition middlePosition = new ChessPosition(row, colDirectionMiddle);
        ChessPiece middleCol = board.getPiece(middlePosition);
        // bounds checking
        if (row < 9 && row >= 1 && col < 9 && col >= 1) {
            // 1st turn has the + 2 option -> row 2 for white row 7 for black)
            // is there an open space in front of the pawn?
            if (currentPiece == null) {
                ChessMove validMove = new ChessMove(myPosition, position, null);
                validMoves.add(validMove);

                // is there an open space 2 spaces away?
                if (nextPiece == null) {
                    if (currentPiece.getTeamColor() == WHITE) {
                        if (myPosition.getRow() == 2) {
                            ChessMove validTwoMove = new ChessMove(myPosition, nextPosition, null);
                            validMoves.add(validTwoMove);
                        }
                    } else if (currentPiece.getTeamColor() == BLACK) {
                        if (myPosition.getRow() == 7) {
                            ChessMove validTwoMove = new ChessMove(myPosition, nextPosition, null);
                            validMoves.add(validTwoMove);
                        }
                    }
                }
                // enemey piece - left/right diagonally
                else if (leftCol != null) {
                    if (currentPiece.getTeamColor() != nextPiece.getTeamColor()) {
                        ChessMove validLeftCaptureMove = new ChessMove(myPosition, leftPosition, null);
                        validMoves.add(validLeftCaptureMove);
                    }
                }
            } else if (rightCol != null) {
                if (currentPiece.getTeamColor() != nextPiece.getTeamColor()) {
                    ChessMove validRightCaptureMove = new ChessMove(myPosition, rightPosition, null);
                    validMoves.add(validRightCaptureMove);
                }
            }
        }

        // check for promotion -> queen, bishop, rook, knight 4 seperate moves
        // team color.. (row 8 for white and row 1 for black)
//        if (currentPiece == null) {
//            ChessMove validMove = new ChessMove(myPosition, position, null);
//            validMoves.add(validMove);
//             if (currentPiece.getTeamColor() == WHITE && row == 8) {
//                ChessMove validWhiteQueenPromotionMove = new ChessMove(myPosition, position, ChessPiece.PieceType.QUEEN);
//                ChessMove validWhiteBishopPromotionMove = new ChessMove(myPosition, position, ChessPiece.PieceType.BISHOP);
//                ChessMove validWhiteRookPromotionMove = new ChessMove(myPosition, position, ChessPiece.PieceType.ROOK);
//                ChessMove validWhiteKnightPromotionMove = new ChessMove(myPosition, position, ChessPiece.PieceType.KNIGHT);
//                validMoves.add(validWhiteQueenPromotionMove);
//                validMoves.add(validWhiteBishopPromotionMove);
//                validMoves.add(validWhiteRookPromotionMove);
//                validMoves.add(validWhiteKnightPromotionMove);
//            } else if (currentPiece.getTeamColor() == BLACK && row == 1) {
//                ChessMove validBlackQueenPromotionMove = new ChessMove(myPosition, position, ChessPiece.PieceType.QUEEN);
//                ChessMove validBlackBishopPromotionMove = new ChessMove(myPosition, position, ChessPiece.PieceType.BISHOP);
//                ChessMove validBlackRookPromotionMove = new ChessMove(myPosition, position, ChessPiece.PieceType.ROOK);
//                ChessMove validBlackKnightPromotionMove = new ChessMove(myPosition, position, ChessPiece.PieceType.KNIGHT);
//                validMoves.add(validBlackQueenPromotionMove);
//                validMoves.add(validBlackBishopPromotionMove);
//                validMoves.add(validBlackRookPromotionMove);
//                validMoves.add(validBlackKnightPromotionMove);
//            }
//        }
    }
}
