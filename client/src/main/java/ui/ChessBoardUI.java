package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import static ui.EscapeSequences.*;

public class ChessBoardUI {
    private static final int BOARD_SIZE_IN_SQUARES = 8;

    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        drawChessBoard(out);
    }

    private static void drawChessBoard(PrintStream out) {
        // initialize the board set up to be 2d

        // for loop to repeat the rows so we get the board
            // for loop for a row of alternating black/white squares
            // use bool and mod to alternate -> set background color


        // assign the pieces to the board (or empty)
        // black Rook a8 Knight b8 Bishop c8 Queen d8 King e8 Bishop f8 Knight g8 Rook h8
        // black Pawns a8-h8

        // white pawns a2-h2
        // white Rook a1 Knight b1 Bishop c1 Queen d1 King e1 Bishop f1 Knight g1 Rook h1

        // function to handle printing the headers a-h (above and below the board)
        // as well as the sides with numbers 1-8 on the left and right

    }

    // TODO: a way to toggle which way (perspective) the board is being drawn as (white vs. black)
    private static void flipBoardPerspectiveToBlack() {
        // reverse the loop but keep the implementation the same
    }

    // set board to be emtpy
    private static String[][] initializeBoard() {

    }
}
