package websocket.commands;

import com.google.gson.Gson;

public class Connect extends UserGameCommand {
    // Used for a user to make a WebSocket connection as a player or observer.
    public enum PlayerType {
        PLAYER, OBSERVER
    }

    private final PlayerType playerType;

    public Connect(String authToken, Integer gameID, PlayerType playerType) {
        super(CommandType.CONNECT, authToken, gameID);
        this.playerType = playerType;
    }

    public PlayerType getPlayerType() {
        return playerType;
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.CONNECT;
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}
