package websocket.commands;

import com.google.gson.Gson;

public class Resign extends UserGameCommand {
    public Resign(CommandType commandType, String authToken, Integer gameID) {
        super(commandType, authToken, gameID);
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.RESIGN;
    }

    // Forfeits the match and ends the game (no more moves can be made).
    public String toString() {
        return new Gson().toJson(this);
    }
}
