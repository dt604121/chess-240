package dataaccess;

import exception.DataAccessException;
import model.*;

import javax.xml.crypto.Data;

public interface GameDAO {
    ListGamesResult listGames(Object listGamesRequest) throws DataAccessException;
    CreateGamesResult createGames(CreateGamesRequest createGamesRequest) throws DataAccessException;
    JoinGamesResult joinGame(JoinGamesRequest joinGameRequest) throws DataAccessException;
    GameData getGame(String gameID) throws DataAccessException;
    void clearGameDAO() throws DataAccessException;
    GameData updateGame(GameData gameData) throws DataAccessException;
}
