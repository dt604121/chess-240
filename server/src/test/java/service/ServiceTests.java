package service;

import chess.ChessGame;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import exception.DataAccessException;
import exception.UnauthorizedException;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests {
    MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
    MemoryGameDAO memoryGameDAO = new MemoryGameDAO();
    MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
    UserService userService = new UserService(memoryUserDAO, memoryAuthDAO);
    GameService gameService = new GameService(memoryGameDAO);

    @BeforeEach
    void setUP() throws DataAccessException {
        // Clear the DAO's
        memoryUserDAO.clearUserDAO();
        memoryAuthDAO.clearAuthDAO();
        memoryGameDAO.clearGameDAO();
        UserData userData = new UserData("testUser", "1234", "testUser@email");
        memoryUserDAO.addUser(userData);
        assertNotNull(memoryUserDAO.getUser("testUser"), "User should be added before login test");
    }
    // Register
    //
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

    // Logout
    @Test
    void logoutPositiveTest () throws DataAccessException, UnauthorizedException {
        String authToken = "1234";
        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        assertNotNull(memoryAuthDAO.getAuthToken(authToken));

        userService.logoutService(logoutRequest);
        assertNull(memoryAuthDAO.getAuthToken(authToken));
    }
    @Test
    void logoutNullAuthTokenTest() {
        LogoutRequest logoutNullRequest = new LogoutRequest(null);

        assertThrows(UnauthorizedException.class, () -> userService.logoutService(logoutNullRequest));
    }

    // List Games
    @Test
    void listGamesPositiveTest() throws DataAccessException {
        ListGamesRequest listGamesRequest = new ListGamesRequest("1234");
        ListGamesResult listGamesResult = gameService.listGamesService(listGamesRequest);

        assertNotNull(listGamesResult);
    }
    @Test
    void listGamesUnauthorizedTest() throws DataAccessException {
        ListGamesRequest listGamesUnauthorizedRequest = new ListGamesRequest("1234");
        ListGamesResult listGamesUnauthorizedResult = gameService.listGamesService(listGamesUnauthorizedRequest);

        assertNull(listGamesUnauthorizedResult);
    }

    // Create Game
    @Test
    void createGamePositiveTest() throws DataAccessException {
        CreateGameRequest createGameRequest = new CreateGameRequest("gameName");
        CreateGameResult createGameResult = gameService.createGameService(createGameRequest);

        assertNotNull(createGameResult);
    }
    @Test
    void createGameUnauthorizedTest() throws DataAccessException {
        CreateGameRequest createGameUnauthorizedRequest = new CreateGameRequest(null);
        CreateGameResult createGameUnauthorizedResult = gameService.createGameService(createGameUnauthorizedRequest);

        assertNull(createGameUnauthorizedResult);
    }

    // Join Game
    @Test
    void joinGamePositiveTest() throws DataAccessException {
        JoinGamesRequest joinGameRequest = new JoinGamesRequest("WHITE", 1234, "1233");
        JoinGamesResult joinGameResult = gameService.JoinGameService(joinGameRequest);

        assertNotNull(joinGameResult);
    }
    @Test
    void joinGameUnauthorizedTest() throws DataAccessException {
        JoinGamesRequest joinGameUnauthorizedRequest = new JoinGamesRequest("white", 1234,
                null);
        JoinGamesResult joinGameUnauthorizedResult = gameService.JoinGameService(joinGameUnauthorizedRequest);

        assertNull(joinGameUnauthorizedResult);
    }

    // Clear
    @Test
    void clearApplicationTest() throws DataAccessException {
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
