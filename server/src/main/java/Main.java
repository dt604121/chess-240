import chess.ChessGame;
import chess.ChessPiece;
import dataaccess.memory.MemoryAuthDAO;
import dataaccess.memory.MemoryGameDAO;
import dataaccess.memory.MemoryUserDAO;
import exception.DataAccessException;
import server.Server;
import service.GameService;
import service.UserService;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        server.run(8080);
    }
}