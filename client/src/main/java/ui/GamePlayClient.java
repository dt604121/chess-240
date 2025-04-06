package ui;
import chess.*;
import exception.ResponseException;
import model.GameData;
import ui.websocket.*;
import websocket.messages.ServerMessage;
import chess.ChessGame.TeamColor;

import java.util.*;

public class GamePlayClient {
    private final ServerFacade serverFacade;
    private final String serverUrl;
    private WebSocketFacade ws;
    private final NotificationHandler notificationHandler;
    private TeamColor color;
    private int gameId;
    private String authToken;
    private GameData gameData;
    private String username;

    public GamePlayClient(String serverUrl, NotificationHandler notificationHandler){
        serverFacade = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.notificationHandler = notificationHandler;
    }

    public void initializeGame(String authToken, int gameId, TeamColor color, GameData gameData) {
        this.authToken = authToken;
        this.gameId = gameId;
        this.color = color;
        this.gameData = gameData;
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
            boolean whitePerspective = (color == TeamColor.WHITE);

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
            ChessBoard board = gameData.game().getBoard();

            if (board == null) {
                throw new ResponseException("Board data is null.");
            }

            boolean whitePerspective = (color == null || color == TeamColor.WHITE);
            ChessBoardUI.drawChessBoard(System.out, board, whitePerspective);

            return "Board has been redrawn";

        } catch (Exception e) {
            throw new ResponseException("Failed to redraw the board: " + e.getMessage());
        }
    }

    private String leaveGame() throws ResponseException {
        try {
            Repl.state = State.SIGNEDOUT;

            if (gameData == null) {
                return "Observer";
            }
            ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);

            ws = new WebSocketFacade(serverUrl, notificationHandler, serverMessage);
            ws.leaveChess(authToken, gameId);

            return String.format("%s has left the game. Come back soon!", TeamColor.WHITE ==
                    color ? gameData.whiteUsername() : gameData.blackUsername());
        } catch (Exception e) {
            throw new ResponseException(e.getMessage());
        }
    }

    private String resignGame() throws ResponseException {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Are you sure that you want to resign?");
            String answer = scanner.nextLine();
            String username = Repl.currentUsername;

            if (answer.equalsIgnoreCase("yes")) {
                ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
                ws = new WebSocketFacade(serverUrl, notificationHandler, serverMessage);
                ws.resignFromChess(authToken, gameId);
                return String.format("%s has forfeited and has resigned from the game.", username);
            }

            return "Ok resignation cancelled. Have a great rest of your game!";
        } catch (Exception e) {
            throw new ResponseException(e.getMessage());
        }
    }

    private String highlightMoves(String... params) throws ResponseException {
        try {
            if (params.length != 1) {
                throw new ResponseException("Expected: <position>");
            }

            String positionString = params[0].toUpperCase();
            int row = Character.getNumericValue(positionString.charAt(1));
            int col = positionString.charAt(0) - 'A' + 1;
            ChessPosition position = new ChessPosition(row, col);

            boolean whitePerspective = (color == TeamColor.WHITE);

            ChessBoard board = new ChessBoard();
            ChessPiece piece = board.getPiece(position);
            ChessGame game = gameData.game();

            if (piece == null) {
                return "No piece found.";
            }

            if (piece.getTeamColor() != game.getTeamTurn()) {
                return "Only pieces on your team can be highlighted.";
            }

            Collection<ChessMove> moves = game.validMoves(position);

            if (moves == null || moves.isEmpty()) {
                return "No valid moves for this piece.";
            }

            Set<ChessPosition> highlightPositions = new HashSet<>();
            for (ChessMove move : moves) {
                highlightPositions.add(move.getEndPosition());
            }

            ChessBoardUI.drawChessBoard(System.out, board,
                    piece.getTeamColor() == ChessGame.TeamColor.WHITE, highlightPositions);
            return "Highlighted moves";
        } catch (Exception e) {
            throw new ResponseException("Couldn't highlight the moves: " + e.getMessage());
        }
    }

    public String help() {
        return """
                move <POSITION> - a piece
                redraw - chess board
                leave - game
                resign - from the game
                highlight <POSITION> - legal moves
                quit - playing chess
                help - with possible commands
                """;
    }
}
