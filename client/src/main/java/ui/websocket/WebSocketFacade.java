package ui.websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import sharedexceptions.ResponseException;
import websocket.commands.*;
import websocket.messages.Error;
import websocket.messages.LoadGame;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {
    Session session;
    NotificationHandler notificationHandler;
    private ServerMessage serverMessage;

    private void handleMessage(String message) {
        try {
            ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);

            switch (serverMessage.getServerMessageType()) {
                case NOTIFICATION -> {
                    Notification notification = new Gson().fromJson(message, Notification.class);
                    notificationHandler.displayNotification(notification.getMessage());
                }
                case ERROR -> {
                    Error error = new Gson().fromJson(message, Error.class);
                    notificationHandler.displayError(error.getErrorMessage());
                }
                case LOAD_GAME -> {
                    LoadGame loadGame = new Gson().fromJson(message, LoadGame.class);
                    notificationHandler.loadGame(loadGame.getGame());
                }
            }
        } catch (Exception ex) {
            System.err.println("Failed to process message: " + ex.getMessage());
        }
    }

    public WebSocketFacade(String url, NotificationHandler notificationHandler, ServerMessage message) throws ResponseException {
        this.serverMessage = message;
        this.notificationHandler = notificationHandler;

        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler((MessageHandler.Whole<String>) this::handleMessage);
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(ex.getMessage());
        }
    }


    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void makeMove(String authToken, Integer gameId, ChessMove move) throws ResponseException {
        try {
            var action = new MakeMove(authToken, gameId, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ResponseException(ex.getMessage());
        }
    }

    public void enterChess(String authToken, Integer gameId, Connect.PlayerType playerType) throws ResponseException {
        try {
            UserGameCommand action = new Connect(authToken, gameId, playerType);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ResponseException(ex.getMessage());
        }
    }

    public void leaveChess(String authToken, Integer gameId) throws ResponseException {
        try {
            var action = new Leave(authToken, gameId);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
            this.session.close();
        } catch (IOException ex) {
            throw new ResponseException(ex.getMessage());
        }
    }

    public void resignFromChess(String authToken, Integer gameId) {
        try {
            var action = new Resign(authToken, gameId);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

