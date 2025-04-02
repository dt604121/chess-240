package websocket.commands;

import com.google.gson.Gson;

public class Leave extends UserGameCommand {
    public Leave(CommandType commandType, String authToken, Integer gameID) {
        super(commandType, authToken, gameID);
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.LEAVE;
    }

    // Tells the server you are leaving the game so it will stop sending you notifications.
    public String toString() {
        return new Gson().toJson(this);
    }
}
