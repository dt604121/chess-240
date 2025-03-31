package websocket.messages;

import com.google.gson.Gson;

public class Notification extends ServerMessage {
    public Notification(ServerMessageType type) {
        super(type);
    }

    // This is a message meant to inform a player when another player made an action.

    public String toString() {
        return new Gson().toJson(this);
    }
}
