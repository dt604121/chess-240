package server;

import com.google.gson.Gson;
import dataaccess.*;
import exception.AlreadyTakenException;
import exception.BadRequestException;
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

    private Object registerHandler(Request req, Response res) {
        // JSON -> Java Object (serialize)
        var registerRequest = new Gson().fromJson(req.body(), RegisterRequest.class);
        try {
            var registerResult = userService.registerService(registerRequest);
            res.status(200);
            // Java Object -> JSON (deserialize)
            return new Gson().toJson(registerResult);
        } catch (BadRequestException e) {
            res.status(400);
            return "Error: bad request";
        } catch (AlreadyTakenException e) {
            res.status(403);
            return "Error: already taken";
        } catch (DataAccessException e) {
            res.status(500);
            return "Internal Server Error";
        }
    }

    private Object loginHandler(Request req, Response res) {
        var loginRequest = new Gson().fromJson(req.body(), LoginRequest.class);
        try {
            var loginResult = userService.loginService(loginRequest);
            res.status(200);
            return new Gson().toJson(loginResult);
        } catch (UnauthorizedException e) {
            res.status(401);
            return "Error: unauthorized\"";
        } catch (DataAccessException e) {
            res.status(500);
            return "Internal Server Error";
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
        var joinGamesResult = gameService.joinGameService(joinGamesRequest);
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