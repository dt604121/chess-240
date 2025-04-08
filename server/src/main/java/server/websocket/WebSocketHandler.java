package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.*;
import dataaccess.dao.*;
import exception.*;
import model.AuthData;
import model.CreateGameRequest;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import websocket.commands.*;
import websocket.messages.*;

import java.io.IOException;
import java.lang.Error;
import java.lang.reflect.Type;
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
        var message = String.format("%s has joined the chess game", username);
        ServerMessage notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(gameId, notification, username);

        if (!(gameData == null)) {
            LoadGame loadGameMessage = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME);
            loadGameMessage.setGame(gameData);
            connections.sendsMessage(session, loadGameMessage);
        }
        Error errorMessage = new Error("Error: Invalid game ID");
        connections.sendsMessage(session, errorMessage);

    }

    private void leaveGame(Session session, String username, Leave command) throws IOException, DataAccessException {
        int gameId = command.getGameID();
        GameData gameData = gameDAO.getGame(gameId);

        if (gameData == null) {
            Error errorMessage = new Error("Error: Invalid game ID");
            connections.sendsMessage(session, errorMessage);
        }

        // TODO: check if user is in the game?

        connections.remove(gameId, username);
        var message = String.format("%s left the chess game", username);
        ServerMessage notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(gameId, notification, username);
    }

    private void resign(Session session, String username, Resign command) throws IOException, DataAccessException {
        int gameId = command.getGameID();
        GameData gameData = gameDAO.getGame(gameId);

        if (gameData == null) {
            Error errorMessage = new Error("Error: Invalid game ID");
            connections.sendsMessage(session, errorMessage);
        }

        connections.remove(gameId, username);
        var message = String.format("%s resigned from the chess game", username);
        ServerMessage notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(gameId, notification, username);
    }

    public void makeMove(Session session, String username, MakeMove command) throws ResponseException, IOException {
        try {
            int gameId = command.getGameID();
            GameData gameData = gameDAO.getGame(gameId);

            if (gameData == null) {
                Error errorMessage = new Error("Error: Invalid game ID");
                connections.sendsMessage(session, errorMessage);
            }

            // TODO: Validate if it's the correct player's turn + moves?

            ChessMove move = command.getMove();

            var message = String.format("%s moved from here %s", username, move);
            ServerMessage notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
            connections.broadcast(gameId, notification, username);

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

//    private void saveSession(int gameId, Session session) {
//        gameSessions.put(gameId, session);
//    }
}