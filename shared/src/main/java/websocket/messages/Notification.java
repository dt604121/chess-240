package websocket.messages;

import com.google.gson.Gson;

public class Notification extends ServerMessage {

    private final ServerMessageType type;
    private final String message;

    public Notification (ServerMessageType type, String message) {
        super(ServerMessageType.NOTIFICATION);
        this.type = type;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public ServerMessageType getType() {
        return type;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
