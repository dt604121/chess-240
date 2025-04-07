package ui.websocket;

public interface NotificationHandler {

default void displayNotification(String message) {
    System.out.println("Notification: " + message);
}

default void displayError(String error) {
    System.err.println("Error: " + error);
}

void loadGame(Object game);
}
