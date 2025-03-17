package client;

import exception.ResponseException;
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
    private ui.State state = ui.State.SIGNEDIN;

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

//    @Test
//    void register() throws Exception {
//        var authData = preLoginClient.register("player1", "password", "p1@email.com");
//        assertTrue(authData.authToken().length() > 10);
//    }

    @Test
    void registerUserPositiveTest() {
        state = ui.State.SIGNEDOUT;
        UserData user = new UserData("joe", "password", "email@gmail.com");
        var result = assertDoesNotThrow(() -> facade.registerUser(user));
        assertTrue(result.authToken().length() > 10);
        assertNotNull(result);
    }

    @Test
    void registerUserNegativeTest() {
        state = State.SIGNEDOUT;
        assertThrows(NullPointerException.class,
                () -> facade.registerUser(null));
    }

    @Test
    void loginUserPositiveTest() {
        state = State.SIGNEDOUT;
        var result = assertDoesNotThrow(() -> preLoginClient.login("joe", "password"));
        assertNotNull(result);
    }
    @Test
    void loginUserNegativeTest() {
        state = State.SIGNEDOUT;
        ResponseException exception = assertThrows(ResponseException.class, () ->
                preLoginClient.login(null, null, null));
        assertEquals("You must sign in", exception.getMessage());
    }

    @Test
    void logoutUserPositiveTest() {
        state = State.SIGNEDIN;
        var result = assertDoesNotThrow(() -> postLoginClient.logout());
        assertNotNull(result);
    }

    @Test
    void logoutUserNegativeTest() {
        state = State.SIGNEDOUT;
        ResponseException exception = assertThrows(ResponseException.class, () ->
                postLoginClient.logout());
        assertEquals("You must sign in", exception.getMessage());
    }

    @Test
    void listGamesPositiveTest() {
        state = ui.State.SIGNEDIN;
        var result = assertDoesNotThrow(() -> postLoginClient.listGames());
        assertNotNull(result);
    }

    @Test
    void listGamesNegativeTest() {
        state = State.SIGNEDOUT;
        ResponseException exception = assertThrows(ResponseException.class, () ->
                postLoginClient.listGames());
        assertEquals("You must sign in", exception.getMessage());
    }

    @Test
    void createGamesPositiveTest() {
        state = ui.State.SIGNEDIN;
        var result = assertDoesNotThrow(() -> postLoginClient.createGame());
        assertNotNull(result);
    }

    @Test
    void createGamesNegativeTest() {
        state = State.SIGNEDOUT;
        ResponseException exception = assertThrows(ResponseException.class, () ->
                postLoginClient.createGame(null, null));
        assertEquals("You must sign in", exception.getMessage());
    }

    @Test
    void playGamePositiveTest() {
        state = ui.State.SIGNEDIN;
        var result = assertDoesNotThrow(() -> postLoginClient.joinGame());
        assertNotNull(result);
    }

    @Test
    void playGameNegativeTest() {
        state = State.SIGNEDOUT;
        ResponseException exception = assertThrows(ResponseException.class, () ->
                postLoginClient.joinGame(null, null, null));
        assertEquals("You must sign in", exception.getMessage());
    }

    @Test
    void observeGamePositiveTest() {
        state = ui.State.SIGNEDIN;
        var result = assertDoesNotThrow(() -> postLoginClient.observeGame());
        assertNotNull(result);
    }

    @Test
    void observeGameNegativeTest() {
        state = State.SIGNEDOUT;
        ResponseException exception = assertThrows(ResponseException.class, () ->
                postLoginClient.observeGame(null, null, null));
        assertEquals("You must sign in", exception.getMessage());
    }

    @Test
    void clearTest() {

    }
}
