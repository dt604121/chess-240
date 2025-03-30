package ui;
import chess.ChessBoard;
import exception.ResponseException;
import ui.websocket.NotificationHandler;
import ui.websocket.WebSocketFacade;

import java.util.Arrays;
import java.util.Objects;

public class GamePlayClient {
    private final ServerFacade serverFacade;
    private final String serverUrl;
    private WebSocketFacade ws;
    private final NotificationHandler notificationHandler;

    public GamePlayClient(String serverUrl, NotificationHandler notificationHandler){
        serverFacade = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.notificationHandler = notificationHandler;

    }

    public String eval(String input) throws ResponseException {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0 ) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "move" -> movePiece(params);
                case "redraw" -> redrawBoard();
                case "leave" -> leaveGame();
                case "resign" -> resignGame();
                case "highlight" -> highlightMoves(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception ex) {
            throw new ResponseException(ex.getMessage());
        }
    }

    public String movePiece(String... params) throws ResponseException{
        try {
            if (params.length != 1) {
                throw new ResponseException("Expected: <piece>");
            }

            var piece = params[0].trim();

            if (piece.isEmpty()) {
                throw new ResponseException("Invalid piece. Cannot be empty.")
            }

            // TODO: update / print board (updated on each player’s screen)
            boolean whitePerspective = Objects.equals(color, "WHITE");

            ChessBoard board = new ChessBoard();
            board.resetBoard();
            ChessBoardUI.drawChessBoard(System.out, board, whitePerspective);
            // TODO: notification -> player’s name moved piece here.
            // TODO: notification -> player name is in check
            // TODO: notification -> player name is in checkmate

            return String.format("You moved the %s piece from here to here.", piece);
        } catch (Exception e) {
            throw new ResponseException(e.getMessage());
        }
    }

    public String redrawBoard() throws ResponseException {
        try {
            // TODO: how to grab actual board using the color...
            boolean whitePerspective = Objects.equals(color, "WHITE");
            ChessBoard board = gameData.game().getBoard();
            board.resetBoard();
            ChessBoardUI.drawChessBoard(System.out, board, whitePerspective);
            return "";
        } catch (Exception e) {
            throw new ResponseException(e.getMessage());
        }
    }

    private String leaveGame() throws ResponseException {
        try {
            Repl.state = State.SIGNEDOUT;

            // TODO: notification ->  player’s name left the game
            return String.format("You have left the game: %s");
        } catch (Exception e) {
            throw new ResponseException(e.getMessage());
        }
    }

    private String resignGame() throws ResponseException {
        try {
            return String.format("You have been resigned from the %s game");
            // TODO: notification -> player’s name has resigned.
        } catch (Exception e) {
            throw new ResponseException(e.getMessage());
        }
    }

    private String highlightMoves(String... params) throws ResponseException {
        try {
            if (params.length != 1) {
                throw new ResponseException("Expected: <piece>");
            }
            // redraw the board with the highlighted moves
            boolean whitePerspective = Objects.equals(color, "WHITE");

            ChessBoard board = new ChessBoard();
            board.resetBoard();
            ChessBoardUI.drawChessBoard(System.out, board, whitePerspective);
            return "";
        } catch (Exception e) {
            throw new ResponseException("" + e.getMessage());
        }
    }

    public String help() {
        return """
                move <PIECE> - a piece
                redraw - chess board
                leave - game
                resign - from the game
                highlight <PIECE> - legal moves
                quit - playing chess
                help - with possible commands
                """;
    }
}
