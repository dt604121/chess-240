package ui;

import websocket.messages.Notification;

import java.util.Scanner;

import static java.awt.Color.RED;
import static ui.EscapeSequences.RESET;
import ui.websocket.NotificationHandler;

public class Repl implements NotificationHandler{
    private final PostLoginClient postLoginClient;
    private final PreLoginClient preLoginClient;
    private final GamePlayClient gamePlayClient;
    public static State state = State.SIGNEDOUT;

    public Repl(String serverUrl) {
        postLoginClient = new PostLoginClient(serverUrl, this);
        preLoginClient = new PreLoginClient(serverUrl, this);
        gamePlayClient = new GamePlayClient(serverUrl, this);
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
                    result = postLoginClient.eval(line);
                    if (result.equals("You have signed out. Come back soon!")) {
                        state = State.SIGNEDOUT;
                    }
                }
                else if (state == State.GAMEPLAY) {
                    result = gamePlayClient.eval(line);
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

    public void notify(Notification notification) {
        System.out.println(RED + notification.getMessage());
        printPrompt();
    }
}