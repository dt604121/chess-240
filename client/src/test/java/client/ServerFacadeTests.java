package client;

import exception.ResponseException;
import model.*;
import org.junit.jupiter.api.*;
import server.Server;
import ui.GamePlayClient;
import ui.GamePlayClient;
import ui.PostLoginClient;
import ui.PreLoginClient;
import ui.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;
    static PostLoginClient postLoginClient;
    static PreLoginClient preLoginClient;
    static GamePlayClient gamePlayClient;
    private client.State state = client.State.SIGNEDIN;

    @BeforeAll
    public static void init() {
        server = new Server();

        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade();
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
    void registerUserPositiveTest() throws Exception {
        state = client.State.SIGNEDOUT;
        var result = assertDoesNotThrow(() -> preLoginClient.register("joe", "password", "email@gmail.com"));
        assertNotNull(result);
    }

    @Test
    void registerUserNegativeTest() throws Exception {
        state = client.State.SIGNEDOUT;
        ResponseException exception = assertThrows(ResponseException.class,
                () -> preLoginClient.register(null, null, null));
        assertEquals(400, exception);
        assertEquals("Expected: <yourname>", exception.getMessage());
    }

    @Test
    void loginUserPositiveTest() throws Exception{
        state = client.State.SIGNEDOUT;
        var result = assertDoesNotThrow(() -> preLoginClient.login("joe", "password"));
        assertNotNull(result);
    }
    @Test
    void loginUserNegativeTest() throws Exception{
        state = client.State.SIGNEDOUT;
        var result = assertThrows(() -> preLoginClient.login(null, null, null));
        assertNull(result);
    }

    @Test
    void logoutUserPositiveTest() throws Exception {
        state = client.State.SIGNEDIN;
        var result = assertDoesNotThrow(() -> postLoginClient.logout());
        assertNotNull(result);
    }

    @Test
    void logoutUserNegativeTest() throws Exception {
        state = client.State.SIGNEDOUT;
        var result = assertThrows(() -> postLoginClient.logout());
        assertEquals("You must sign in", result);
    }

    @Test
    void listGamesPositiveTest() throws Exception {
        state = client.State.SIGNEDIN;
        var result = assertDoesNotThrow(() -> postLoginClient.listGames());
        assertNotNull(result);
    }

    @Test
    void listGamesNegativeTest() throws Exception {
        state = client.State.SIGNEDOUT;
        var result = assertThrows(() -> postLoginClient.listGames());
        assertEquals("You must sign in", result);
    }

    @Test
    void createGamesPositiveTest() throws Exception {
        state = client.State.SIGNEDIN;
        var result = assertDoesNotThrow(() -> postLoginClient.createGame());
        assertNotNull(result);
    }

    @Test
    void createGamesNegativeTest() throws Exception {
        state = client.State.SIGNEDOUT;
        var result = assertThrows(() -> postLoginClient.createGame());
        assertEquals("You must sign in", result);
    }

    @Test
    void joinGamePositiveTest() throws Exception {
        state = client.State.SIGNEDIN;
        var result = assertDoesNotThrow(() -> postLoginClient.joinGame());
        assertNotNull(result);
    }

    @Test
    void joinGameNegativeTest() throws Exception {
        state = client.State.SIGNEDOUT;
        var result = assertThrows(() -> postLoginClient.joinGame());
        assertEquals("You must sign in", result);
    }

    @Test
    void clearTest() throws Exception {

    }
}
