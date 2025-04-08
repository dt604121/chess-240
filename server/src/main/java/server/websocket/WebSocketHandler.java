package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.*;
import dataaccess.dao.*;
import exception.*;
import model.GameData;
import websocket.messages.*;
import model.AuthData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import websocket.commands.*;
import websocket.messages.Error;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebSocket
public class WebSocketHandler {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    private final ConnectionManager connections = new ConnectionManager();
    private static final Gson gson = new Gson();

    private final Map<Integer, Session> gameSessions = new HashMap<>();

    public WebSocketHandler(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        try {
            JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
            String typeString = jsonObject.get("commandType").getAsString();
            UserGameCommand.CommandType type = UserGameCommand.CommandType.valueOf(typeString);
            UserGameCommand command;

            switch (type) {
                case CONNECT -> command = gson.fromJson(message, Connect.class);
                case MAKE_MOVE -> command = gson.fromJson(message, MakeMove.class);
                case LEAVE -> command = gson.fromJson(message, Leave.class);
                case RESIGN -> command = gson.fromJson(message, Resign.class);
                default -> throw new IllegalArgumentException("Unknown command type: " + type);
            }

            String username = getUsername(command.getAuthToken());

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, (Connect) command);
                case MAKE_MOVE -> makeMove(session, username, (MakeMove) command);
                case LEAVE -> leaveGame(session, username, (Leave) command);
                case RESIGN -> resign(session, username, (Resign) command);
            }

        } catch (UnauthorizedException ex) {
            connections.sendsMessage(session, new websocket.messages.Error("Error: Unauthorized"));
        } catch (Exception ex) {
            ex.printStackTrace();
            connections.sendsMessage(session, new websocket.messages.Error("Error " + ex.getMessage()));
        }
    }

    private void connect(Session session, String username, Connect command) throws IOException, DataAccessException {
        int gameId = command.getGameID();
        connections.add(gameId, username, session);

        GameData gameData = gameDAO.getGame(gameId);

        if (gameData == null) {
            Error errorMessage = new Error("Error: invalid gameId");
            connections.sendsMessage(session, errorMessage);
            return;
        }

        var message = String.format("%s has joined the chess game", username);
        ServerMessage notification = new Notification(message);
        connections.broadcast(gameId, notification, username);

        LoadGame loadGameMessage = new LoadGame(gameData.game());
        connections.sendsMessage(session, loadGameMessage);
    }

    private void leaveGame(Session session, String username, Leave command) throws IOException, DataAccessException {
        int gameId = command.getGameID();
        GameData gameData = gameDAO.getGame(gameId);

        if (gameData == null) {
            Error errorMessage = new Error("Error: invalid gameId");
            connections.sendsMessage(session, errorMessage);
        }

        // TODO: check if user is in the game?

        connections.remove(gameId, username);
        var message = String.format("%s left the chess game", username);
        Notification notification = new Notification(message);
        connections.broadcast(gameId, notification, username);
    }

    private void resign(Session session, String username, Resign command) throws IOException, DataAccessException {
        int gameId = command.getGameID();
        GameData gameData = gameDAO.getGame(gameId);

        if (gameData == null) {
            Error errorMessage = new Error("Error: invalid gameId");
            connections.sendsMessage(session, errorMessage);
        }

        connections.remove(gameId, username);
        var message = String.format("%s resigned from the chess game", username);
        Notification notification = new Notification(message);
        connections.broadcast(gameId, notification, username);
    }

    public void makeMove(Session session, String username, MakeMove command) throws ResponseException, IOException {
        try {
            int gameId = command.getGameID();
            GameData gameData = gameDAO.getGame(gameId);
            if (gameData == null) {
                Error errorMessage = new Error("Error: invalid gameId");
                connections.sendsMessage(session, errorMessage);
                return;
            }

            String whiteUsername = gameData.whiteUsername();
            String blackUsername = gameData.blackUsername();

            ChessGame.TeamColor playerColor;
            if (username.equals(whiteUsername)) {
                playerColor = ChessGame.TeamColor.WHITE;
            } else if (username.equals(blackUsername)) {
                playerColor = ChessGame.TeamColor.BLACK;
            } else {
                // Observer trying to move
                connections.sendsMessage(session, new Error("Error: Only players can make moves"));
                return;
            }

            ChessGame game = gameData.game();
            ChessMove move = command.getMove();

            if (move == null) {
                Error errorMessage = new Error("Error: invalid move");
                connections.sendsMessage(session, errorMessage);
                return;
            }

            if (playerColor != game.getTeamTurn()) {
                connections.sendsMessage(session, new Error("Error: Not your turn"));
                return;
            }

            game.movePiece(move);

            // Check if the game is already over
            if (game.isInCheckmate(ChessGame.TeamColor.WHITE) || game.isInCheckmate(ChessGame.TeamColor.BLACK) ||
                    game.isInStalemate(ChessGame.TeamColor.WHITE) || game.isInStalemate(ChessGame.TeamColor.BLACK)) {

                connections.sendsMessage(session, new Error("Error: Game is already over"));
                return;
            }

            ChessGame.TeamColor opponentColor = game.getTeamTurn() == ChessGame.TeamColor.WHITE ? ChessGame.TeamColor.BLACK :
                    ChessGame.TeamColor.WHITE;

            boolean isInCheck = game.isInCheck(opponentColor);
            boolean isInCheckmate = game.isInCheckmate(opponentColor);

            if (isInCheckmate) {
                Notification checkmateNotification = new Notification(String.format("Player %s is in checkmate!", opponentColor));
                connections.broadcast(gameId, checkmateNotification, null);
            }

            else if (isInCheck) {
                Notification checkNotification = new Notification(String.format("Player %s is in check!", opponentColor));
                connections.broadcast(gameId, checkNotification, null);
            }

            var message = String.format("%s moved from here %s", username, move);
            Notification notification = new Notification(message);
            connections.broadcast(gameId, notification, username);

            LoadGame loadGameMessage = new LoadGame(gameData.game());
            connections.broadcast(gameId, loadGameMessage, null);

        } catch (Exception ex) {
            connections.sendsMessage(session, new Error("Error: " + ex.getMessage()));
        }
    }

    private String getUsername(String authToken) throws DataAccessException, UnauthorizedException {
        AuthData authData = authDAO.getAuthToken(authToken);
        if (authData == null) {
            throw new UnauthorizedException("Invalid auth token");
        }
        return authData.username();
    }
}