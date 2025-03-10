package ui;

import java.util.Arrays;

import client.State;
import exception.ResponseException;

public class PreLoginClient {
    private final ServerFacade serverFacade;
    private final String serverUrl;
    private State state = State.SIGNEDOUT;

    public PreLoginClient(String serverUrl) {
        serverFacade = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> login(params);
                case "register" -> register();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }
    public String login(String... params) throws ResponseException {
        if (params.length >= 1) {
            state = State.SIGNEDIN;
        }
        throw new ResponseException(400, "Expected: <yourname>");
    }

    public void register() {

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
