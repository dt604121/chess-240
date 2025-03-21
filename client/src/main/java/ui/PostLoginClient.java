package ui;
import chess.ChessBoard;
import exception.ResponseException;
import model.*;

import java.util.Arrays;
import java.util.Objects;

public class PostLoginClient {
    private final ServerFacade serverFacade;
    private State state = State.SIGNEDIN;
    private final String serverUrl;
    private UserData user;

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
                case "create" -> createGame(params);
                case "list" -> listGames();
                case "observe" -> observeGame(params);
                case "play" -> joinGame(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String logout()  throws ResponseException {
        assertSignedIn();
        state = State.SIGNEDOUT;

        try {
            serverFacade.logoutUser(this.user);
            this.user = null;  // Clear the user data after logging out
            return "%s, you have signed out. Come back soon!";
        } catch (Exception e) {
            throw new ResponseException(401, "Logout failed: " + e.getMessage());
        }
    }

    public String createGame(String... params)  throws ResponseException {
        assertSignedIn();
        if (params.length != 1) {
            throw new ResponseException(400, "Expected: name");
        }

        var name = params[0];
        if (name.isEmpty()) {
            throw new ResponseException(400, "Invalid game name. Cannot be left blank.");
        }

        try {
            CreateGameRequest request = new CreateGameRequest(name);
            CreateGameResult result = serverFacade.createGames(request);
            int gameId = result.gameID();
            return String.format("You created a game as %s with an id of %d", name, gameId);
        } catch (ResponseException ex) {
            throw new ResponseException(401, ex.getMessage());
        }
    }

    public String listGames() throws ResponseException {
        assertSignedIn();

        try {
            ListGamesResult result = serverFacade.listGames();
            if (result.games().isEmpty()){
                return "No active games found.";
            }
            return result.toString();

            // implement listGames
        } catch (ResponseException ex) {
            throw new ResponseException(401, "Failed to list games" + ex.getMessage());
        }
    }

    public String joinGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length < 1) {
            throw new ResponseException(400, "Expected: <id> [color]");
        }
        var id = Integer.parseInt(params[0]);
        var color = params[1];

        JoinGamesRequest request = new JoinGamesRequest(color, id);

        try {
            boolean whitePerspective = !Objects.equals(color, "BLACK");
            ChessBoard board = new ChessBoard();
            board.resetBoard();
            ChessBoardUI.drawChessBoard(System.out, board, whitePerspective);
            serverFacade.joinGame(request);
            return String.format("You have joined the game as %s!", color);

        } catch (ResponseException ex) {
            throw new ResponseException(401, "failed to join game" +ex.getMessage());
        }
    }

    public String observeGame(String... params) throws ResponseException {
        assertSignedIn();

        if (params.length < 1) {
            throw new ResponseException(400, "Expected: <id>");
        }
        var id = Integer.parseInt(params[0]);

        try {
            GameData gameData = serverFacade.observeGame(id);

            boolean whitePerspective = true;

            ChessBoard board = gameData.game().getBoard();
            board.resetBoard();
            ChessBoardUI.drawChessBoard(System.out, board, whitePerspective);

            return String.format("Observing game %d", id);

        } catch (ResponseException ex) {
            throw new ResponseException(401, ex.getMessage());
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

    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException(400, "You must sign in");
        }
    }
}
