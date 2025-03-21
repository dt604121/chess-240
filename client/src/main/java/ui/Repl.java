package ui;

import java.util.Scanner;

import static ui.EscapeSequences.RESET;

public class Repl {
    private final PostLoginClient postLoginClient;
    private final PreLoginClient preLoginClient;
    private final GamePlayClient gamePlayClient;
    private final State state = State.SIGNEDOUT;

    public Repl(String serverUrl) {
        postLoginClient = new PostLoginClient(serverUrl);
        preLoginClient = new PreLoginClient(serverUrl);
        gamePlayClient = new GamePlayClient(serverUrl);
    }

    public void run() {
        System.out.println("\uDC36 Welcome to Chess240! Type help to get started");
        System.out.print(preLoginClient.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();
            try {
                if (state == State.SIGNEDOUT){
                    result = preLoginClient.eval(line);
                }
                else if (state == State.SIGNEDIN) {
                    result = postLoginClient.eval(line);
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
        System.out.print("\n" + RESET + ">>> ");
    }

    public static class Main {
        public static void main(String[] args) {
            String serverUrl = "http://localhost:8080";
            Repl repl = new Repl(serverUrl);
            repl.run();
        }
    }
}