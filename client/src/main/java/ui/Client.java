package ui;

import websocket.messages.Error;
import websocket.messages.ServerMessage;

public class Client extends ServerMessage {
    public Client(ServerMessageType type) {
        super(type);
    }

    @Override
    public void notify(ServerMessage message) {
        switch (message.getServerMessageType()) {
            case NOTIFICATION -> displayNotification(((NotificationMessage) message).getMessage());
            case ERROR -> displayError(((Error) message).getError());
            case LOAD_GAME -> loadGame(((LoadGameMessage) message).getGame());
        }
    }
}
