package server;

import com.google.gson.Gson;
import dataaccess.*;
import exception.DataAccessException;
import exception.UnauthorizedException;
import model.*;
import service.GameService;
import service.UserService;
import service.ClearService;
import spark.*;

import java.util.Map;

public class Server {
    private final UserService userService;
    private final GameService gameService;
    private final ClearService clearService;

    public Server() {
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        this.userService = new UserService(userDAO, authDAO);
        this.gameService = new GameService(gameDAO);
        this.clearService = new ClearService(userDAO, authDAO, gameDAO);
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

    private Object loginHandler(Request req, Response res) {
        var loginRequest = new Gson().fromJson(req.body(), LoginRequest.class);
        try {
            var loginResult = userService.loginService(loginRequest);
            res.status(200);
            return new Gson().toJson(loginResult);
        } catch (DataAccessException e) {
            res.status(500);
            return "Internal Server Error";
        } catch (UnauthorizedException e) {
            res.status(401);
            return "Error: unauthorized\"";
        }
    }

    private Object logoutHandler(Request req, Response res) {
         var logoutRequest = new Gson().fromJson(req.body(), LogoutRequest.class);
         try {
             userService.logoutService(logoutRequest);
             res.status(200);
             return "{}";
         } catch (DataAccessException e) {
             res.status(500);
             return "Internal Service Error";
         } catch (UnauthorizedException e) {
             res.status(401);
             return "Error: unauthorized\"";
         }
    }

    private Object listGamesHandler(Request req, Response res) throws DataAccessException {
        var listGamesRequest = new Gson().fromJson(req.body(), ListGamesRequest.class);
        var listGamesResult = gameService.listGamesService(listGamesRequest);
        return new Gson().toJson(Map.of("games", listGamesResult));
    }

    private Object createGamesHandler(Request req, Response res) throws DataAccessException {
        var createGamesRequest = new Gson().fromJson(req.body(), CreateGameRequest.class);
        var createGamesResult = gameService.createGameService(createGamesRequest);
        return new Gson().toJson(createGamesResult);
    }

    private Object joinGameHandler(Request req, Response res) throws DataAccessException{
        var joinGamesRequest = new Gson().fromJson(req.body(), JoinGamesRequest.class);
        var joinGamesResult = gameService.JoinGameService(joinGamesRequest);
        return new Gson().toJson(joinGamesResult);
    }

    private Object clearHandler(Request req, Response res) {
        try {
            clearService.clearService();
            res.status(200);
            return "{}";
        } catch (DataAccessException e) {
            res.status(500);
            return "Internal Server Error";
        }
    }
}