package client;

import exception.ResponseException;
import model.*;
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
        RegisterRequest request = new RegisterRequest("Cami", "cutie", "bestie@gmail.com");
        var result = assertDoesNotThrow(() -> facade.registerUser(request));
        assertNotNull(result);
        assertTrue(result.authToken().length() > 10);
    }

    @Test
    void registerUserNegativeTest() {
        RegisterRequest request = new RegisterRequest("", "", "");
        var result = assertThrows(ResponseException.class, () -> facade.registerUser(request));
        assertEquals("\"Error: name, password, and email cannot be empty", result.getMessage());
    }

    @Test
    void loginUserPositiveTest() throws ResponseException {
        RegisterRequest request = new RegisterRequest("Cami", "cutie", "bestie@gmail.com");
        facade.registerUser(request);
        var loginRequest = new LoginRequest("Cami", "cutie");
        var result = assertDoesNotThrow(() -> facade.loginUser(loginRequest));
        assertNotNull(result);
        assertEquals("Cami", result.username());
    }

    // TODO: fix the typing to be request instead of user?!
//    @Test
//    void loginUserNegativeTest() throws ResponseException {
//        RegisterRequest request = new RegisterRequest("Cami", "cutie", "bestie@gmail.com");
//        facade.registerUser(request);
//        var loginRequest = new LoginRequest("wrongUser", "wrongPass");
//        assertThrows(ResponseException.class, () -> facade.loginUser(loginRequest));
//    }

    @Test
    void logoutUserPositiveTest() throws ResponseException {
        RegisterRequest request = new RegisterRequest("Cami", "cutie", "email");
        RegisterResult result = facade.registerUser(request);

        assertDoesNotThrow(() -> facade.logoutUser(result));
    }

    @Test
    void logoutUserNegativeTest() {
        UserData notSignedInUser = new UserData("UnknownUser", "password", "email");

        ResponseException exception = assertThrows(ResponseException.class, () -> facade.logoutUser(notSignedInUser));

        assertEquals("You must sign in", exception.getMessage());
    }

    @Test
    void listGamesPositiveTest() throws ResponseException {
        RegisterRequest request = new RegisterRequest("Cami", "cutie", "bestie@gmail.com");
        facade.registerUser(request);
        var result = assertDoesNotThrow(() -> facade.listGames());
        assertNotNull(result);
    }

    @Test
    void listGamesNegativeTest() throws ResponseException {
        RegisterRequest request = new RegisterRequest("Cami", "cutie", "bestie@gmail.com");
        RegisterResult result = facade.registerUser(request);
        facade.logoutUser(result);
        ResponseException exception = assertThrows(ResponseException.class, () ->
                facade.listGames());
        assertEquals("You must sign in", exception.getMessage());
    }

    @Test
    void createGamesPositiveTest() throws ResponseException {
        RegisterRequest request = new RegisterRequest("Cami", "cutie", "bestie@gmail.com");
        facade.registerUser(request);
        LoginRequest loginRequest = new LoginRequest("Cami", "cutie");
        facade.loginUser(loginRequest);
        CreateGameRequest gameRequest = new CreateGameRequest("Danica");
        var result = assertDoesNotThrow(() -> facade.createGames(gameRequest));
        assertNotNull(result);
    }

    @Test
    void createGamesNegativeTest() throws ResponseException {
        RegisterRequest request = new RegisterRequest("Cami", "cutie", "bestie@gmail.com");
        facade.registerUser(request);
        CreateGameRequest gameRequest = new CreateGameRequest("");
        ResponseException exception = assertThrows(ResponseException.class, () -> facade.createGames(gameRequest));
        assertEquals("Invalid game name. Cannot be left blank.", exception.getMessage());
    }

    @Test
    void playGamePositiveTest() throws ResponseException {
        RegisterRequest request = new RegisterRequest("Cami", "cutie", "bestie@gmail.com");
        facade.registerUser(request);
        JoinGamesRequest joinGamesRequest = new JoinGamesRequest("BLACK", 1234);
        var result = assertDoesNotThrow(() -> facade.joinGame(joinGamesRequest));
        assertNotNull(result);
    }

    @Test
    void playGameNegativeTest() throws ResponseException {
        RegisterRequest request = new RegisterRequest("Cami", "cutie", "bestie@gmail.com");
        var result = facade.registerUser(request);
        facade.logoutUser(result);

        JoinGamesRequest params = new JoinGamesRequest("BLACK", 1234);
        ResponseException exception = assertThrows(ResponseException.class, () -> facade.joinGame(params));

        assertEquals("You must sign in", exception.getMessage());
    }

    @Test
    void observeGamePositiveTest() throws ResponseException {
        RegisterRequest request = new RegisterRequest("Cami", "cutie", "bestie@gmail.com");
        facade.registerUser(request);
        var result = assertDoesNotThrow(() -> facade.observeGame(-1));
        assertNotNull(result);
    }

    @Test
    void observeGameNegativeTest() throws ResponseException {
        RegisterRequest request = new RegisterRequest("Cami", "cutie", "bestie@gmail.com");
        facade.registerUser(request);
        ResponseException exception = assertThrows(ResponseException.class, () ->
                facade.observeGame(1234));
        assertEquals(400, exception.getStatusCode());
    }

    @Test
    void clearTest() {
        assertDoesNotThrow(() -> facade.clear());
    }
}
