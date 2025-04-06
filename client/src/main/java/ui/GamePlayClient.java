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
            initializeGame(authToken, gameId, color, gameData);
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

            ChessBoard board = gameData.game().getBoard();
            ChessPiece pieceToMove = board.getPiece(start);

            if (pieceToMove == null) {
                throw new ResponseException("No piece to move found.");
            }

            if (pieceToMove.getTeamColor() != gameData.game().getTeamTurn()) {
                throw new ResponseException("Only pieces on your team can be moved.");
            }

            Collection<ChessMove> validMoves = gameData.game().validMoves(start);
            boolean isValidMove = validMoves.stream().anyMatch(move -> move.getEndPosition().equals(end));

            if (!isValidMove) {
                throw new ResponseException("That move is not valid.");
            }

            ChessMove move = new ChessMove(start, end, null);
            gameData.game().movePiece(move);

            TeamColor opponentColor = gameData.game().getTeamTurn() == TeamColor.WHITE ? TeamColor.BLACK :
                    TeamColor.WHITE;
            boolean isInCheck = gameData.game().isInCheck(opponentColor);
            boolean isInCheckmate = gameData.game().isInCheckmate(opponentColor);

            ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);

            ws = new WebSocketFacade(serverUrl, notificationHandler, serverMessage);

            if (isInCheck) {
                String checkNotification = String.format("Player %s is in check!", opponentColor);
                ws.makeMove(checkNotification, gameId, move);
            }

            if (isInCheckmate) {
                String checkmateNotification = String.format("Player %s is in checkmate!", opponentColor);
                ws.makeMove(checkmateNotification, gameId, move);
            }

            boolean whitePerspective = (color == null || color == TeamColor.WHITE);
            ChessBoardUI.drawChessBoard(System.out, board, whitePerspective, null);

            return String.format("You moved your piece from %s to %s.", startPosition, endPosition);
        } catch (Exception e) {
            throw new ResponseException(e.getMessage());
        }
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
            ChessBoard board = gameData.game().getBoard();

            if (board == null) {
                throw new ResponseException("Board data is null.");
            }

            boolean whitePerspective = (color == null || color == TeamColor.WHITE);
            ChessBoardUI.drawChessBoard(System.out, board, whitePerspective, null);

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
            int col = positionString.charAt(0) - 'a' + 1;
            ChessPosition position = new ChessPosition(row, col);

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
