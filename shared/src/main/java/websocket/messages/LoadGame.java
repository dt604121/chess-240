package websocket.messages;

import com.google.gson.Gson;

public class LoadGame extends ServerMessage {
    public LoadGame(ServerMessageType type) {
        super(type);
    }
    public String toString() {
        return new Gson().toJson(this);
    }
}
