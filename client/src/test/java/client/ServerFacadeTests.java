package client;

import exception.ResponseException;
import model.AuthData;
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
    private ui.State state = ui.State.SIGNEDIN;

    @BeforeAll
    public static void init() {
        server = new Server();

        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);

        var url = "http://localhost:8080";
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

//    @Test
//    void register() throws Exception {
//        var authData = preLoginClient.register("player1", "password", "p1@email.com");
//        assertTrue(authData.authToken().length() > 10);
//    }

    @Test
    void registerUserPositiveTest() throws Exception {
        state = ui.State.SIGNEDOUT;
        var result = assertDoesNotThrow(() -> preLoginClient.register("joe", "password", "email@gmail.com"));
        assertNotNull(result);
    }

    @Test
    void registerUserNegativeTest() throws Exception {
        state = State.SIGNEDOUT;
        ResponseException exception = assertThrows(ResponseException.class,
                () -> preLoginClient.register(null, null, null));
        assertEquals("Expected: <yourname>", exception.getMessage());
    }

    @Test
    void loginUserPositiveTest() throws Exception{
        state = State.SIGNEDOUT;
        var result = assertDoesNotThrow(() -> preLoginClient.login("joe", "password"));
        assertNotNull(result);
    }
    @Test
    void loginUserNegativeTest() throws Exception{
        state = State.SIGNEDOUT;
        ResponseException exception = assertThrows(ResponseException.class, () ->
                preLoginClient.login(null, null, null));
        assertEquals("You must sign in", exception.getMessage());
    }

    @Test
    void logoutUserPositiveTest() throws Exception {
        state = State.SIGNEDIN;
        var result = assertDoesNotThrow(() -> postLoginClient.logout());
        assertNotNull(result);
    }

    @Test
    void logoutUserNegativeTest() throws Exception {
        state = State.SIGNEDOUT;
        ResponseException exception = assertThrows(ResponseException.class, () ->
                postLoginClient.logout());
        assertEquals("You must sign in", exception.getMessage());
    }

    @Test
    void listGamesPositiveTest() throws Exception {
        state = ui.State.SIGNEDIN;
        var result = assertDoesNotThrow(() -> postLoginClient.listGames());
        assertNotNull(result);
    }

    @Test
    void listGamesNegativeTest() throws Exception {
        state = State.SIGNEDOUT;
        ResponseException exception = assertThrows(ResponseException.class, () ->
                postLoginClient.listGames());
        assertEquals("You must sign in", exception.getMessage());
    }

    @Test
    void createGamesPositiveTest() throws Exception {
        state = ui.State.SIGNEDIN;
        var result = assertDoesNotThrow(() -> postLoginClient.createGame());
        assertNotNull(result);
    }

    @Test
    void createGamesNegativeTest() throws Exception {
        state = State.SIGNEDOUT;
        ResponseException exception = assertThrows(ResponseException.class, () ->
                postLoginClient.createGame(null, null));
        assertEquals("You must sign in", exception.getMessage());
    }

    @Test
    void playGamePositiveTest() throws Exception {
        state = ui.State.SIGNEDIN;
        var result = assertDoesNotThrow(() -> postLoginClient.joinGame());
        assertNotNull(result);
    }

    @Test
    void playGameNegativeTest() throws Exception {
        state = State.SIGNEDOUT;
        ResponseException exception = assertThrows(ResponseException.class, () ->
                postLoginClient.joinGame(null, null, null));
        assertEquals("You must sign in", exception.getMessage());
    }

    @Test
    void observeGamePositiveTest() throws Exception {
        state = ui.State.SIGNEDIN;
        var result = assertDoesNotThrow(() -> postLoginClient.observeGame());
        assertNotNull(result);
    }

    @Test
    void observeGameNegativeTest() throws Exception {
        state = State.SIGNEDOUT;
        ResponseException exception = assertThrows(ResponseException.class, () ->
                postLoginClient.observeGame(null, null, null));
        assertEquals("You must sign in", exception.getMessage());
    }

    @Test
    void clearTest() throws Exception {

    }
}
