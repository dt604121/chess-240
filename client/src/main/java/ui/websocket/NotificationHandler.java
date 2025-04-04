package ui.websocket;

public interface NotificationHandler {

default void displayNotification(String message) {
    System.out.println("Notification: " + message);
}

default void displayError(String error) {
    System.err.println("Error: " + error);
}

// TODO: implement loadGame
default void loadGame(Object game) {
    System.out.println("Loading game state...");
    // Update the UI with the game state
}
}
