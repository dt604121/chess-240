package server;

import com.google.gson.Gson;
import dataaccess.DatabaseManager;
import dataaccess.dao.*;
import dataaccess.sql.*;
import exception.*;
import model.*;
import server.websocket.WebSocketHandler;
import service.*;
import spark.*;

import java.util.Map;

public class Server {
    private final UserService userService;
    private final GameService gameService;
    private final ClearService clearService;
    private final WebSocketHandler webSocketHandler;

    public Server() {
        DatabaseManager databaseManager = new DatabaseManager();
        UserDAO userDAO = new SQLUserDAO(databaseManager);
        AuthDAO authDAO = new SQLAuthDAO(databaseManager);
        GameDAO gameDAO = new SQLGameDAO(databaseManager);
        this.userService = new UserService(userDAO, authDAO);
        this.gameService = new GameService(gameDAO, authDAO);
        this.clearService = new ClearService(userDAO, authDAO, gameDAO);
        // pass in auth and gamedao
        webSocketHandler = new WebSocketHandler();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.webSocket("/ws", webSocketHandler);

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::registerHandler);
        Spark.post("/session", this::loginHandler);
        Spark.delete("/session", this::logoutHandler);
        Spark.get("/game", this::listGamesHandler);
        Spark.post("/game", this::createGamesHandler);
        Spark.put("/game", this::joinGameHandler);
        Spark.delete("/db", this::clearHandler);

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

            return new Gson().toJson(registerResult);
        } catch (BadRequestException e) {
            res.status(400);
            return "{ \"message\": \"Error: bad request\" }";
        } catch (AlreadyTakenException e) {
            res.status(403);
            return "{ \"message\": \"Error: already taken\" }";
        } catch (DataAccessException e) {
            res.status(500);
            return new Gson().toJson(Map.of("message",e.getMessage()));
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
            return "{ \"message\": \"Error: unauthorized\" }";
        } catch (DataAccessException e) {
            res.status(500);
            return new Gson().toJson(Map.of("message",e.getMessage()));
        }
    }

    private Object logoutHandler(Request req, Response res) {
        String authToken = req.headers("authorization");
         try {
             userService.logoutService(authToken);
             res.status(200);
             return "{}";
         } catch (DataAccessException e) {
             res.status(500);
             return new Gson().toJson(Map.of("message",e.getMessage()));
         } catch (UnauthorizedException e) {
             res.status(401);
             return "{ \"message\": \"Error: unauthorized\" }";
         }
    }

    private Object listGamesHandler(Request req, Response res) {
        String authToken = req.headers("Authorization");
        try {
            var listGamesResult = gameService.listGamesService(authToken);
            res.status(200);
            return new Gson().toJson(listGamesResult);
        } catch (UnauthorizedException e) {
            res.status(401);
            return "{ \"message\": \"Error: unauthorized\" }";
        } catch (DataAccessException e) {
            res.status(500);
            return new Gson().toJson(Map.of("message",e.getMessage()));
        }
    }

    private Object createGamesHandler(Request req, Response res) {
        var createGamesRequest = new Gson().fromJson(req.body(), CreateGameRequest.class);
        String authToken = req.headers("authorization");
        try {
            var createGamesResult = gameService.createGameService(createGamesRequest, authToken);
            res.status(200);

            return new Gson().toJson(createGamesResult);
        } catch (BadRequestException e){
            res.status(400);
            return "{ \"message\": \"Error: bad request\" }";
        } catch (UnauthorizedException e) {
            res.status(401);
            return "{ \"message\": \"Error: unauthorized\" }";
        } catch (DataAccessException e) {
            res.status(500);
            return new Gson().toJson(Map.of("message",e.getMessage()));
        }
    }

    private Object joinGameHandler(Request req, Response res) {
        var joinGamesRequest = new Gson().fromJson(req.body(), JoinGamesRequest.class);
        String authToken = req.headers("authorization");
        try {
            gameService.joinGameService(joinGamesRequest, authToken);
            res.status(200);

            // String message = username + " joined game";
            // call websocket handler

            return "{}";
        } catch (BadRequestException e) {
            res.status(400);
            return "{ \"message\": \"Error: bad request\" }";
        } catch (UnauthorizedException e) {
            res.status(401);
            return "{ \"message\": \"Error: unauthorized\" }";
        } catch (AlreadyTakenException e) {
            res.status(403);
            return "{ \"message\": \"Error: already taken\" }";
        } catch (DataAccessException e) {
            res.status(500);
            return new Gson().toJson(Map.of("message",e.getMessage()));
        }
    }

    private Object clearHandler(Request req, Response res) {
        try {
            clearService.clearService();
            res.status(200);
            return "{}";
        } catch (DataAccessException e) {
            res.status(500);
            return "{ \"message\": \"Error: (description of error)\" }";
        }
    }
}