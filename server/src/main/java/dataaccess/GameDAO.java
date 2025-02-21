package dataaccess;

import chess.ChessGame;
import exception.DataAccessException;
import model.*;

public interface GameDAO {
    ListGamesResult listGames(Object listGamesRequest) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    void clearGameDAO() throws DataAccessException;
    void updateGame(GameData gameData) throws DataAccessException;
    void addGame(GameData gameData);
}
