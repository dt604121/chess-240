package websocket.commands;

import chess.ChessMove;
import com.google.gson.Gson;

public class MakeMove extends UserGameCommand {
    private final ChessMove move;

    public MakeMove(String authToken, Integer gameID, ChessMove move) {
        super(CommandType.MAKE_MOVE, authToken, gameID);
        this.move = move;
    }

    public ChessMove getMove() {
        return move;
    }

    @Override
    public CommandType getCommandType(){
        return CommandType.MAKE_MOVE;
    }

    // Used to request to make a move in a game.
    public String toString() {
        return new Gson().toJson(this);
    }

}
