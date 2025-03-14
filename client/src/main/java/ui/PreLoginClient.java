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
        if (params.length >= 1) {
            var name = params[0];
            var password = params[1];

            var loginRequest = new LoginRequest(name, password);

            try {
                LoginResult loginResult = serverFacade.loginUser(loginRequest);
                // TODO: What do we do with the authToken?
                String authToken = loginResult.authToken();
                state = State.SIGNEDIN;
                return String.format("You logged in as %s", name);
            } catch (Exception e) {
                throw new ResponseException(401, "Login failed: " + e.getMessage());
            }

        }
        throw new ResponseException(400, "Expected: <name> <password>");
    }

    public String register(String... params) throws ResponseException{
        if (params.length >= 1) {
            state = State.SIGNEDIN;

            var name = params[0];
            var password = params[1];
            var email = params[2];

            var user = new UserData(name, password, email);

            serverFacade.registerUser(user);

            return String.format("You registered as %s.", name);
        }
        throw new ResponseException(400, "Expected: <name> <password> <email>");
    }

    public String help() {
        return """
                login <USERNAME> <PASSWORD> - to play chess
                register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                quit - playing chess
                help - with possible commands
                """;
    }
}
