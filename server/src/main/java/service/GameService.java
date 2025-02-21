package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import exception.DataAccessException;
import exception.UnauthorizedException;
import model.*;

public class GameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;
    private int randomInt = 0;

    public GameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public ListGamesResult listGamesService(ListGamesRequest listGamesRequest) throws DataAccessException {
        return gameDAO.listGames(listGamesRequest);
    }

    public int createAndSaveGameID(String gameName) throws DataAccessException {
        randomInt += 1;
        int gameID = 0;
        gameID += randomInt;
        // create a new chess game
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(gameID, null, null, gameName, game);
        gameDAO.addGame(gameData);
        return gameID;
    }

    public CreateGameResult createGameService(CreateGameRequest createGameRequest) throws DataAccessException,
            BadRequestException, UnauthorizedException {

        if (createGameRequest.gameName() == null) {
            throw new BadRequestException("unauthorized");
        }

        // authenticate
        AuthData authData = authDAO.getAuthToken(createGameRequest.authToken());
        if (authData == null) {
            throw new UnauthorizedException("unauthorized");
        }

        // generate a gameID
        int gameID = createAndSaveGameID(createGameRequest.gameName());

        return new CreateGameResult(gameID);
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
        GameData existingGame = gameDAO.getGame(joinGameRequest.gameID());
        if (existingGame != null) {
            throw new AlreadyTakenException("Username already taken");
        }
        return gameDAO.joinGame(joinGameRequest);
    }
}
