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
    void loginInvalidPasswordTest() throws UnauthorizedException {
        // negative: wrong password -> assertThrows
        LoginRequest invalidLoginRequest = new LoginRequest("testUser", "1233");

        assertThrows(UnauthorizedException.class, () -> userService.loginService(invalidLoginRequest));
    }
    @Test
    void loginNonexistentUsernameTest() throws DataAccessException {
        // negative: nonexistent username -> assertThrows
        LoginRequest nonexistentUserRequest = new LoginRequest("nonExistentUser", "password");

        assertThrows(DataAccessException.class, () -> userService.loginService(nonexistentUserRequest));
    }
    @Test
    void loginEmptyUsernameTest() throws DataAccessException {
        // negative: empty username
        LoginRequest emptyUsernameRequest = new LoginRequest("", "password");

        assertThrows(DataAccessException.class, () -> userService.loginService(emptyUsernameRequest));
    }
    @Test
    void loginEmptyPasswordTest() throws UnauthorizedException {
        LoginRequest emptyPasswordRequest = new LoginRequest("testUser", "");

        assertThrows(UnauthorizedException.class, () -> userService.loginService(emptyPasswordRequest));
    }

    // Logout
    // List Games
    // Create Game
    // Join Game
    // Clear
    @Test
    void clearApplicationTest() throws DataAccessException{
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
        assertThrows(DataAccessException.class, () -> memoryUserDAO.getUser("clearUser"),
                "UserDAO should be empty after clear.");
        assertThrows(DataAccessException.class, () -> memoryGameDAO.getGame(12),
                "GameDAO should be empty after clear.");
        assertThrows(DataAccessException.class, () -> memoryAuthDAO.getAuthToken("1234"),
                "AuthDAO should be empty after clear.");
    }
}
