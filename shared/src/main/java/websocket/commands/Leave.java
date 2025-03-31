package websocket.commands;

import com.google.gson.Gson;

public class Leave extends UserGameCommand {
    public Leave(CommandType commandType, String authToken, Integer gameID) {
        super(commandType, authToken, gameID);
    }

    // Tells the server you are leaving the game so it will stop sending you notifications.
    public String toString() {
        return new Gson().toJson(this);
    }
}
