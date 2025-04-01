package server.websocket;

import com.google.gson.Gson;
import exception.ResponseException;
import exception.UnauthorizedException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.MakeMove;
import websocket.commands.UserGameCommand;
import websocket.messages.Notification;

import java.io.IOException;
// TODO: this file!

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        try {
            UserGameCommand command = Serializer.fromJson(message, UserGameCommand.class);
            String username = getUsername(command.getAuthToken());

            saveSession(command.getGameID(), session);

            switch (command.getCommandType()) {
                // TODO: write the following methods (have them send the error, loadgame, and notification servermes
                case CONNECT -> connect(session, username, (ConnectCommand) command); // load game.. + notification
                case MAKE_MOVE -> makeMove(session, username, (MakeMoveCommand) command); // load game.. + notification
                case LEAVE -> leaveGame(session, username, (LeaveGameCommand) command);  // notification
                case RESIGN -> resign(session, username, (ResignCommand) command); // notification
            }
        } catch (UnauthorizedException ex) {
            sendsMessage(session.getRemote(), new Error("Error: Unauthorized"));
        } catch (Exception ex) {
            ex.printStackTrace();
            sendsMessage(session.getRemote(), new Error("Error " + ex.getMessage()));
        }
    }

    private void enter(String visitorName, Session session) throws IOException {
        connections.add(visitorName, session);
        var message = String.format("%s has joined the chess game", visitorName);
        var notification = new Notification(Notification.Type.ARRIVAL, message);
        connections.broadcast(visitorName, notification);
    }

    private void exit(String visitorName) throws IOException {
        connections.remove(visitorName);
        var message = String.format("%s left the chess game", visitorName);
        var notification = new Notification(Notification.Type.DEPARTURE, message);
        connections.broadcast(visitorName, notification);
    }

    private void resign(String visitorName) throws IOException {
        connections.remove(visitorName);
        var message = String.format("%s resigned from the chess game", visitorName);
        var notification = new Notification(Notification.Type.RESIGN, message);
        connections.broadcast(visitorName, notification);
    }

    public void makeMove(String player, String move) throws ResponseException {
        try {
            var message = String.format("%s moved from here %s", player, move);
            var notification = new Notification(Notification.Type.MOVE, message);
            connections.broadcast("", notification);
        } catch (Exception ex) {
            throw new ResponseException(ex.getMessage());
        }
    }
}