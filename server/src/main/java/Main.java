import dataaccess.memory.MemoryAuthDAO;
import dataaccess.memory.MemoryGameDAO;
import dataaccess.memory.MemoryUserDAO;
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
        GameService gameService = new GameService(memoryGameDAO, memoryAuthDAO);
        Server server = new Server();
        server.run(8080);
    }
}