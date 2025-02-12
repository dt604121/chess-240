package server;

import com.google.gson.Gson;
import model.*;
import service.UserService;
import spark.*;
import java.util.UUID;

import java.util.Map;

public class Server {
    private final UserService userService;

    public Server(UserService userService) {
        this.userService = userService;
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGames);
        Spark.put("/game", this::joinGame);
        Spark.delete("/db", this::clearUserDAO);

        // This line initializes the server and can be removed once you have a functioning endpoint
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private void exceptionHandler(ResponseException ex, Request req, Response res) {
        res.status(ex.StatusCode());
        res.body(ex.toJson());
    }
    private Object register(Request req, Response res) throws ResponseException {
        var registerRequest = new Gson().fromJson(req.body(), RegisterRequest.class);
        var registerResult = userService.register(registerRequest);
        return new Gson().toJson(registerResult);
    }

    private Object login(Request req, Response res) throws ResponseException {
        var loginRequest = new Gson().fromJson(req.body(), LoginRequest.class);
        var loginResult = userService.login(loginRequest);
        return new Gson().toJson(loginResult);
    }

    private Object logout(Request req, Response res) throws ResponseException {
        // var logoutRequest = new Gson().fromJson(req.body(), LogoutRequest.class);
        // var logoutResult = userService.logout(logoutRequest);
        return null;
    }

    private Object listGames(Request req, Response res) throws ResponseException {
        // TODO: best guess?
        var listGamesRequest = new Gson().fromJson(req.body(), ListGamesRequest.class);
        var listGamesResult = userService.listGames(listGamesRequest);
        return new Gson().toJson(Map.of("games", listGamesResult));
    }

    private Object createGames(Request req, Response res) throws ResponseException {
        var createGamesRequest = new Gson().fromJson(req.body(), CreateGamesRequest.class);
        var createGamesResult = userService.createGames(createGamesRequest);
        return new Gson().toJson(createGamesResult);
    }

    private Object joinGame(Request req, Response res) throws ResponseException{
        var joinGamesRequest = new Gson().fromJson(req.body(), JoinGamesRequest.class);
        var joinGamesResult = userService.joinGame(joinGamesRequest);
        return new Gson().toJson(joinGamesResult);
    }

    private Object clearUserDAO(Request req, Response res) throws ResponseException {
        userService.clearUserDAO();
        res.status(204);
        return "";
    }
}
