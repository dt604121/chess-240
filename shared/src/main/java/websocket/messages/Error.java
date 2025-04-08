package websocket.messages;

import com.google.gson.Gson;

public class Error extends ServerMessage {
    private String errorMessage = "";

    public Error(String type) {
        super(ServerMessageType.ERROR);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
