package dataaccess;

import exception.DataAccessException;
import model.*;

import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    // Key: ID Value: GameDataRecord
    final private HashMap<Integer, GameData> gameDataTable = new HashMap<>();

    @Override
    public ListGamesResult listGames(Object listGamesRequest) throws DataAccessException {
        return null;
    }

    @Override
    public CreateGameResult createGames(CreateGameRequest createGameRequest) throws DataAccessException {
        return null;
    }

    public void addGame(GameData gameData){
        gameDataTable.put(gameData.gameId(), gameData);
    }

    @Override
    public JoinGamesResult joinGame(JoinGamesRequest joinGameRequest) throws DataAccessException {
        return null;
    }

    @Override
    public GameData getGame(int gameID) {
        if (gameDataTable.containsKey(gameID)) {
            return gameDataTable.get(gameID);
        }
        return null;
    }

    @Override
    public GameData updateGame(GameData gameData) throws DataAccessException {
        return null;
    }

    @Override
    public void clearGameDAO(){
        gameDataTable.clear();
    }

}