package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import exception.DataAccessException;
import exception.UnauthorizedException;
import model.LoginRequest;
import model.LoginResult;
import model.UserData;
import org.eclipse.jetty.util.log.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Server;
import service.GameService;
import service.UserService;

import javax.xml.crypto.Data;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests {
    MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
    MemoryGameDAO memoryGameDAO = new MemoryGameDAO();
    MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
    UserService userService = new UserService(memoryUserDAO, memoryAuthDAO);
    GameService gameService = new GameService(memoryGameDAO);

    @BeforeEach
    void setUP() throws DataAccessException {
        memoryUserDAO.clearUserDAO();
        memoryAuthDAO.clearAuthDAO();
        memoryGameDAO.clearGameDAO();
        UserData userData = new UserData("testUser", "1234", "testUser@email");
        memoryUserDAO.addUser(userData);
        assertNotNull(memoryUserDAO.getUser("testUser"), "User should be added before login test");
    }

    // Login
    @Test
    void loginPositiveTest() throws DataAccessException, UnauthorizedException {
        // positive: empty -> assertTrue
        LoginRequest loginRequest = new LoginRequest("testUser", "1234");
        LoginResult loginResult = userService.login(loginRequest);

        assertEquals(loginRequest.username(), loginResult.username());
        assertNotNull(loginResult.authToken());
    }
    @Test
    void loginInvalidPasswordTest() throws UnauthorizedException {
        // negative: wrong password -> assertThrows
        LoginRequest invalidLoginRequest = new LoginRequest("testUser", "1233");

        assertThrows(UnauthorizedException.class, () -> userService.login(invalidLoginRequest));
    }
    @Test
    void loginNonexistentUsernameTest() throws DataAccessException {
        // negative: nonexistent username -> assertThrows
        LoginRequest nonexistentUserRequest = new LoginRequest("nonExistentUser", "password");

        assertThrows(DataAccessException.class, () -> userService.login(nonexistentUserRequest));
    }
    @Test
    void loginEmptyUsernameTest() throws DataAccessException {
        // negative: empty username
        LoginRequest emptyUsernameRequest = new LoginRequest("", "password");

        assertThrows(DataAccessException.class, () -> userService.login(emptyUsernameRequest));
    }
    @Test
    void loginEmptyPasswordTest() throws UnauthorizedException {
        LoginRequest emptyPasswordRequest = new LoginRequest("testUser", "");

        assertThrows(UnauthorizedException.class, () -> userService.login(emptyPasswordRequest));
    }
}
