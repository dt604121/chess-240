package dataaccess;

import chess.ChessGame;
import exception.DataAccessException;
import model.*;

public interface GameDAO {
    ListGamesResult listGames(Object listGamesRequest) throws DataAccessException;
    JoinGamesResult joinGame(JoinGamesRequest joinGameRequest) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    void clearGameDAO() throws DataAccessException;
    GameData updateGame(GameData gameData) throws DataAccessException;
    GameData createGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game)
            throws DataAccessException;
    void addGame(GameData gameData);
}
