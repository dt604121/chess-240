package ui;
import chess.*;
import dataaccess.dao.AuthDAO;
import exception.ResponseException;
import model.GameData;
import ui.websocket.*;
import websocket.commands.Connect;
import chess.ChessGame.TeamColor;
import websocket.messages.ServerMessage;

import java.util.*;

public class GamePlayClient implements NotificationHandler{
    private final ServerFacade serverFacade;
    private final String serverUrl;
    private TeamColor color;
    private int gameId;
    private String authToken;
    private ChessGame game;
    private Connect.PlayerType playerType;
    private WebSocketFacade ws;
    private GameData gameData;
    private NotificationHandler notificationHandler;
    private AuthDAO authDAO;
    boolean isObserver;

    public GamePlayClient(ServerFacade serverFacade, String serverUrl, NotificationHandler notificationHandler){
        this.serverFacade = serverFacade;
        this.serverUrl = serverUrl;
        this.notificationHandler = notificationHandler;
    }

    public void initializeGame(String authToken, int gameId, TeamColor color, GameData gameData,
                               Connect.PlayerType playerType) {
        this.authToken = authToken;
        this.gameId = gameId;
        this.color = color;
        this.playerType = playerType;
        this.isObserver = playerType == Connect.PlayerType.OBSERVER;
        this.gameData = gameData;
        this.game = gameData.game();
    }

    public void initializeWebsocket() throws ResponseException {
        ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        if (ws == null) {
            ws = new WebSocketFacade(serverUrl, this, serverMessage);
            ws.enterChess(authToken, this.gameId, playerType);
        }
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
            if (isObserver) {
                throw new ResponseException("Observers cannot make moves.");
            }
            ChessPiece promotionPiece = null;

            if (params.length != 2 && params.length != 3) {
                throw new ResponseException("Expected: <start> <end> [promotion]");
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

            if (params.length == 3) {
                String promotionPieceString = params[2].trim().toUpperCase();
                ChessPiece.PieceType type = ChessPiece.PieceType.valueOf(promotionPieceString);
                promotionPiece = new ChessPiece(pieceToMove.getTeamColor(), type);
            }

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

            ChessMove move = new ChessMove(start, end, promotionPiece != null ? promotionPiece.getPieceType() : null);
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
            Repl.state = State.SIGNEDIN;

            ws.leaveChess(authToken, gameId);

            return String.format("%s has left the game. Come back soon!", TeamColor.WHITE ==
                    color ? gameData.whiteUsername() : gameData.blackUsername());
        } catch (Exception e) {
            throw new ResponseException(e.getMessage());
        }
    }

    private String resignGame() throws ResponseException {
        try {
            if (isObserver) {
                throw new ResponseException("Observers cannot make moves.");
            }
            Scanner scanner = new Scanner(System.in);
            System.out.print("Are you sure that you want to resign? ");
            String answer = scanner.nextLine();

            if (answer.equalsIgnoreCase("yes")) {
                ws.resignFromChess(authToken, gameId);
                Repl.state = State.SIGNEDIN;
                return String.format("%s has forfeited and has resigned from the game.", TeamColor.WHITE ==
                        color ? gameData.whiteUsername() : gameData.blackUsername());
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
            ChessGame game = gameData.game();

            if (piece == null) {
                return "No piece found at that location.";
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
                move <START POSITION> <END POSITION> <OPTIONAL PROMOTION PIECE> - a piece
                redraw - chess board
                leave - game
                resign - from the game
                highlight <POSITION> - legal moves
                quit - playing chess
                help - with possible commands
                """;
    }

//    public void setAuthToken(String authToken) {
//        this.authToken = authToken;
//    }

    @Override
    public void loadGame(ChessGame game) {
        try {
            this.game = game;
            System.out.println();
            redrawBoard();
        } catch (exception.ResponseException e) {
            System.err.println("Failed to redraw board: " + e.getMessage());
        }
    }
}
