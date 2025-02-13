package server;

import com.google.gson.Gson;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import exception.DataAccessException;
import exception.UnauthorizedException;
import model.*;
import service.GameService;
import service.UserService;
import spark.*;

import java.util.Map;

public class Server {
    private final UserService userService;
    private final GameService gameService;

    public Server(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::registerHandler);
        Spark.post("/session", this::loginHandler);
        Spark.delete("/session", this::logoutHandler);
        Spark.get("/game", this::listGamesHandler);
        Spark.post("/game", this::createGamesHandler);
        Spark.put("/game", this::joinGameHandler);
        Spark.delete("/db", this::clearHandler);

        // This line initializes the server and can be removed once you have a functioning endpoint
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object registerHandler(Request req, Response res) throws DataAccessException {
        var registerRequest = new Gson().fromJson(req.body(), RegisterRequest.class);
        var registerResult = userService.registerService(registerRequest);
        return new Gson().toJson(registerResult);
    }

    private Object loginHandler(Request req, Response res) throws DataAccessException, UnauthorizedException {
        var loginRequest = new Gson().fromJson(req.body(), LoginRequest.class);
        try {
            MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
            MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
            var loginResult = userService.loginService(loginRequest, memoryUserDAO, memoryAuthDAO);
            return new Gson().toJson(loginResult);
            // TODO: fix the syntax for this rq
        } catch (UnauthorizedException e){
            res.status(401);
            return "Unauthorized";
        } catch (DataAccessException e) {
            res.status(500);
            return "Internal Server Error";
        }
    }

    private Object logoutHandler(Request req, Response res) throws DataAccessException {
        // var logoutRequest = new Gson().fromJson(req.body(), LogoutRequest.class);
        // var logoutResult = userService.logout(logoutRequest);
        return null;
    }

    private Object listGamesHandler(Request req, Response res) throws DataAccessException {
        // TODO: best guess?
        var listGamesRequest = new Gson().fromJson(req.body(), ListGamesRequest.class);
        var listGamesResult = gameService.listGames(listGamesRequest);
        return new Gson().toJson(Map.of("games", listGamesResult));
    }

    private Object createGamesHandler(Request req, Response res) throws DataAccessException {
        var createGamesRequest = new Gson().fromJson(req.body(), CreateGamesRequest.class);
        var createGamesResult = gameService.createGames(createGamesRequest);
        return new Gson().toJson(createGamesResult);
    }

    private Object joinGameHandler(Request req, Response res) throws DataAccessException{
        var joinGamesRequest = new Gson().fromJson(req.body(), JoinGamesRequest.class);
        var joinGamesResult = gameService.joinGame(joinGamesRequest);
        return new Gson().toJson(joinGamesResult);
    }

    private Object clearHandler(Request req, Response res) throws DataAccessException {
        userService.clearUserDAOService();
//        userService.clearGameDAO();
//        userService.clearAuthDAO();
        res.status(204);
        return "";
    }
}
