package dataaccess;

import exception.DataAccessException;
import model.*;

public interface GameDAO {
    ListGamesResult listGames(Object listGamesRequest) throws DataAccessException;
    CreateGameResult createGames(CreateGameRequest createGameRequest) throws DataAccessException;
    JoinGamesResult joinGame(JoinGamesRequest joinGameRequest) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    void clearGameDAO() throws DataAccessException;
    GameData updateGame(GameData gameData) throws DataAccessException;
}
