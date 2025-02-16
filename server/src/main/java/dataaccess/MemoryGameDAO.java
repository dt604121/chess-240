package dataaccess;

import exception.DataAccessException;
import model.*;

import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    // Key: Username Value: GameDataRecord
    final private HashMap<String, GameData> gameDataTable = new HashMap<>();

    @Override
    public ListGamesResult listGames(Object listGamesRequest) throws DataAccessException {
        return null;
    }

    @Override
    public CreateGamesResult createGames(CreateGamesRequest createGamesRequest) throws DataAccessException {
        return null;
    }

    @Override
    public JoinGamesResult joinGame(JoinGamesRequest joinGameRequest) throws DataAccessException {
        return null;
    }

    @Override
    public GameData getGame(String gameID) throws DataAccessException {
        return null;
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