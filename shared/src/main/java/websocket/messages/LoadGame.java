package websocket.messages;

import com.google.gson.Gson;

public class LoadGame extends ServerMessage {
    public LoadGame(ServerMessageType type) {
        super(type);
    }
    // Used by the server to send the current game state to a client. When a client receives this message, it will redraw the chess board.
    public String toString() {
        return new Gson().toJson(this);
    }
}
