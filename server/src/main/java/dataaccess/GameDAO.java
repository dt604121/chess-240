package dataaccess;

import model.GameData;
import server.ResponseException;

public interface GameDAO {
    GameData listGames(Object listGamesRequest) throws ResponseException;
    GameData createGame(GameData) throws ResponseException;
    GameData getGame(String gameID) throws ResponseException;
    GameData updateGame(GameData) throws ResponseException;
    void clearGameDAO() throws ResponseException;
}
