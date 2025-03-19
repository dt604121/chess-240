package client;

import exception.ResponseException;
import model.CreateGameRequest;
import model.JoinGamesRequest;
import model.LoginRequest;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import ui.PostLoginClient;
import ui.PreLoginClient;
import ui.ServerFacade;
import ui.State;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;
    static PostLoginClient postLoginClient;
    static PreLoginClient preLoginClient;
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
        user = new UserData("Cami", "cutie", "bestie@gmail.com");
        assertDoesNotThrow(() -> facade.registerUser(user));
    }

    @Test
    void registerUserPositiveTest() {
        var result = assertDoesNotThrow(() -> facade.registerUser(user));
        assertNotNull(result);
        assertTrue(result.authToken().length() > 10);
    }

    @Test
    void registerUserNegativeTest() {
        UserData nullUser = new UserData(null, null, null);
        assertThrows(ResponseException.class, () -> facade.registerUser(nullUser));
    }

    @Test
    void loginUserPositiveTest() {
        var loginRequest = new LoginRequest("Cami", "cutie");
        var result = assertDoesNotThrow(() -> facade.loginUser(loginRequest));
        assertNotNull(result);
        assertEquals("Cami", result.username());
    }

    @Test
    void loginUserNegativeTest() {
        var loginRequest = new LoginRequest("wrongUser", "wrongPass");
        assertThrows(ResponseException.class, () -> facade.loginUser(loginRequest));
    }

    @Test
    void logoutUserPositiveTest() throws ResponseException {
        UserData testUser = new UserData("Cami", "cutie", "email");
        facade.registerUser(testUser);

        LoginRequest loginRequest = new LoginRequest("Cami", "cutie");
        var loginResult = facade.loginUser(loginRequest);

        assertDoesNotThrow(() -> facade.logoutUser(testUser));
        assertNull(loginResult.authToken());
    }

    @Test
    void logoutUserNegativeTest() {
        ResponseException exception = assertThrows(ResponseException.class, () -> facade.logoutUser(null));
        assertEquals("You must sign in", exception.getMessage());
    }

    @Test
    void listGamesPositiveTest() {
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
        LoginRequest loginRequest = new LoginRequest("cami", "cutie");
        facade.loginUser(loginRequest);
        CreateGameRequest request = new CreateGameRequest("Danica");
        var result = assertDoesNotThrow(() -> facade.createGames(request));
        assertNotNull(result);
    }

    @Test
    void createGamesNegativeTest() {
        CreateGameRequest request = new CreateGameRequest("");
        ResponseException exception = assertThrows(ResponseException.class, () -> facade.createGames(request));
        assertEquals("Invalid game name. Cannot be left blank.", exception.getMessage());
    }

    @Test
    void playGamePositiveTest() {
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
    void observeGamePositiveTest() {
        var result = assertDoesNotThrow(() -> facade.observeGame(-1));
        assertNotNull(result);
    }

    @Test
    void observeGameNegativeTest() {
        ResponseException exception = assertThrows(ResponseException.class, () ->
                facade.observeGame(1234));
        assertEquals(400, exception.getStatusCode());
    }

    @Test
    void clearTest() {
        assertDoesNotThrow(() -> facade.clear());

        var result = assertDoesNotThrow(() -> facade.listGames());
        assertTrue(result.games().isEmpty());
    }
}
