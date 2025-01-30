package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor teamTurn;
    private ChessBoard board;

    public ChessGame() {
        this.teamTurn = TeamColor.WHITE;
        this.board = new ChessBoard();

        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null){
            return null;
        }
        Collection<ChessMove> possibleMoves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> validMoves = new ArrayList<>();
        for (ChessMove possibleMove : possibleMoves){
            // iterate through each move in validMoves (move in moves)
            ChessBoard clonedBoard = board.cloneBoard();
            ChessGame game = new ChessGame();
            game.setBoard(clonedBoard);

            // simulate the move and check if it's valid
            try {
                game.makeMove(possibleMove);
                validMoves.add(possibleMove);
            }
            catch (InvalidMoveException ignored) {
            }

            return (Collection<ChessMove>) validMoves;
        }
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        // initialize the piece
        ChessPiece piece = board.getPiece(move.getStartPosition());
        TeamColor teamColor = piece.getTeamColor();
        ChessGame game = new ChessGame();
        // make move -> set the piece to have the new move? Do we need to call pieceMoves?
        game.makeMove(new ChessMove(move.getStartPosition(), move.getEndPosition(), null));
        // if isInCheck
        if (isInCheck(teamColor)){
            // if == null || it's invalid from starting position || teamTurn != teamColor
            if (piece == null || teamTurn != teamColor){
                throw InvalidMoveException;
            }
        }
        // new teams turn if teamTurn == WHITE set it now to BLACK
        if (teamTurn == TeamColor.WHITE){
            game.setTeamTurn(ChessGame.TeamColor.BLACK);
        }
        else {
            game.setTeamTurn(ChessGame.TeamColor.WHITE);
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        // iterate through the board if the pieceType == KING && getTeamColor == teamColor
        ChessPosition kingPosition = null;
        for (int row = 1; row <= 8 && row >= 1; row += 1){
            for (int col = 1; col <= 8 && col >= 1; col += 1){
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor){
                    kingPosition = new ChessPosition(row, col);
                }
            }
        }
        
        // iterate through opposing team pieceMoves
        for (int row = 1; row <= 8 && row >= 1; row += 1){
            for (int col = 1; col <= 8 && col >= 1; col += 1){
                ChessPosition enemyPosition = new ChessPosition(row, col);
                ChessPiece enemyPiece = board.getPiece(enemyPosition);
                
                // check to make sure it's an enemy piece
                if (enemyPiece != null && enemyPiece.getTeamColor() != teamColor){
                    Collection<ChessMove> enemyMoves = enemyPiece.pieceMoves(board, enemyPosition);

                    for (ChessMove enemyMove : enemyMoves){
                        // if the move == king position
                        if (enemyMove.getEndPosition().equals(kingPosition)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        // if inCheck() is true (king in check)
        if (isInCheck(teamColor)){
            // iterate through position
            ChessPosition kingPosition = null;
            for (int row = 1; row <= 8 && row >= 1; row += 1){
                for (int col = 1; col <= 8 && col >= 1; col += 1){
                    ChessPosition position = new ChessPosition(row, col);
                    ChessPiece piece = board.getPiece(position);
                    if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor){
                        kingPosition = new ChessPosition(row, col);
                    }
                }
            }
                // as long as the piece != null and matches the team color
                // if no legal moves -> will use validMoves
        }

        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        // if isCheck() is false -> king isn't in danger
        if (isInCheck() == false){
            // iterate through the moves of the teamcolor
            // check if the move is within the bounds
            for (int row = 1; row <= 8 && row >= 1; row += 1){
                for (int col = 1; col <=8 && col >= 1; col += 1){
                    ChessPosition position = new ChessPosition(row, col);
                    ChessPiece piece = board.getPiece(position);
                    Collection<ChessMove> validMoves = piece.pieceMoves(board, position);

                    if (piece != null && piece.getTeamColor() == teamColor){
                        validMoves = validMoves(position);
                        if (validMoves.isEmpty() == true){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
