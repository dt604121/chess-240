package ui;
import client.State;
import exception.ResponseException;

import java.util.Arrays;

public class PostLoginClient {
    private final ServerFacade serverFacade;
    private State state = State.SIGNEDIN;

    public PostLoginClient(){
        serverFacade = new ServerFacade();
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0 ) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "logout" -> logout();
                case "create" -> createGame(params);
                case "list" -> listGames();
                case "observe" -> observeGame(params);
                case "join" -> joinGame(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String joinGame(String... params) throws ResponseException {
        assertSignedIn();
        // TODO: add try / catch block
        if (params.length >= 1) {
            state = State.SIGNEDIN;
        }
        var id = params[0];
        var color = params[1];

        // how is this part different than the joinGame we implemneted in the UserService?
        serverFacade.joinGame();
        throw new ResponseException(400, "Expected: <id> [color]");
        return "";
    }

    public String observeGame(String... params) throws ResponseException {
        assertSignedIn();

        // TODO: add try / catch block

        if (params.length < 1) {
            throw new ResponseException(400, "Expected: <id>");
        }
        var id = params[0];

        boolean whitePerspective = true;

        // how do we grab the board to actually display it?
        ChessBoardUI.drawChessBoard(System.out, board, whitePerspective);

        // call observeGame()

        return "Observing game " + id;
    }

    public String listGames() throws ResponseException {
        assertSignedIn();
        serverFacade.listGames();
        return "";
    }

    public String createGame(String... params)  throws ResponseException {
        assertSignedIn();
        // TODO: add try / catch block
        if (params.length == 1) {
            state = State.SIGNEDIN;
        }
        var name = params[0];

        // how is this part different than the joinGame we implemneted in the UserService?
        serverFacade.createGames();
        throw new ResponseException(400, "Expected: name");
        return "";
    }

    public String logout()  throws ResponseException {
        assertSignedIn();
        serverFacade.logoutUser();
        return "";
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

    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException(400, "You must sign in");
        }
    }
}
