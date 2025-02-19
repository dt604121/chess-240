package service;

import dataaccess.GameDAO;
import exception.DataAccessException;
import model.*;

public class GameService {
    private final GameDAO gameDAO;

    public GameService(GameDAO gameDAO){
        this.gameDAO = gameDAO;
    }
    public ListGamesResult listGamesService(ListGamesRequest listGamesRequest) throws DataAccessException {
        return gameDAO.listGames(listGamesRequest);
    }

    public CreateGameResult createGameService(CreateGameRequest createGameRequest) throws DataAccessException{
        return gameDAO.createGames(createGameRequest);
    }

    public JoinGamesResult joinGameService(JoinGamesRequest joinGameRequest) throws DataAccessException {
        return gameDAO.joinGame(joinGameRequest);
    }

    public GameData getGame(int gameID) throws DataAccessException{
        return gameDAO.getGame(gameID);
    }

    public GameData updateGame(GameData gameData) throws DataAccessException{
        return gameDAO.updateGame(gameData);
    }
}
