import dataaccess.*;
import server.Server;
import service.GameService;
import service.UserService;

public class Main {
    public static void main(String[] args) {
        MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
        MemoryGameDAO memoryGameDAO = new MemoryGameDAO();
        UserService userService = new UserService(memoryUserDAO);
        GameService gameService = new GameService(memoryGameDAO);
        Server server = new Server(userService, gameService);
        server.run(8080);
    }
}