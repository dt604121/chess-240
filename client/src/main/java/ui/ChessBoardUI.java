package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import static ui.EscapeSequences.*;

public class ChessBoardUI {
    private static final int BOARD_SIZE = 8;

    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        String[][] board = initializeBoard();
        drawChessBoard(out, board, true);

        // black perspective:
        // drawChessBoard(out, board, false);
    }

    private static void drawChessBoard(PrintStream out, String[][] board, boolean whitePerspective) {
        // initialize the board set up to be 2d

        // print column header:   "   a   b   c   d   e   f   g   h"

        // for loop to repeat the rows so we get the board (col in 0..7:)
            // print rank number (row+1 or 8-row)
            // for col in 0..7 loop for a row of alternating black/white squares
                // determine if (row + col) is even or odd -> use bool and mod to alternate -> set background color
                    // if even -> light square color
                    // if odd -> dark square color
                // print the board[row][col] with that background color
        // print rank number again

        // print column header:   "   a   b   c   d   e   f   g   h"
    }

    // TODO: a way to toggle which way (perspective) the board is being drawn as (white vs. black)
    private static void flipBoardPerspectiveToBlack() {
        // reverse the loop but keep the implementation the same
    }

    // set board to be emtpy
    private static String[][] initializeBoard() {
        String[][] board = new String[BOARD_SIZE][BOARD_SIZE];

        // for row 0-7
            // for col 0-7
                // board[row][col] = empty

        // assign the pieces to the board (or empty)
        // black Rook a8 Knight b8 Bishop c8 Queen d8 King e8 Bishop f8 Knight g8 Rook h8
        board[0][0] = BLACK_ROOK;
        board[0][1] = BLACK_KNIGHT;
        board[0][2] = BLACK_BISHOP;
        board[0][3] = BLACK_QUEEN;
        board[0][4] = BLACK_KING;
        board[0][5] = BLACK_BISHOP;
        board[0][6] = BLACK_KNIGHT;
        board[0][7] = BLACK_ROOK;

        // black Pawns a8-h8
        // for col in 0..7:
            // board[1][col] = BLACK_PAWN

        // white pawns a2-h2
        // for col in 0..7:
            // board[6][col] = WHITE_PAWN

        // white Rook a1 Knight b1 Bishop c1 Queen d1 King e1 Bishop f1 Knight g1 Rook h1
        board[7][0] = WHITE_ROOK;
        board[7][1] = WHITE_KNIGHT;
        board[7][2] = WHITE_BISHOP;
        board[7][3] = WHITE_QUEEN;
        board[7][4] = WHITE_KING;
        board[7][5] = WHITE_BISHOP;
        board[7][6] = WHITE_KNIGHT;
        board[7][7] = WHITE_ROOK;
        return board;
    }
}
