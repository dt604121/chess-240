import dataaccess.*;
import server.Server;
import service.GameService;
import service.UserService;

public class Main {
    public static void main(String[] args) {
        MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
        MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
        MemoryGameDAO memoryGameDAO = new MemoryGameDAO();
        // Phase 4: swap for sqlUserDAO
        UserService userService = new UserService(memoryUserDAO, memoryAuthDAO);
        GameService gameService = new GameService(memoryGameDAO);
        Server server = new Server(userService, gameService);
        server.run(8080);
    }
}