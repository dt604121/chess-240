package ui.websocket;

import com.google.gson.Gson;
import exception.ResponseException;
import websocket.commands.*;
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

    public WebSocketFacade(String url, NotificationHandler notificationHandler, ServerMessage message) throws ResponseException {
        this.serverMessage = message;
        this.notificationHandler = notificationHandler;

        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage message = new Gson().fromJson(message, ServerMessage.class);
                    notificationHandler.notify(message);
                } catch (Exception ex) {
                    notificationHandler.notify(new Error(ex.getMessage()))
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(ex.getMessage());
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void enterChess(String visitorName) throws ResponseException {
        try {
            var action = new Connect(Connect, visitorName);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ResponseException(ex.getMessage());
        }
    }

    public void leaveChess(String visitorName) throws ResponseException {
        try {
            var action = new Leave(Leave, visitorName);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
            this.session.close();
        } catch (IOException ex) {
            throw new ResponseException(ex.getMessage());
        }
    }

}

