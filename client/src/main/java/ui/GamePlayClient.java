package ui;
import chess.ChessBoard;
import exception.ResponseException;
import ui.websocket.NotificationHandler;
import ui.websocket.WebSocketFacade;

import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

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
                throw new ResponseException("Invalid piece. Cannot be empty.");
            }

            // TODO: update / print board (updated on each player’s screen)
            // current state of the chess board from the side the user is playing.
            // If playing white, white pieces should be drawn on bottom. If playing black,
            // black pieces should be drawn on bottom. observing -> white
            boolean whitePerspective = Objects.equals(color, "WHITE");

            ChessBoard board = new ChessBoard();
            board.resetBoard();
            ChessBoardUI.drawChessBoard(System.out, board, whitePerspective);
            // TODO: notification -> player’s name moved piece from here to here
            // TODO: notification -> player name is in check
            // TODO: notification -> player name is in checkmate
            // board automatically updates on all clients involved in the game.
            // Sends MakeMove to Server

            return String.format("You moved the %s piece from here to here.", piece);
        } catch (Exception e) {
            throw new ResponseException(e.getMessage());
        }
    }

    public String redrawBoard() throws ResponseException {
        try {
            // TODO: how to grab actual board using the color...
            // current state of the chess board from the side the user is playing.
            // If playing white, white pieces should be drawn on bottom. If playing black,
            // black pieces should be drawn on bottom. observing -> white
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
            // Removes the user from the game (check whether they are playing or observing the game).
            // The client transitions back to the Post-Login UI.
            ws = new WebSocketFacade(serverUrl, notificationHandler);
            // Sends a Leave WebSocket message to the server.
            ws.leaveChess();
            return String.format("You have left the game. Come back soon!");
        } catch (Exception e) {
            throw new ResponseException(e.getMessage());
        }
    }

    private String resignGame() throws ResponseException {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Are you sure that you want to resign?");
            String answer = scanner.nextLine();
            if (answer.equalsIgnoreCase("yes")) {
                return "You have forfeited and have resigned from the game.";
            }
            return "Ok. Have a good rest of your game!";

            // TODO: notification -> player’s name has resigned.
            // Prompts the user to confirm they want to resign.
            // If they do, the user forfeits the game and the game is over.
            // Does not cause the user to leave the game.

            // Sends a RESIGN WebSocket message to the server.
        } catch (Exception e) {
            throw new ResponseException(e.getMessage());
        }
    }

    private String highlightMoves(String... params) throws ResponseException {
        try {
            if (params.length != 1) {
                throw new ResponseException("Expected: <piece>");
            }
            // redraw the board with the highlighted moves -> grab from the piece moves calculator!!
            // current state of the chess board from the side the user is playing.
            // If playing white, white pieces should be drawn on bottom. If playing black,
            // black pieces should be drawn on bottom. observing -> white

           // ** This is a local operation and has no effect on remote users’ screens. **
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
