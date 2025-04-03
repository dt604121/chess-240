package websocket.messages;

import com.google.gson.Gson;
import model.GameData;

public class LoadGame extends ServerMessage {
    private GameData game;

    public LoadGame(ServerMessageType type) {
        super(ServerMessageType.LOAD_GAME);
    }

    public Object getGame() {
        return game;
    }

    // Used by the server to send the current game state to a client. When a client receives this message, it will redraw the chess board.

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
