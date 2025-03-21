package ui;

import java.util.Arrays;

import exception.ResponseException;
import model.LoginRequest;
import model.LoginResult;
import model.UserData;

public class PreLoginClient {
    private final ServerFacade serverFacade;
    private State state = State.SIGNEDOUT;
    private final String serverUrl;

    public PreLoginClient(String serverUrl) {
        serverFacade = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    // scan in the input and then call the correct function
    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> login(params);
                case "register" -> register(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }
    public String login(String... params) throws ResponseException {
        if (isUserLoggedIn()) {
            throw new ResponseException(400, "Already connected");
        }
        if (params.length == 2) {
            var name = params[0].trim();
            var password = params[1].trim();

            var loginRequest = new LoginRequest(name, password);

            try {
                serverFacade.loginUser(loginRequest);
                state = State.SIGNEDIN;
                return String.format("You logged in as %s", name);
            } catch (Exception e) {
                throw new ResponseException(401, "Login failed: " + e.getMessage());
            }

        }
        throw new ResponseException(400, "Expected: <name> <password>");
    }

    public String register(String... params) throws ResponseException{
        try {
            if (params.length != 3) {
                return "Error: Expected format: <name> <password> <email>";
            }

            var name = params[0].trim();
            var password = params[1].trim();
            var email = params[2].trim();

            if (name.isEmpty() || password.isEmpty() || email.isEmpty()) {
                return "Error: name, password, and email cannot be empty";
            }

            if (isUserLoggedIn()) {
                throw new ResponseException(400, "Already connected");
            }

            var user = new UserData(name, password, email);

            serverFacade.registerUser(user);

            state = State.SIGNEDIN;

            return String.format("You registered as %s.", name);
        } catch (ResponseException e) {
            return "Registration failed: " + e.getMessage();
        } catch (Exception e) {
            return "Unexpected error occurred during registration." + e.getMessage();
        }
    }

    public String help() {
        return """
                login <USERNAME> <PASSWORD> - to play chess
                register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                quit - playing chess
                help - with possible commands
                """;
    }

    public boolean isUserLoggedIn() {
        return state == State.SIGNEDIN;
    }
}
