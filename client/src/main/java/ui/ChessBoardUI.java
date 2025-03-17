package ui;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import static ui.EscapeSequences.*;

public class ChessBoardUI {
    private static final int BOARD_SIZE = 8;

    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        boolean whitePerspective = true;
        drawChessBoard(out, board, whitePerspective);
    }

    public static void drawChessBoard(PrintStream out, ChessBoard board, boolean whitePerspective) {
        if (whitePerspective) {
            out.println("    a   b   c   d   e   f   g   h");
        } else {
            out.println("    h   g   f   e   d   c   b   a");
        }

        // for loop to repeat the rows so we get the board (col in 0..7:)
        for (int row = 0; row < BOARD_SIZE; row++) {
            int actualRow = whitePerspective ? (BOARD_SIZE - 1 - row) : row;
            // print rank number (row+1 or 8-row)
            int rankLabel = whitePerspective ? (row + 1) : (BOARD_SIZE - row);
            out.print(rankLabel + "  ");
            // for col in 0..7 loop for a row of alternating black/white squares
            for (int col = 0; col < BOARD_SIZE; col++) {
                int actualCol = whitePerspective ? col : (BOARD_SIZE - 1 - col);
                // determine if (row + col) is even or odd -> use bool and mod to alternate -> set background color
                boolean isLightSquare = ((actualCol + actualRow) % 2 == 0);
                // if even -> light square color
                if (isLightSquare) {
                    out.print(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_BLACK);
                }
                // if odd -> dark square color
                else {
                    out.print(SET_BG_COLOR_BLACK + SET_TEXT_COLOR_WHITE);
                }
                // print the board[row][col] with that background color
                ChessPiece piece = board.getPiece(new ChessPosition(actualRow + 1, actualCol + 1));
                String pieceSymbol = (piece != null) ? piece.toString() : " ";
                out.print(" " + pieceSymbol + " ");
                out.print(RESET);

                // print rank number again
                out.print(" " + rankLabel);
                out.println();

                // print column header (below)
                if (whitePerspective) {
                    out.println("    a   b   c   d   e   f   g   h");
                } else {
                    out.println("    h   g   f   e   d   c   b   a");
                }

                out.print(RESET);
            }
        }
    }
}
