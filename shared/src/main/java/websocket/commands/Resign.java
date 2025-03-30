package websocket.commands;

import com.google.gson.Gson;

public class Resign extends UserGameCommand {
    public Resign(CommandType commandType, String authToken, Integer gameID) {
        super(commandType, authToken, gameID);
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}
