package ui;
import exception.ResponseException;

import java.util.Arrays;

public class GamePlayClient {
    private final ServerFacade serverFacade;
    private State state = State.SIGNEDIN;
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
//                case "move" -> movePiece();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception ex) {
            throw new ResponseException(401, ex.getMessage());
        }
    }

    public String help() {
        return """
                move - a piece 
                quit - playing chess
                help - with possible commands
                """;
    }
}
