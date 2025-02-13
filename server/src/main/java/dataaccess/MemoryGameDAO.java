package dataaccess;

import exception.DataAccessException;
import model.*;

public class MemoryGameDAO implements GameDAO {
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
    public GameData updateGame() throws DataAccessException {
        return null;
    }

    @Override
    public void clearGameDAO() throws DataAccessException {

    }

}