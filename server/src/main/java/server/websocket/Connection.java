package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    public String playerName;
    public int gameId;
    public Session session;

    // TODO: make it a set of sessions!
    public Connection(int gameId, String playerName, Session session) {
        this.gameId = gameId;
        this.playerName = playerName;
        this.session = session;
    }

    public void send(String msg) throws IOException {
        if (session.isOpen()) {
            session.getRemote().sendString(msg);
        }
    }
}