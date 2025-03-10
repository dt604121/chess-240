package client;

import org.junit.jupiter.api.*;
import server.Server;
import ui.GameplayClient;
import ui.PostLoginClient;
import ui.PreLoginClient;
import ui.ServerFacade;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;
    static PostLoginClient postLoginClient;
    static PreLoginClient preLoginClient;
    static GameplayClient gameplayClient;

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
        preLoginClient.clear();
        postLoginClient.clear();
        gameplayClient.clear();
    }
//
//    @Test
//    void register() throws Exception {
//        var authData = facade.register("player1", "password", "p1@email.com");
//        assertTrue(authData.authToken().length() > 10);
//    }

}
