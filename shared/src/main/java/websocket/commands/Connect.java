package websocket.commands;

import com.google.gson.Gson;

public class Connect extends UserGameCommand {
    public Connect(CommandType commandType, String authToken, Integer gameID) {
        super(commandType, authToken, gameID);
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}
