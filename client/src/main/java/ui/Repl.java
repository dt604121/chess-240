package ui;

import java.util.Scanner;

import static ui.EscapeSequences.RESET;

public class Repl {
    private final PostLoginClient postLoginClient;
    private final PreLoginClient preLoginClient;
    private final GamePlayClient gamePlayClient;
    private State state = State.SIGNEDOUT;

    public Repl(String serverUrl) {
        postLoginClient = new PostLoginClient(serverUrl);
        preLoginClient = new PreLoginClient(serverUrl);
        gamePlayClient = new GamePlayClient(serverUrl);
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
                    if (result.startsWith("You logged in as")) {
                        state = State.SIGNEDIN;
                    }
                }
                else if (state == State.SIGNEDIN) {
                    result = postLoginClient.eval(line);
                    if (result.equals("You have signed out. Come back soon!")) {
                        state = State.SIGNEDOUT;
                    }
                }
                else if (state == State.GAMEPLAY) {
                    result = gamePlayClient.eval(line);
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
}