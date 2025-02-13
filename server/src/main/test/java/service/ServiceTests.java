package java.service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import exception.DataAccessException;
import exception.UnauthorizedException;
import model.LoginRequest;
import model.LoginResult;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ServiceTests {
    private UserService userService;
    private MemoryUserDAO memoryUserDAO;
    private MemoryAuthDAO memoryAuthDAO;

    @BeforeEach
    void setUP() throws DataAccessException{
        userService = new UserService(memoryUserDAO, memoryAuthDAO);
        memoryUserDAO = new MemoryUserDAO();
        memoryAuthDAO = new MemoryAuthDAO();

        UserData testUser = new UserData("testUser", "1234", "test@example.com");
        memoryUserDAO.addUser(testUser);
    }

    // Login
    @Test
    void loginPositiveTest() throws DataAccessException, UnauthorizedException {
        // positive: empty -> assertTrue
        LoginRequest loginRequest = new LoginRequest("testUser", "1234");

        LoginResult loginResult = userService.loginService(loginRequest, memoryUserDAO, memoryAuthDAO);

        assertNotNull(loginResult);
        assertEquals("testUser", loginResult.username());
        assertNotNull(loginResult.authToken());
    }

    @Test
    void loginNegativeTest() throws DataAccessException, UnauthorizedException {
        // negative: username is taken -> assertThrows
        LoginRequest loginRequest = new LoginRequest("testUser", "incorrectPassword");
        LoginResult loginResult = userService.loginService(loginRequest, memoryUserDAO, memoryAuthDAO);

        assertThrows(UnauthorizedException.class, this::invalidLogin);
    }

    private void invalidLogin() throws DataAccessException, UnauthorizedException {
        LoginRequest loginRequest = new LoginRequest("testUser", "incorrectPassword");
        userService.loginService(loginRequest, memoryUserDAO, memoryAuthDAO);
    }
}
