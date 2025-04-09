package ui;
import chess.*;
import exception.ResponseException;
import ui.websocket.*;
import websocket.commands.Connect;
import chess.ChessGame.TeamColor;
import websocket.messages.ServerMessage;

import java.util.*;

public class GamePlayClient {
    private final ServerFacade serverFacade;
    private final String serverUrl;
    private TeamColor color;
    private int gameId;
    private String authToken;
    private ChessGame game;
    private Connect.PlayerType playerType;
    private WebSocketFacade ws;
    private NotificationHandler notificationHandler;
    String username = Repl.currentUsername;

    public GamePlayClient(ServerFacade serverFacade, String serverUrl){
        this.serverFacade = serverFacade;
        this.serverUrl = serverUrl;
    }

    public void initializeGame(String authToken, int gameId, TeamColor color,
                               Connect.PlayerType playerType) {
        this.authToken = authToken;
        this.gameId = gameId;
        this.color = color;
        this.playerType = playerType;
    }

    public String eval(String input) throws ResponseException {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0 ) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            initializeGame(authToken, gameId, color, playerType);

            if (playerType == Connect.PlayerType.PLAYER) {
                ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
                ws = new WebSocketFacade(serverUrl, notificationHandler, serverMessage);
                ws.enterChess(authToken, this.gameId, playerType);
            }

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
            if (params.length != 2) {
                throw new ResponseException("Expected: <piece>");
            }

            var startPosition = params[0].trim();
            var endPosition = params[1].trim();

            if (startPosition.isEmpty() || endPosition.isEmpty()) {
                throw new ResponseException("Invalid position(s). Cannot be left empty.");
            }

            ChessPosition start = positionConversion(startPosition);
            ChessPosition end = positionConversion(endPosition);

            ChessBoard board = game.getBoard();
            ChessPiece pieceToMove = board.getPiece(start);

            if (pieceToMove == null) {
                throw new ResponseException("No piece to move found.");
            }

            if (pieceToMove.getTeamColor() != game.getTeamTurn()) {
                throw new ResponseException("Only pieces on your team can be moved.");
            }

            Collection<ChessMove> validMoves = game.validMoves(start);
            boolean isValidMove = validMoves.stream().anyMatch(move -> move.getEndPosition().equals(end));

            if (!isValidMove) {
                throw new ResponseException("That move is not valid.");
            }

            ChessMove move = new ChessMove(start, end, null);
            ws.makeMove(authToken, gameId, move);
        } catch (Exception e) {
            throw new ResponseException(e.getMessage());
        }
        return "";
    }

    public ChessPosition positionConversion(String position) throws ResponseException {
        char colChar = position.charAt(0);
        char rowChar = position.charAt(1);

        int col = colChar - 'a' + 1;
        int row = Character.getNumericValue(rowChar);

        if (col < 1 || col > 8 || row < 1 || row > 8) {
            throw new ResponseException("Out of bounds.");
        }

        return new ChessPosition(row, col);
    }

    public String redrawBoard() throws ResponseException {
        try {
            ChessBoard board = game.getBoard();

            if (board == null) {
                throw new ResponseException("Board data is null.");
            }

            boolean whitePerspective = playerType == Connect.PlayerType.OBSERVER || color == ChessGame.TeamColor.WHITE;

            ChessBoardUI.drawChessBoard(System.out, board, whitePerspective, null, null,
                    null);

            return "Board has been redrawn";

        } catch (Exception e) {
            throw new ResponseException("Failed to redraw the board: " + e.getMessage());
        }
    }

    private String leaveGame() throws ResponseException {
        try {
            Repl.state = State.SIGNEDOUT;

            ws.leaveChess(authToken, gameId);

            return String.format("%s has left the game. Come back soon!", username);
        } catch (Exception e) {
            throw new ResponseException(e.getMessage());
        }
    }

    private String resignGame() throws ResponseException {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Are you sure that you want to resign? ");
            String answer = scanner.nextLine();

            if (answer.equalsIgnoreCase("yes")) {
                ws.resignFromChess(authToken, gameId);
                Repl.state = State.SIGNEDIN;
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

            String positionString = params[0].trim();

            if (positionString.isEmpty()) {
                throw new ResponseException("Invalid position. Cannot be left empty.");
            }

            ChessPosition position = positionConversion(positionString);

            ChessBoard board = game.getBoard();
            ChessPiece piece = board.getPiece(position);

            if (piece == null) {
                return "No piece found at that location.";
            }

            if (piece.getTeamColor() != game.getTeamTurn()) {
                return "Only pieces on your team can be highlighted.";
            }

            Collection<ChessMove> validMoves = game.validMoves(position);

            if (validMoves == null || validMoves.isEmpty()) {
                return "No valid moves for this piece.";
            }

            Set<ChessPosition> highlightPositions = new HashSet<>();
            for (ChessMove move : validMoves) {
                highlightPositions.add(move.getEndPosition());
            }

            boolean whitePerspective = (color == null || color == TeamColor.WHITE);

            ChessBoardUI.drawChessBoard(System.out, board,
                    whitePerspective, highlightPositions,
                    position, null);

            return "Highlighted legal moves for selected piece";
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

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
