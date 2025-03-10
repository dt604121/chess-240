package ui;

import service.GameService;

import java.util.Scanner;

import static java.awt.Color.BLUE;
import static java.awt.Color.GREEN;
import static ui.EscapeSequences.RESET;

public class Repl {
    private final PostLoginClient postLoginClient;
    private final PreLoginClient preLoginClient;
    private final GamePlayClient gamePlayClient;

    public Repl(String serverUrl) {
        postLoginClient = new postLoginClient(serverUrl, this);
        preLoginClient = new preLoginClient(serverUrl, this);
        gamePlayClient = new gamePlayClient(serverUrl, this);
    }

    public void run() {
        System.out.println("\uD83D\uDC36 Welcome to the pet store. Sign in to start.");
        System.out.print(preLoginClient.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = preLoginClient.eval(line);
                System.out.print(BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + RESET + ">>> " + GREEN);
    }

}
