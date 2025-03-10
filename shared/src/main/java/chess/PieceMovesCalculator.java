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
             row < 9 && row >= 1 && col < 9 && col >= 1; row += rowDirection, col += colDirection) {
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
    // psuedo code for pawn
    // if in bounds
        // If there is an open space in front of the pawn:
            // If it is the first move:
                // If the square two steps ahead is also open:
                    // Add the two-square move to validMoves
                // Else:
                    // Return (movement is blocked)
            // Else (not the first move):
                // Add the one-square move to validMoves
                // if the pawn reaches the end of the board:
                    // Add the promotion moves (Queen, Rook, Bishop, Knight) to validMoves

        // Else if there is an enemy piece in a diagonal direction:
            // Check the diagonal left and diagonal right:
                // If there is an enemy piece:
                    // Add the capture move to validMoves
                        // if the pawn reaches the end of the board:
                            // Add the promotion moves (Queen, Rook, Bishop, Knight) to validMoves

    static void movePawn(ChessBoard board, ChessPosition myPosition, ArrayList<ChessMove> validMoves,
                         int rowDirection, int colDirectionLeft, int colDirectionMiddle, int colDirectionRight) {
        // start the offset -> look ahead
        int row = myPosition.getRow() + rowDirection;
        int col = myPosition.getColumn() + colDirectionMiddle;
        int colLeft = myPosition.getColumn() + colDirectionLeft;
        int colRight = myPosition.getColumn() + colDirectionRight;
        ChessPosition position = new ChessPosition(row, col);
        ChessPiece originalPiece = board.getPiece(myPosition);
        ChessPiece currentPiece = board.getPiece(position);
        // bounds checking
        if (row < 9 && row >= 1 && col < 9 && col >= 1) {
            // is there an open space in front of the pawn?
            if (currentPiece == null){
                if (originalPiece.getTeamColor() == WHITE && row == 8 ||
                        originalPiece.getTeamColor() == BLACK && row == 1) {
                    // check for promotion -> queen, bishop, rook, knight 4 sep. moves
                    // team color -> (row 8 for white and row 1 for black)
                    // make the types a list and then iterate through it!
                    ChessPiece.PieceType[] promotionTypes = {
                            ChessPiece.PieceType.QUEEN,
                            ChessPiece.PieceType.BISHOP,
                            ChessPiece.PieceType.ROOK,
                            ChessPiece.PieceType.KNIGHT
                    };
                    for (ChessPiece.PieceType promotionType : promotionTypes) {
                        validMoves.add(new ChessMove(myPosition, position, promotionType));
                    }
                }
                else {
                    ChessMove validMove = new ChessMove(myPosition, position, null);
                    validMoves.add(validMove);
                }

                // is there an open space 2 spaces away?
                // 1st turn has the + 2 option -> row 2 for white row 7 for black)
                int twoRows = row + rowDirection;

                if (twoRows < 9 && twoRows >= 1) {
                    ChessPosition nextPosition = new ChessPosition(twoRows, col);
                    ChessPiece nextPiece = board.getPiece(nextPosition);

                    if (nextPiece == null && originalPiece.getTeamColor() == WHITE && myPosition.getRow() == 2) {
                        ChessMove validTwoMove = new ChessMove(myPosition, nextPosition, null);
                        validMoves.add(validTwoMove);
                    }
                    else if (originalPiece.getTeamColor() == BLACK && myPosition.getRow() == 7) {
                        ChessMove validTwoMove = new ChessMove(myPosition, nextPosition, null);
                        validMoves.add(validTwoMove);
                    }
                }
            }
            // enemy piece - left/right diagonally
            diagonalEnemyPieceChecker(colLeft, originalPiece, row, board, validMoves, myPosition);
            diagonalEnemyPieceChecker(colRight, originalPiece, row, board, validMoves, myPosition);
        }
    }
    static void diagonalEnemyPieceChecker(int colPositionDirection, ChessPiece originalPiece, int row, ChessBoard board,
                                          ArrayList<ChessMove> validMoves, ChessPosition myPosition){
        if (colPositionDirection < 9 && colPositionDirection >= 1) {
            ChessPosition colPosition = new ChessPosition(row, colPositionDirection);
            ChessPiece colPieceDirection = board.getPiece(colPosition);
            if ((colPieceDirection != null) && (originalPiece.getTeamColor() != colPieceDirection.getTeamColor())) {
                if (originalPiece.getTeamColor() == WHITE  && row == 8 || originalPiece.getTeamColor() == BLACK
                        && row == 1){
                    // check for promotion -> queen, bishop, rook, knight 4 sep. moves
                    // team color -> (row 8 for white and row 1 for black)
                    // make the types a list and then iterate through it!
                    ChessPiece.PieceType[] promotionTypes = {
                            ChessPiece.PieceType.QUEEN,
                            ChessPiece.PieceType.BISHOP,
                            ChessPiece.PieceType.ROOK,
                            ChessPiece.PieceType.KNIGHT
                    };
                    for (ChessPiece.PieceType promotionType : promotionTypes) {
                        validMoves.add(new ChessMove(myPosition, colPosition, promotionType));
                    }
                }
                else {
                    ChessMove validCaptureMove = new ChessMove(myPosition, colPosition, null);
                    validMoves.add(validCaptureMove);
                }
            }
        }
    }
}