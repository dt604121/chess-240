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
/*    public GameData listGames(Object listGamesRequest) {
        return null;
    }

    public GameData createGame(GameData) {
        return null;
    }

    public GameData getGame(String gameID) {
        return null;
    }

    public GameData updateGame(GameData) {
        return null;
    }

    public void clearGameDAO(){

    }*/

}