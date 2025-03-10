package ui;
import client.State;
import exception.ResponseException;

import java.util.Arrays;

public class PostLoginClient {
    private final ServerFacade serverFacade;
    private final String serverUrl;
    private State state = State.SIGNEDIN;

    public PostLoginClient(String serverUrl){
        serverFacade = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0 ) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "logout" -> logout();
                case "create" -> createGame();
                case "list" -> listGames();
                case "observe" -> observeGame();
                case "join" -> joinGame();
                case "quit" -> "quit";
                default -> help();
            }
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String help() {
        return """
                logout - when you're done
                list - games
                create <NAME> - a game
                join <ID> [WHITE][BLACK] - a game
                observe <ID> - a game
                quit - playing chess
                help - with possible commands
                """;
    }
}
