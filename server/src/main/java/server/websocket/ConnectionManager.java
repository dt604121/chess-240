package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.Notification;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    private final ConcurrentHashMap<Integer, ConcurrentHashMap<String, Connection>> gameConnections = new ConcurrentHashMap<>();

    public void add(String visitorName, Session session) {
        var connection = new Connection(visitorName, session);
        gameConnections.putIfAbsent(gameId, new Connection(playerName, session));
        gameConnections.get(gameId).put(playerName, new Connection(playerName, session));
    }

    public void remove(String visitorName) {
        if (gameConnections.containsKey(gameId)) {
            gameConnections.get(gameId).remove(playerName);
            if (gameConnections.get(gameId).isEmpty()) {
                gameConnections.remove(gameId); // Clean up empty games
            }
        }
    }

    public void broadcast(String excludeVisitorName, Notification notification) throws IOException {
        if (!gameConnections.containsKey(gameId)) return;

        var gameConn = gameConnections.get(gameId);
        var removeList = new ArrayList<Connection>();
        for (var c : gameConn.values()) {
            if (c.session.isOpen()) {
                if (!c.visitorName.equals(excludeVisitorName)) {
                    c.send(notification.toString());
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            gameConn.remove(c.visitorName);
        }
    }
}
