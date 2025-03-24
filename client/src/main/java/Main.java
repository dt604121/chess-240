import chess.*;
import exception.ResponseException;
import ui.Repl;
import ui.ServerFacade;

public class Main {
    public static void main(String[] args) {
        ServerFacade serverFacade = new ServerFacade("https://localhost:8080");
        try {
            serverFacade.clear();
        } catch (ResponseException e) {
            System.out.println("Error with clearing the server facade.");
        }
        Repl repl = new Repl("https://localhost:8080");
        repl.run();
    }
}