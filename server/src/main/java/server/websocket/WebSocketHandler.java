package server.websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import dataaccess.sql.SQLUserDAO;
import exception.ResponseException;
import exception.UnauthorizedException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.*;
import websocket.messages.*;

import java.io.IOException;
import java.lang.Error;
import java.util.HashMap;
import java.util.Map;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private static final Gson gson = new Gson();

    private final Map<Integer, Session> gameSessions = new HashMap<>();

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

        var gameState = loadGame(gameId);
        sendsMessage(session, new LoadGame(gameState));
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

            var gameState = loadGame(gameId);
            sendsMessage(session, new LoadGame(gameState));
        } catch (Exception ex) {
            throw new ResponseException(ex.getMessage());
        }
    }

    // TODO: how do we get the username?
    private String getUsername(String authToken) {
        //
    }

    private void saveSession(int gameId, Session session) {
        gameSessions.put(gameId, session);
    }

    private void sendsMessage(Session session, Object message) throws IOException {
        session.getRemote().sendString(gson.toJson(message));
    }
}