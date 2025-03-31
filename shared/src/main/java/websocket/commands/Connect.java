package websocket.commands;

import com.google.gson.Gson;

public class Connect extends UserGameCommand {
    public Connect(CommandType commandType, String authToken, Integer gameID) {
        super(commandType, authToken, gameID);
    }

    public enum Type {
        ENTER
    }

    // Used for a user to make a WebSocket connection as a player or observer.

    public String toString() {
        return new Gson().toJson(this);
    }
}
