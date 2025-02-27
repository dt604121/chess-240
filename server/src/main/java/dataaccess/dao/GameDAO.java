package dataaccess.dao;

import exception.DataAccessException;
import model.*;

import java.util.Collection;

public interface GameDAO {
    Collection<GameData> listGames(Object listGamesRequest) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    void clearGameDAO() throws DataAccessException;
    void updateGame(GameData gameData) throws DataAccessException;
    void addGame(GameData gameData);
}
