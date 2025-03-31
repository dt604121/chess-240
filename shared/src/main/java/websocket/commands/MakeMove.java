package websocket.commands;

import com.google.gson.Gson;

public class MakeMove extends UserGameCommand {
    public MakeMove(CommandType commandType, String authToken, Integer gameID) {
        super(commandType, authToken, gameID);
        // supports the additional move field when serializing the MAKE_MOVE command over the WebSocket. This must result in something like the following:
        // "move": { "start": { "row": 3, "col": 3 }, "end": { "row": 5, "col": 5 } }
    }

    // Used to request to make a move in a game.
    public String toString() {
        return new Gson().toJson(this);
    }
}
