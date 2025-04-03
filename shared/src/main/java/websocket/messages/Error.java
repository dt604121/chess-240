package websocket.messages;

import com.google.gson.Gson;

public class Error extends ServerMessage {
    private final String errorMessage;

    public Error(String type) {
        super(ServerMessageType.ERROR);
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
