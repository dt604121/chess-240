package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import exception.DataAccessException;
import exception.UnauthorizedException;
import model.*;

import java.util.Collection;
import java.util.Objects;

public class GameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;
    private int randomInt = 0;

    public GameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public ListGamesResult listGamesService(ListGamesRequest listGamesRequest) throws DataAccessException,
            UnauthorizedException {
        // authenticate
        AuthData authData = authDAO.getAuthToken(listGamesRequest.authToken());
        if (authData == null){
            throw new UnauthorizedException("Error: unauthorized");
        }
        Collection<GameData> games = gameDAO.listGames(listGamesRequest);
        return new ListGamesResult(games);
    }

    public int createAndSaveGameID(String gameName) {
        randomInt += 1;
        int gameID = 0;
        gameID += randomInt;
        // create a new chess game
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(gameID, null, null, gameName, game);
        gameDAO.addGame(gameData);
        return gameID;
    }

    public CreateGameResult createGameService(CreateGameRequest createGameRequest, String authToken) throws DataAccessException,
            BadRequestException, UnauthorizedException {

        if (createGameRequest.gameName() == null) {
            throw new BadRequestException("\"Error: bad request\"");
        }

        // authenticate
        if (authDAO.getAuthToken(authToken) == null) {
            throw new UnauthorizedException("unauthorized");
        }

        // generate a gameID
        int gameID = createAndSaveGameID(createGameRequest.gameName());

        return new CreateGameResult(gameID);
    }
    public JoinGamesResult joinGameService(JoinGamesRequest joinGameRequest) throws DataAccessException,
            BadRequestException, UnauthorizedException, AlreadyTakenException {
        if (joinGameRequest.playerColor() == null || joinGameRequest.authToken() == null) {
            throw new BadRequestException("\"Error: bad request\"");
        }

        // authenticate
        AuthData authData = authDAO.getAuthToken(joinGameRequest.authToken());
        if (authData == null){
            throw new UnauthorizedException("Error: unauthorized");
        }
        String username = authData.username();

        // white/black username exists already (playerColor)
        GameData existingGame = gameDAO.getGame(joinGameRequest.gameID());
        if (joinGameRequest.playerColor().equals("WHITE") && Objects.equals(username, existingGame.whiteUsername())) {
            throw new AlreadyTakenException("Username already taken");
        }

        if (joinGameRequest.playerColor().equals("BLACK") && Objects.equals(username, existingGame.blackUsername())) {
            throw new AlreadyTakenException("Username already taken");
        }

        gameDAO.updateGame(existingGame);
        return new JoinGamesResult();
    }
}
