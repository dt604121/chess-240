package client;

import exception.ResponseException;
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
        UserData nullUser = new UserData(null, null, null);
        var result = assertThrows(ResponseException.class, () -> facade.registerUser(nullUser));
        assertEquals("Error: Invalid user data. Name, password, and email cannot be null", result.getMessage());
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
        facade.registerUser(testUser);

        assertDoesNotThrow(() -> facade.logoutUser(testUser));
    }

    @Test
    void logoutUserNegativeTest() {
        ResponseException exception = assertThrows(ResponseException.class, () -> facade.logoutUser(null));
        assertEquals("You must sign in", exception.getMessage());
    }

    @Test
    void listGamesPositiveTest() throws ResponseException {
        user = new UserData("Cami", "cutie", "bestie@gmail.com");
        facade.registerUser(user);
        var result = assertDoesNotThrow(() -> facade.listGames());
        assertNotNull(result);
    }

    @Test
    void listGamesNegativeTest() {
        ResponseException exception = assertThrows(ResponseException.class, () ->
                facade.listGames());
        assertEquals("You must sign in", exception.getMessage());
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
        JoinGamesRequest request = new JoinGamesRequest("BLACK", 1234);
        var result = assertDoesNotThrow(() -> facade.joinGame(request));
        assertNotNull(result);
    }

    @Test
    void playGameNegativeTest() {
        ResponseException exception = assertThrows(ResponseException.class, () -> facade.joinGame(null));
        assertEquals("You must sign in", exception.getMessage());
    }

    @Test
    void observeGamePositiveTest() throws ResponseException {
        user = new UserData("Cami", "cutie", "bestie@gmail.com");
        facade.registerUser(user);
        var result = assertDoesNotThrow(() -> facade.observeGame(-1));
        assertNotNull(result);
    }

    @Test
    void observeGameNegativeTest() throws ResponseException {
        user = new UserData("Cami", "cutie", "bestie@gmail.com");
        facade.registerUser(user);
        ResponseException exception = assertThrows(ResponseException.class, () ->
                facade.observeGame(1234));
        assertEquals(400, exception.getStatusCode());
    }

    @Test
    void clearTest() {
        assertDoesNotThrow(() -> facade.clear());
    }
}
