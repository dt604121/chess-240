package ui;
import chess.ChessBoard;
import exception.ResponseException;
import model.*;
import ui.websocket.NotificationHandler;
import ui.websocket.WebSocketFacade;

import java.util.*;

public class PostLoginClient {
    private final ServerFacade serverFacade;
    private final String serverUrl;
    // do we need to initialize this to be null?
    private UserData user;
    private final NotificationHandler notificationHandler;
    private WebSocketFacade ws;

    public PostLoginClient(String serverUrl, NotificationHandler notificationHandler){
        serverFacade = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.notificationHandler = notificationHandler;
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

    public String logout() throws ResponseException {
        assertSignedIn();

        try {
            ws.leaveChess(user.username());
            ws = null;
            Repl.state = State.SIGNEDOUT;
            return "You have signed out. Come back soon!";
        } catch (Exception e) {
            throw new ResponseException("Logout failed: " + e.getMessage());
        }
    }

    public String createGame(String... params)  throws ResponseException {
        assertSignedIn();

        try {
            if (params.length != 1) {
                throw new ResponseException("Expected: name");
            }

            var name = params[0].trim();

            if (name.isEmpty()) {
                throw new ResponseException("Invalid game name. Cannot be left blank.");
            }

            CreateGameRequest request = new CreateGameRequest(name);

            CreateGameResult result = serverFacade.createGames(request);

            int gameId = result.gameID();

            return String.format("You created a game as %s with an id of %d", name, gameId);

        } catch (ResponseException ex) {
            throw new ResponseException(ex.getMessage());
        }
    }

    private final Map<Integer, Integer> gameList = new HashMap<>();
    public String listGames() throws ResponseException {
        assertSignedIn();

        try {
            ListGamesResult result = serverFacade.listGames();

            if (result == null || result.games() == null || result.games().isEmpty()) {
                return "No active games found.";
            }

            gameList.clear();

            Collection<GameData> games = result.games();
            StringBuilder sb = new StringBuilder("Active Games:\n" );

            int index = 1;
            for (GameData game : games) {
                gameList.put(index, game.gameID());

                sb.append(String.format("%d. %s (White: %s, Black: %s)\n", index++, game.gameName(),
                        game.whiteUsername() == null ? "Open" : game.whiteUsername(),
                        game.blackUsername() == null ? "Open" : game.blackUsername()));
            }
            return sb.toString();

        } catch (ResponseException ex) {
            throw new ResponseException("Failed to list games" + ex.getMessage());
        }
    }

    public String joinGame(String... params) throws ResponseException {
        assertSignedIn();

        try {
            if (params.length != 2) {
                throw new ResponseException("Expected: <id> [color]");
            }
            int gameNumber;

            try {
                gameNumber = Integer.parseInt(params[0]);
            } catch (NumberFormatException e) {
                return "Error: Game ID must be a number.";
            }

            Integer gameID = gameList.get(gameNumber);
            if (gameID == null) {
                return "Error: Invalid game number. Please list the games again and choose a valid number.";
            }

            var color = params[1].trim().toUpperCase();
            if (!color.equals("WHITE") && !color.equals("BLACK")) {
                return "Error: Color must be 'WHITE' or 'BLACK'.";
            }

            JoinGamesRequest request = new JoinGamesRequest(color, gameNumber);
            serverFacade.joinGame(request);
            // TODO: add notification -> player’s name playing as COLOR.
            // WebSocket connection with the server (using the /ws endpoint) so it can send and receive gameplay messages.
            // Send a CONNECT WebSocket message to the server.
            Repl.state = State.GAMEPLAY;

            return String.format("You have joined the game as %s!", color);

        } catch (ResponseException ex) {
            throw new ResponseException("failed to join game" +ex.getMessage());
        }
    }

    public String observeGame(String... params) throws ResponseException {
        assertSignedIn();

        try {
            if (params.length != 1) {
                throw new ResponseException("Expected: <id>");
            }

            int gameNumber;
            try {
                gameNumber = Integer.parseInt(params[0]);
            } catch (NumberFormatException e) {
                return "Error: Game Number must be a number.";
            }

            if (gameNumber <= 0) {
                return "Error: Game Number must be greater than 0";
            }

            Integer gameID = gameList.get(gameNumber);
            if (gameID == null) {
                return "Error: Invalid game number. Please list the games again and choose a valid number";
            }

            // TODO: how to grab actual board
            GameData gameData = serverFacade.joinGame(gameNumber);
            ChessBoard board = gameData.game().getBoard();
            board.resetBoard();
            ChessBoardUI.drawChessBoard(System.out, board, true);
            // TODO: notification -> observer’s name is observing the game.
            // WebSocket connection with the server (using the /ws endpoint) so it can send and receive gameplay messages.
            // Send a CONNECT WebSocket message to the server.
            Repl.state = State.GAMEPLAY;

            return String.format("Observing game %d", gameNumber);

        } catch (ResponseException ex) {
            throw new ResponseException(ex.getMessage());
        }
    }

    public String help() {
        return """
                logout - when you're done
                list - games
                create <NAME> - a game
                play <ID> [WHITE][BLACK] - a game
                observe <ID> - a game
                quit - playing chess
                help - with possible commands
                """;
    }

    private void assertSignedIn() throws ResponseException {
        System.out.println(Repl.state);
        if (Repl.state == State.SIGNEDOUT) {
            throw new ResponseException("You must sign in");
        }
    }
}
