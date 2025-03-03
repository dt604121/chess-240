package dataaccess.sql;

import dataaccess.dao.GameDAO;
import exception.DataAccessException;
import model.GameData;

import java.util.Collection;
import java.util.List;

public class SQLGameDAO implements GameDAO {
    // list pets
    @Override
    public Collection<GameData> listGames(Object listGamesRequest) throws DataAccessException {
        return List.of();
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public void clearGameDAO() throws DataAccessException {

    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {

    }

    // add pet
    @Override
    public void addGame(GameData gameData) {

    }
}
