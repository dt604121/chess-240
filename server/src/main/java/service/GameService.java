package service;

import dataaccess.GameDAO;
import exception.DataAccessException;
import model.*;

public class GameService {
    private final GameDAO gameDAO;

    public GameService(GameDAO gameDAO){
        this.gameDAO = gameDAO;
    }
    public ListGamesResult listGames(ListGamesRequest listGamesRequest) throws DataAccessException {
        return gameDAO.listGames(listGamesRequest);
    }

    public CreateGamesResult createGames(CreateGamesRequest createGamesRequest) throws DataAccessException{
        return gameDAO.createGames(createGamesRequest);
    }

    public JoinGamesResult joinGame(JoinGamesRequest joinGameRequest) throws DataAccessException {
        return gameDAO.joinGame(joinGameRequest);
    }

    void clearGameDAO() throws DataAccessException{
        gameDAO.clearGameDAO();
    }

    public GameData getGame(String gameID) throws DataAccessException{
        return gameDAO.getGame(gameID);
    }

//    public GameData updateGame(GameData) throws DataAccessException{
//        return gameDAO.updateGame(GameData);
//    }
}
