package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import static ui.EscapeSequences.*;

public class ChessBoardUI {
    private static final int BOARD_SIZE = 8;
    private static final String SET_BG_GRAY = "\u001B[48;5;240m";

    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);

        ChessBoard board = new ChessBoard();
        board.resetBoard();

        boolean whitePerspective = true;
        Set<ChessPosition> highlightedPositions = new HashSet<>();
        ChessPosition startMove = null;
        ChessPosition endMove = null;

        drawChessBoard(out, board, whitePerspective, highlightedPositions, startMove, endMove);
    }

    public static void drawChessBoard(PrintStream out, ChessBoard board, boolean whitePerspective,
                                      Set<ChessPosition> highlightedPositions, ChessPosition startMove, ChessPosition endMove) {
        out.print(SET_BG_GRAY);

        printColumnHeaders(out, whitePerspective);

        // iterate over rows 0..7
        for (int row = 0; row < BOARD_SIZE; row++) {
            int actualRow = whitePerspective ? (BOARD_SIZE - 1 - row) : row;
            int rankLabel = whitePerspective ? (BOARD_SIZE - row) : (row + 1);

            out.printf("%s%2d ", SET_BG_GRAY, rankLabel);

            // alternating black/white squares
            for (int col = 0; col < BOARD_SIZE; col++) {
                int actualCol = whitePerspective ? col : (BOARD_SIZE - 1 - col);
                // determine if (row + col) is even or odd -> use bool and mod to alternate -> set background color
                boolean isLightSquare = ((actualCol + actualRow) % 2 == 0);
                // if even -> light square color
                if (isLightSquare) {
                    out.print(SET_BG_COLOR_BEIGE);
                }
                // if odd -> dark square color
                else {
                    out.print(SET_BG_COLOR_DARK_GREEN);
                }

                ChessPosition position = new ChessPosition(actualRow + 1, actualCol + 1);

                if (position.equals(startMove)) {
                    out.print(SET_BG_COLOR_BLUE);
                }

                if (position.equals(endMove)) {
                    out.print(SET_BG_COLOR_BLUE);
                }

                if (highlightedPositions != null && highlightedPositions.contains(position)) {
                    out.print(SET_BG_COLOR_BLUE);
                }

                // print the board[row][col] with that background color
                ChessPiece piece = board.getPiece(new ChessPosition(actualRow + 1, actualCol + 1));
                if (piece != null) {
                    if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                        out.print(SET_TEXT_COLOR_WHITE);
                    } else {
                        out.print(SET_TEXT_COLOR_BLACK);
                    }
                }

                String pieceSymbol = (piece != null) ? getPieceSymbol(piece) : EMPTY;
                out.print(" " + pieceSymbol + " " + RESET);
            }
            out.printf("%s %2d\n", SET_BG_GRAY, rankLabel);
        }
        printColumnHeaders(out, whitePerspective);

        out.print(RESET);
        System.out.println();
    }

    private static void printColumnHeaders(PrintStream out, boolean whitePerspective) {
        out.print(SET_BG_GRAY);
        if (whitePerspective) {
            out.println(EMPTY + "  "  + "a" + EMPTY + " " + "b" + EMPTY + " " +  "c" + EMPTY + " " + "d" + EMPTY + " "
                    + "e" + EMPTY + " " + "f" + EMPTY + " " + "g" + EMPTY + " "  + "h" + EMPTY + " " );
        } else {
            out.println(EMPTY + "  "  + "h" + EMPTY + " " + "g" + EMPTY + " " +  "f" + EMPTY + " " + "e" + EMPTY + " "
                    + "d" + EMPTY + " " + "c" + EMPTY + " " + "b" + EMPTY + " "  + "a" + EMPTY + " " );
        }
    }

    private static String getPieceSymbol(ChessPiece piece) {
        if (piece == null) {
            return " ";
        }

        boolean isWhite = (piece.getTeamColor() == ChessGame.TeamColor.WHITE);
        return switch (piece.getPieceType()) {
            case KING -> isWhite ? WHITE_KING : BLACK_KING;
            case QUEEN -> isWhite ? WHITE_QUEEN : BLACK_QUEEN;
            case ROOK -> isWhite ? WHITE_ROOK : BLACK_ROOK;
            case BISHOP -> isWhite ? WHITE_BISHOP : BLACK_BISHOP;
            case KNIGHT -> isWhite ? WHITE_KNIGHT : BLACK_KNIGHT;
            case PAWN -> isWhite ? WHITE_PAWN : BLACK_PAWN;
        };
    }
}