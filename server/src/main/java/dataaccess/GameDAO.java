package dataaccess;

import model.*;

public interface GameDAO {
    ListGamesResult listGames(Object listGamesRequest) throws ResponseException;
    CreateGamesResult createGames(CreateGamesRequest createGamesRequest) throws ResponseException;
    JoinGamesResult joinGame(JoinGamesRequest joinGameRequest) throws ResponseException
    GameData getGame(String gameID) throws ResponseException;
    GameData updateGame(GameData) throws ResponseException;
    void clearGameDAO() throws ResponseException;
}
