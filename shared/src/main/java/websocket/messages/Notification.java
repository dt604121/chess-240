package websocket.messages;

import com.google.gson.Gson;

public class Notification extends ServerMessage {
    public enum Type { ARRIVAL, DEPARTURE, MOVE, RESIGN};

    private final Type type;
    private final String message;

    public Notification (Type type, String message) {
        super(ServerMessageType.NOTIFICATION);
        this.type = type;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}
