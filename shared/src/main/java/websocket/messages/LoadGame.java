package websocket.messages;

import com.google.gson.Gson;
import model.GameData;

public class LoadGame extends ServerMessage {
    private GameData game;

    public LoadGame(ServerMessageType type) {
        super(ServerMessageType.LOAD_GAME);
    }

    public Object getGame() {
        return game;
    }

    public  void setGame(GameData gameData) {
        this.game = game;
    }
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
