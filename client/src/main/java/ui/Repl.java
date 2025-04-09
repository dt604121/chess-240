package ui;

import chess.ChessGame;
import ui.websocket.NotificationHandler;

import java.util.Scanner;

import static ui.EscapeSequences.RESET;

public class Repl implements NotificationHandler{
    private final PostLoginClient postLoginClient;
    private final PreLoginClient preLoginClient;
    private final GamePlayClient gamePlayClient;
    public static State state = State.SIGNEDOUT;
    public static String currentUsername = null;
    public String authToken;

    public Repl(String serverUrl) {
        ServerFacade serverFacade = new ServerFacade(serverUrl);
        postLoginClient = new PostLoginClient(serverFacade, serverUrl);
        preLoginClient = new PreLoginClient(serverFacade, serverUrl);
        gamePlayClient = new GamePlayClient(serverFacade, serverUrl, this);
    }

    public void run() {
        System.out.println("Welcome to Chess240! Type help to get started");
        System.out.print(preLoginClient.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();
            try {
                if (state == State.SIGNEDOUT){
                    result = preLoginClient.eval(line);
                    if (result.startsWith("You logged in as") || result.startsWith("You registered as")) {
                        state = State.SIGNEDIN;
                    }
                }
                else if (state == State.SIGNEDIN) {
                    String authToken = preLoginClient.getAuthToken();
                    postLoginClient.setAuthToken(authToken);

                    result = postLoginClient.eval(line);
                    if (result.startsWith("You have joined the game as")) {
                            gamePlayClient.initializeGame(
                                    authToken,
                                    postLoginClient.getGameID(),
                                    postLoginClient.getColor(),
                                    postLoginClient.getGameData(),
                                    postLoginClient.getPlayerType()
                            );
                        state = State.GAMEPLAY;
                    }
                    if (result.equals("You have signed out. Come back soon!")) {
                        state = State.SIGNEDOUT;
                    }
                }
                else if (state == State.GAMEPLAY) {
                    result = gamePlayClient.eval(line);
                    gamePlayClient.setAuthToken(authToken);
                    if (result.equals("You have left the game. Come back soon!") || result.equals("You have resigned from the game.")) {
                        state = State.SIGNEDIN;
                    }
                }
                System.out.print(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + RESET + state + " >>> ");
    }

    @Override
    public void loadGame(ChessGame game) {

    }
}