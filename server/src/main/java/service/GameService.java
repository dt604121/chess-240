package service;

import chess.ChessGame;
import dataaccess.dao.AuthDAO;
import dataaccess.dao.GameDAO;
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

    public ListGamesResult listGamesService(String authToken) throws DataAccessException,
            UnauthorizedException {
        // authenticate
        AuthData authData = authDAO.getAuthToken(authToken);
        if (authData == null){
            throw new UnauthorizedException("Error: unauthorized");
        }
        Collection<GameData> games = gameDAO.listGames(authToken);
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

    public CreateGameResult createGameService(CreateGameRequest createGameRequest, String authToken)
            throws DataAccessException,
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
    public void joinGameService(JoinGamesRequest joinGameRequest, String authToken) throws DataAccessException,
            BadRequestException, UnauthorizedException, AlreadyTakenException {

        GameData existingGame = gameDAO.getGame(joinGameRequest.gameID());
        if (joinGameRequest.playerColor() == null || joinGameRequest.gameID() < 1 || existingGame == null ||
                (!(joinGameRequest.playerColor().equals("WHITE")) && !(joinGameRequest.playerColor().equals("BLACK")))) {
            throw new BadRequestException("\"Error: bad request\"");
        }

        // authenticate
        AuthData authData = authDAO.getAuthToken(authToken);
        if (authData == null){
            throw new UnauthorizedException("Error: unauthorized");
        }

        String username = authData.username();

        // white/black username exists already (playerColor)
        if (joinGameRequest.playerColor().equals("WHITE") && !Objects.equals(username, existingGame.whiteUsername()) &&
                existingGame.whiteUsername() != null) {
            throw new AlreadyTakenException("Username already taken");
        }

        if (joinGameRequest.playerColor().equals("BLACK") && !Objects.equals(username, existingGame.blackUsername()) &&
                existingGame.blackUsername() != null) {
            throw new AlreadyTakenException("Username already taken");
        }

        // save the username
        if (joinGameRequest.playerColor().equals("WHITE") ){
            GameData gameData = new GameData(joinGameRequest.gameID(), username, existingGame.blackUsername(),
                    existingGame.gameName(), existingGame.game());
            gameDAO.updateGame(gameData);
        }

        if (joinGameRequest.playerColor().equals("BLACK") ){
            GameData gameData = new GameData(joinGameRequest.gameID(),existingGame.whiteUsername(), username,
                    existingGame.gameName(), existingGame.game());
            gameDAO.updateGame(gameData);
        }


    }
}
