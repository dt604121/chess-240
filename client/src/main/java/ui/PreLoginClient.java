package ui;

import java.util.Arrays;

import exception.ResponseException;
import model.LoginRequest;
import model.UserData;

public class PreLoginClient {
    private final ServerFacade serverFacade;
    private final String serverUrl;

    public PreLoginClient(String serverUrl) {
        serverFacade = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    // scan in the input and then call the correct function
    public String eval(String input) {
        Repl.state = State.SIGNEDOUT;
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> login(params);
                case "register" -> register(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws ResponseException{
        try {
            if (params.length != 3) {
                return "Error. Expected: <name> <password> <email>";
            }

            var name = params[0].trim();
            var password = params[1].trim();
            var email = params[2].trim();

            boolean nameIsEmail = name.contains("@");
            boolean passwordIsEmail = password.contains("@");
            boolean emailIsEmail = email.contains("@");

            if (nameIsEmail && !emailIsEmail) {
                return "Error: It looks like you entered the email first. " +
                        "The correct order is: <username> <password> <email>.";
            }

            if (!nameIsEmail && passwordIsEmail) {
                return "Error: It looks like you swapped the password and email. " +
                        "The correct order is: <username> <password> <email>.";
            }

            if (name.isEmpty() || password.isEmpty() || email.isEmpty()) {
                return "Error: name, password, and email cannot be empty";
            }

            if (isUserLoggedIn()) {
                throw new ResponseException("Already connected");
            }

            var user = new UserData(name, password, email);

            serverFacade.registerUser(user);

            Repl.state = State.SIGNEDIN;

            return String.format("You registered as %s.", name);
        } catch (ResponseException e) {
            String message = e.getMessage().toLowerCase();
            if (message.contains("already taken")) {
                return "Error: That username is already taken. Please choose another.";
            } else if (message.contains("invalid email")) {
                return "Error: The email format is incorrect.";
            } else {
                return "Registration failed: " + e.getMessage();
            }
        } catch (Exception e) {
            return "Unexpected error occurred during registration." + e.getMessage();
        }
    }

    public String login(String... params) throws ResponseException {
        try {
            if (params.length != 2) {
                return "Error. Expected <name> <password>";
            }
            var name = params[0].trim();
            var password = params[1].trim();

            if (name.isEmpty() || password.isEmpty()) {
                return "Error: name, and password cannot be empty";
            }

            if (isUserLoggedIn()) {
                throw new ResponseException("Already connected");
            }

            var loginRequest = new LoginRequest(name, password);

            serverFacade.loginUser(loginRequest);

            Repl.state = State.SIGNEDIN;

            return String.format("You logged in as %s", name);
        } catch (ResponseException e) {
            throw new ResponseException("Login failed: " + e.getMessage());
        } catch (Exception e) {
            return "Unexpected error occurred during login." + e.getMessage();
        }
    }

    public String help() {
        return """
                login <USERNAME> <PASSWORD> - to play chess
                register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                quit - playing chess
                help - with possible commands
                """;
    }

    public boolean isUserLoggedIn() {
        return Repl.state == State.SIGNEDIN;
    }
}
