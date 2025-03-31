package websocket.messages;

public class Error extends ServerMessage {
    public Error(ServerMessageType type) {
        super(type);
    }

    // This message is sent to a client when it sends an invalid command. The message must include the word Error.
}
