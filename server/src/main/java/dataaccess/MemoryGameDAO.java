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
    public CreateGamesResult createGames(CreateGamesRequest createGamesRequest) throws DataAccessException {
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
    public GameData getGame(int gameID) throws DataAccessException {
        if (gameDataTable.containsKey(gameID)) {
            return gameDataTable.get(gameID);
        }
        throw new DataAccessException("No game found.");
    }

    @Override
    public GameData updateGame(GameData gameData) throws DataAccessException {
        return null;
    }

    @Override
    public void clearGameDAO() throws DataAccessException {
        gameDataTable.clear();
    }

}