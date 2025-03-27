package ui;
import exception.ResponseException;

import java.util.Arrays;

public class GamePlayClient {
    private final ServerFacade serverFacade;
    private final String serverUrl;

    public GamePlayClient(String serverUrl){
        serverFacade = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;

    }

    public String eval(String input) throws ResponseException {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0 ) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "move" -> movePiece();
                case "redraw" -> redrawBoard();
                case "leave" -> leaveGame();
                case "resign" -> resignGame();
                case "highlight" -> highlightMoves();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception ex) {
            throw new ResponseException(ex.getMessage());
        }
    }

    public String movePiece() throws ResponseException{
        try {
            return "";
        } catch (Exception e) {
            throw new ResponseException("" + e.getMessage())
        }
    }

    public String redrawBoard() throws ResponseException {
        try {
            return "";
        } catch (Exception e) {
            throw new ResponseException("" + e.getMessage())
        }
    }

    private String leaveGame() throws ResponseException {
        try {
            return "";
        } catch (Exception e) {
            throw new ResponseException("" + e.getMessage())
        }
    }

    private String resignGame() throws ResponseException {
        try {
            return "";
        } catch (Exception e) {
            throw new ResponseException("" + e.getMessage())
        }
    }

    private String highlightMoves() throws ResponseException {
        try {
            return "";
        } catch (Exception e) {
            throw new ResponseException("" + e.getMessage())
        }
    }

    public String help() {
        return """
                move - a piece
                redraw - chess board
                leave - game
                resign - from the game
                highlight - legal moves
                quit - playing chess
                help - with possible commands
                """;
    }
}
