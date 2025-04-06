package server.websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import dataaccess.dao.*;
import exception.*;
import model.AuthData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import websocket.commands.*;
import websocket.messages.*;

import java.io.IOException;
import java.lang.Error;
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
            UserGameCommand command = gson.fromJson(message, UserGameCommand.class);
            String username = getUsername(command.getAuthToken());

            saveSession(command.getGameID(), session);

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, (Connect) command);
                case MAKE_MOVE -> makeMove(session, username, (MakeMove) command);
                case LEAVE -> leaveGame(session, username, (Leave) command);
                case RESIGN -> resign(session, username, (Resign) command);
            }
        } catch (UnauthorizedException ex) {
            sendsMessage(session, new Error("Error: Unauthorized"));
        } catch (Exception ex) {
            ex.printStackTrace();
            sendsMessage(session, new Error("Error " + ex.getMessage()));
        }
    }

    private void connect(Session session, String username, Connect command) throws IOException {
        int gameId = command.getGameID();
        connections.add(gameId, username, session);
        var message = String.format("%s has joined the chess game", username);
        var notification = new Notification(Notification.Type.ARRIVAL, message);
        connections.broadcast(gameId, notification);

        sendsMessage(session, new LoadGame(ServerMessage.ServerMessageType.NOTIFICATION));
    }

    private void leaveGame(Session session, String username, Leave command) throws IOException {
        int gameId = command.getGameID();
        connections.remove(gameId, username);
        var message = String.format("%s left the chess game", username);
        var notification = new Notification(Notification.Type.DEPARTURE, message);
        connections.broadcast(gameId, notification);
    }

    private void resign(Session session, String username, Resign command) throws IOException {
        int gameId = command.getGameID();
        connections.remove(gameId, username);
        var message = String.format("%s resigned from the chess game", username);
        var notification = new Notification(Notification.Type.RESIGN, message);
        connections.broadcast(gameId, notification);
    }

    public void makeMove(Session session, String username, MakeMove command) throws ResponseException {
        try {
            int gameId = command.getGameID();
            ChessMove move = command.getMove();

            var message = String.format("%s moved from here %s", username, move);
            var notification = new Notification(Notification.Type.MOVE, message);
            connections.broadcast(gameId, notification);

            sendsMessage(session, new LoadGame(ServerMessage.ServerMessageType.NOTIFICATION));
        } catch (Exception ex) {
            throw new ResponseException(ex.getMessage());
        }
    }

    private String getUsername(String authToken) throws DataAccessException, UnauthorizedException {
        AuthData authData = authDAO.getAuthToken(authToken);
        if (authData == null) {
            throw new UnauthorizedException("Invalid auth token");
        }
        return authData.username();
    }

    private void saveSession(int gameId, Session session) {
        gameSessions.put(gameId, session);
    }

    public void sendsMessage(Session session, Object message) throws IOException {
        session.getRemote().sendString(gson.toJson(message));
    }
}