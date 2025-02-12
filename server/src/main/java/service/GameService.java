package service;

import dataaccess.GameDAO;
import model.*;
import server.ResponseException;

public class GameService {
    private final GameDAO gameDAO;

    public GameService(GameDAO gameDAO){
        this.gameDAO = gameDAO;
    }
    public ListGamesResult listGames(ListGamesRequest listGamesRequest) throws ResponseException {
        return gameDAO.listGames(listGamesRequest);
    }

    public CreateGamesResult createGames(CreateGamesRequest createGamesRequest) throws ResponseException{
        return gameDAO.createGames(createGamesRequest);
    }

    public JoinGamesResult joinGame(JoinGamesRequest joinGameRequest) throws ResponseException {
        return gameDAO.joinGame(joinGameRequest);
    }

    void clearGameDAO() throws ResponseException{
        gameDAO.clearGameDAO();
    }

    public GameData getGame(String gameID) throws ResponseException{
        return gameDAO.getGame(gameID);
    }
    public GameData updateGame(GameData) throws ResponseException{
        return gameDAO.updateGame(GameData);
    }
}
