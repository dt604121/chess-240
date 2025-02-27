package dataaccess.memory;

import dataaccess.dao.GameDAO;
import model.*;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    // Key: ID Value: GameDataRecord
    final private HashMap<Integer, GameData> gameDataTable = new HashMap<>();

    @Override
    public Collection<GameData> listGames(Object listGamesRequest) {
        return gameDataTable.values();
    }

    @Override
    public void addGame(GameData gameData) {
        gameDataTable.put(gameData.gameID(), gameData);
    }

    @Override
    public GameData getGame(int gameID) {
        if (gameDataTable.containsKey(gameID)) {
            return gameDataTable.get(gameID);
        }
        return null;
    }

    @Override
    public void updateGame(GameData gameData) {
        gameDataTable.put(gameData.gameID(), gameData);
    }

    @Override
    public void clearGameDAO(){
        gameDataTable.clear();
    }

}