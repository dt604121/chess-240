package service;

import dataaccess.GameDAO;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import exception.DataAccessException;
import exception.UnauthorizedException;
import model.*;

public class GameService {
    private final GameDAO gameDAO;

    public GameService(GameDAO gameDAO){
        this.gameDAO = gameDAO;
    }
    public ListGamesResult listGamesService(ListGamesRequest listGamesRequest) throws DataAccessException {
        return gameDAO.listGames(listGamesRequest);
    }

    public CreateGameResult createGameService(CreateGameRequest createGameRequest) throws DataAccessException,
            BadRequestException, UnauthorizedException {

        if (createGameRequest.gameName() == null){
            throw new BadRequestException("unauthorized");
        }

        if (createGameRequest.authToken() == null) {
            throw new UnauthorizedException("unauthorized");
        }

        GameData gameData = new GameData();
        return new CreateGameResult();
    }

    public JoinGamesResult joinGameService(JoinGamesRequest joinGameRequest) throws DataAccessException,
            BadRequestException, UnauthorizedException, AlreadyTakenException {
        if (joinGameRequest.playerColor() == null | joinGameRequest.authToken() == null) {
            throw new BadRequestException("unauthorized");
        }
        if (joinGameRequest.authToken() == null) {
            throw new UnauthorizedException("unauthorized");
        }
        // exists already
        GameData existingGame = gameDAO.getGame();
        if (existingGame != null) {
            throw new AlreadyTakenException("Username already taken");
        }
        return gameDAO.joinGame(joinGameRequest);
    }
}
