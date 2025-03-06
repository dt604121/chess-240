package service;

import chess.ChessGame;
import dataaccess.memory.MemoryAuthDAO;
import dataaccess.memory.MemoryGameDAO;
import dataaccess.memory.MemoryUserDAO;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import exception.DataAccessException;
import exception.UnauthorizedException;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests {
    MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
    MemoryGameDAO memoryGameDAO = new MemoryGameDAO();
    MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
    UserService userService = new UserService(memoryUserDAO, memoryAuthDAO);
    GameService gameService = new GameService(memoryGameDAO, memoryAuthDAO);

    @BeforeEach
    void setUP() {
        // Clear the DAO's
        memoryUserDAO.clearUserDAO();
        memoryAuthDAO.clearAuthDAO();
        memoryGameDAO.clearGameDAO();
        UserData userData = new UserData("testUser", "1234", "testUser@email");
        memoryUserDAO.addUser(userData);
        assertNotNull(memoryUserDAO.getUser("testUser"), "User should be added before login test");
    }
    @Test
    void registerPositiveTest() throws DataAccessException, AlreadyTakenException, BadRequestException {
        RegisterRequest registerRequest = new RegisterRequest("userTest", "1234",
                "testing@email.com");
        RegisterResult registerResult = userService.registerService(registerRequest);

        assertEquals(registerRequest.username(), registerResult.username());
        assertNotNull(memoryAuthDAO.getAuthToken(registerResult.authToken()));
    }
    @Test
    void registerExistingUserTest() {
        RegisterRequest registerExistingUserRequest = new RegisterRequest("testUser", "1234",
                "testing@email.com");

        assertThrows(AlreadyTakenException.class, () -> userService.registerService(registerExistingUserRequest));
    }
    // Login
    @Test
    void loginPositiveTest() throws DataAccessException, UnauthorizedException {
        // positive: empty -> assertTrue
        LoginRequest loginRequest = new LoginRequest("testUser", "1234");
        LoginResult loginResult = userService.loginService(loginRequest);

        assertEquals(loginRequest.username(), loginResult.username());
        assertNotNull(loginResult.authToken());
    }
    @Test
    void loginInvalidPasswordTest() {
        // negative: wrong password -> assertThrows
        LoginRequest invalidLoginRequest = new LoginRequest("testUser", "1233");

        assertThrows(UnauthorizedException.class, () -> userService.loginService(invalidLoginRequest));
    }
    @Test
    void loginNonexistentUsernameTest() {
        // negative: nonexistent username -> assertThrows
        LoginRequest nonexistentUserRequest = new LoginRequest("nonExistentUser", "password");

        assertThrows(UnauthorizedException.class, () -> userService.loginService(nonexistentUserRequest));
    }
    @Test
    void loginEmptyUsernameTest() {
        // negative: empty username
        LoginRequest emptyUsernameRequest = new LoginRequest("", "password");

        assertThrows(UnauthorizedException.class, () -> userService.loginService(emptyUsernameRequest));
    }
    @Test
    void loginEmptyPasswordTest() {
        LoginRequest emptyPasswordRequest = new LoginRequest("testUser", "");

        assertThrows(UnauthorizedException.class, () -> userService.loginService(emptyPasswordRequest));
    }

    @Test
    void logoutPositiveTest () throws DataAccessException, UnauthorizedException {
        AuthData authData = userService.createAndSaveAuthToken("testUser");
        String authToken = authData.authToken();
        memoryAuthDAO.addAuthToken(authData);
        assertNotNull(memoryAuthDAO.getAuthToken(authData.authToken()));

        userService.logoutService(authToken);
        assertNull(memoryAuthDAO.getAuthToken(authData.authToken()));
    }
    @Test
    void logoutNullAuthTokenTest() {
        assertThrows(UnauthorizedException.class, () -> userService.logoutService(null));
    }

    // List Games
    @Test
    void listGamesPositiveTest() throws DataAccessException, UnauthorizedException {
        GameData game1 = new GameData(2, "whiteUsername", "blackUsername",
                "gameName", null);
        memoryGameDAO.addGame(game1);
        AuthData authData = userService.createAndSaveAuthToken("testUser");
       String authToken = authData.authToken();
       ListGamesResult listGamesResult = gameService.listGamesService(authToken);

        assertNotNull(listGamesResult);
        assertTrue(listGamesResult.games().contains(game1));
    }
    @Test
    void listGamesUnauthorizedTest() {
        String authToken = "1234";
        assertThrows(UnauthorizedException.class, () -> gameService.listGamesService(authToken));
    }

    // Create Game
    @Test
    void createGamePositiveTest() throws DataAccessException, UnauthorizedException, BadRequestException {
        CreateGameRequest createGameRequest = new CreateGameRequest("gameName");
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, "testUser");
        memoryAuthDAO.addAuthToken(authData);
        CreateGameResult createGameResult = gameService.createGameService(createGameRequest, authToken);

        assertTrue(createGameResult.gameID() > 0 );
        assertNotNull(createGameResult);
    }
    @Test
    void createGameUnauthorizedTest() {
        CreateGameRequest createGameUnauthorizedRequest = new CreateGameRequest(null);

        assertThrows(BadRequestException.class, () -> gameService.createGameService(createGameUnauthorizedRequest, null));
    }

    // Join Game
    @Test
    void joinGamePositiveTest() throws DataAccessException, UnauthorizedException, BadRequestException,
            AlreadyTakenException {
        GameData game1 = new GameData(1234,null, "blackUsername",
                "gameName", null);
        memoryGameDAO.addGame(game1);
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, "whiteUsername");
        memoryAuthDAO.addAuthToken(authData);
        JoinGamesRequest joinGameRequest = new JoinGamesRequest("WHITE", 1234);
        gameService.joinGameService(joinGameRequest, authToken);
    }
    @Test
    void joinGameUnauthorizedTest() {
        JoinGamesRequest joinGameUnauthorizedRequest = new JoinGamesRequest("white", 1234);

        assertThrows(BadRequestException.class, () -> gameService.joinGameService(joinGameUnauthorizedRequest,
                null));
    }

    // Clear
    @Test
    void clearApplicationTest() {
        UserData userData = new UserData("clearUser", "clear", "clearUser@email");
        GameData gameData = new GameData(12, "whiteUsername", "blackUsername",
                "gameName", new ChessGame());
        AuthData authData = new AuthData("1234", "username");
        memoryUserDAO.addUser(userData);
        memoryGameDAO.addGame(gameData);
        memoryAuthDAO.addAuthToken(authData);

        memoryUserDAO.clearUserDAO();
        memoryAuthDAO.clearAuthDAO();
        memoryGameDAO.clearGameDAO();

        // throws error if the data in the DAO still exists
        assertNull(memoryUserDAO.getUser("clearUser"),
                "UserDAO should be empty after clear.");
        assertNull(memoryGameDAO.getGame(12),
                "GameDAO should be empty after clear.");
        assertNull(memoryAuthDAO.getAuthToken("1234"),
                "AuthDAO should be empty after clear.");
    }
}
