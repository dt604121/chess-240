package client;

import sharedexceptions.ResponseException;
import model.CreateGameRequest;
import model.JoinGamesRequest;
import model.LoginRequest;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;

public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;
    public UserData user;

    @BeforeAll
    public static void init() {
        server = new Server();

        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);

        var url = "http://localhost:" + port;
        facade = new ServerFacade(url);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void clear() throws Exception {
        facade.clear();
    }

    @Test
    void registerUserPositiveTest() {
        user = new UserData("Cami", "cutie", "bestie@gmail.com");
        var result = assertDoesNotThrow(() -> facade.registerUser(user));
        assertNotNull(result);
        assertTrue(result.authToken().length() > 10);
    }

    @Test
    void registerUserNegativeTest() {
        UserData empty = new UserData("", "", "");
        var result = assertThrows(ResponseException.class, () -> facade.registerUser(empty));
        assertEquals("Registration failed: Username, email, and password are required.", result.getMessage());
    }

    @Test
    void loginUserPositiveTest() throws ResponseException {
        user = new UserData("Cami", "cutie", "bestie@gmail.com");
        facade.registerUser(user);
        var loginRequest = new LoginRequest("Cami", "cutie");
        var result = assertDoesNotThrow(() -> facade.loginUser(loginRequest));
        assertNotNull(result);
        assertEquals("Cami", result.username());
    }

    @Test
    void loginUserNegativeTest() throws ResponseException {
        user = new UserData("Cami", "cutie", "bestie@gmail.com");
        facade.registerUser(user);
        var loginRequest = new LoginRequest("wrongUser", "wrongPass");
        assertThrows(ResponseException.class, () -> facade.loginUser(loginRequest));
    }

    @Test
    void logoutUserPositiveTest() throws ResponseException {
        UserData testUser = new UserData("Cami", "cutie", "email");
        var result = facade.registerUser(testUser);
        assertNotNull(result.authToken());

        LoginRequest loginRequest = new LoginRequest("Cami", "cutie");
        facade.loginUser(loginRequest);

        assertDoesNotThrow(() -> facade.logoutUser(testUser));
    }

    @Test
    void logoutUserNegativeTest() {
        ResponseException exception = assertThrows(ResponseException.class, () -> facade.logoutUser(null));
        assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    void listGamesPositiveTest() throws ResponseException {
        user = new UserData("Cami", "cutie", "bestie@gmail.com");
        facade.registerUser(user);
        var result = assertDoesNotThrow(() -> facade.listGames());
        assertNotNull(result);
    }

    @Test
    void listGamesNegativeTest() throws ResponseException {
        user = new UserData("Cami", "cutie", "bestie@gmail.com");
        facade.registerUser(user);

        LoginRequest loginRequest = new LoginRequest("Cami", "cutie");
        facade.loginUser(loginRequest);

        facade.logoutUser(user);

        ResponseException exception = assertThrows(ResponseException.class, () ->
                facade.listGames());
        assertEquals("You must sign in.", exception.getMessage());
    }

    @Test
    void createGamesPositiveTest() throws ResponseException {
        user = new UserData("Cami", "cutie", "bestie@gmail.com");
        facade.registerUser(user);
        LoginRequest loginRequest = new LoginRequest("Cami", "cutie");
        facade.loginUser(loginRequest);
        CreateGameRequest request = new CreateGameRequest("Danica");
        var result = assertDoesNotThrow(() -> facade.createGames(request));
        assertNotNull(result);
    }

    @Test
    void createGamesNegativeTest() throws ResponseException {
        user = new UserData("Cami", "cutie", "bestie@gmail.com");
        facade.registerUser(user);
        CreateGameRequest request = new CreateGameRequest("");
        ResponseException exception = assertThrows(ResponseException.class, () -> facade.createGames(request));
        assertEquals("Invalid game name. Cannot be left blank.", exception.getMessage());
    }

    @Test
    void playGamePositiveTest() throws ResponseException {
        user = new UserData("Cami", "cutie", "bestie@gmail.com");
        facade.registerUser(user);

        LoginRequest loginRequest = new LoginRequest("Cami", "cutie");
        facade.loginUser(loginRequest);

        CreateGameRequest createRequest = new CreateGameRequest("Cami'sGame");
        var result = facade.createGames(createRequest);

        JoinGamesRequest request = new JoinGamesRequest("BLACK", result.gameID());
        var result2 = facade.joinGame(request);
        assertNotNull(result2);
    }

    @Test
    void playGameNegativeTest() throws ResponseException {
        user = new UserData("Cami", "cutie", "bestie@gmail.com");
        facade.registerUser(user);
        LoginRequest loginRequest = new LoginRequest("Cami", "cutie");
        facade.loginUser(loginRequest);
        JoinGamesRequest request = new JoinGamesRequest("BLACK", 1234);
        assertThrows(ResponseException.class, () ->
                facade.joinGame(request));
    }

    @Test
    void clearTest() {
        assertDoesNotThrow(() -> facade.clear());
    }
}
