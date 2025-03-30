package websocket.messages;

import com.google.gson.Gson;

// TODO: compare with petshop..

public class Notification extends ServerMessage {
    public Notification(ServerMessageType type) {
        super(type);
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}
