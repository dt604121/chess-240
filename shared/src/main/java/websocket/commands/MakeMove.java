package websocket.commands;

import com.google.gson.Gson;

public class MakeMove extends UserGameCommand {
    public MakeMove(CommandType commandType, String authToken, Integer gameID) {
        super(commandType, authToken, gameID);
        // "move": { "start": { "row": 3, "col": 3 }, "end": { "row": 5, "col": 5 } }
    }
    public String toString() {
        return new Gson().toJson(this);
    }
}
