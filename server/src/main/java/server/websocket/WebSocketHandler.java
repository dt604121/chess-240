package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.*;
import dataaccess.dao.*;
import exception.*;
import model.GameData;
import sharedexceptions.ResponseException;
import websocket.messages.*;
import model.AuthData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import websocket.commands.*;
import websocket.messages.Error;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebSocket
public class WebSocketHandler {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    boolean isObserver;

    private final ConnectionManager connections = new ConnectionManager();
    private static final Gson GSON = new Gson();

    private final Map<Integer, Session> gameSessions = new HashMap<>();

    public WebSocketHandler(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        try {
            JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
            String typeString = jsonObject.get("commandType").getAsString();
            UserGameCommand.CommandType type = UserGameCommand.CommandType.valueOf(typeString);
            UserGameCommand command;

            switch (type) {
                case CONNECT -> command = GSON.fromJson(message, Connect.class);
                case MAKE_MOVE -> command = GSON.fromJson(message, MakeMove.class);
                case LEAVE -> command = GSON.fromJson(message, Leave.class);
                case RESIGN -> command = GSON.fromJson(message, Resign.class);
                default -> throw new IllegalArgumentException("Unknown command type: " + type);
            }

            String username = getUsername(command.getAuthToken());
            int gameId = command.getGameID();
            GameData gameData = gameDAO.getGame(gameId);

            String blackUsername = gameData.blackUsername();
            String whiteUsername = gameData.whiteUsername();

            if (username.equals(blackUsername) || username.equals(whiteUsername)) {
                 isObserver = false;
            }

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, (Connect) command);
                case MAKE_MOVE -> makeMove(session, username, (MakeMove) command);
                case LEAVE -> leaveGame(session, username, (Leave) command);
                case RESIGN -> resign(session, username, (Resign) command);
            }

        } catch (UnauthorizedException ex) {
            connections.sendsMessage(session, new websocket.messages.Error("Error: Unauthorized"));
        } catch (Exception ex) {
            ex.printStackTrace();
            connections.sendsMessage(session, new websocket.messages.Error("Error " + ex.getMessage()));
        }
    }

    private void connect(Session session, String username, Connect command) throws IOException, DataAccessException {
        int gameId = command.getGameID();
        connections.add(gameId, username, session);

        GameData gameData = gameDAO.getGame(gameId);

        if (gameData == null) {
            Error errorMessage = new Error("Error: invalid gameId");
            connections.sendsMessage(session, errorMessage);
            return;
        }

        ChessGame game = gameData.game();
        ChessGame.TeamColor color = game.getTeamTurn();
        if (game.isGameOver()){
            Error errorMessage = new Error("Error: game is over.");
            connections.sendsMessage(session, errorMessage);
            return;
        }

        String message;
        if (isObserver){
            message = String.format("%s is observing the chess game now.", username);
        }
        else {
            message = String.format("%s has joined the chess game as %s", username, color == ChessGame.TeamColor.WHITE ? "black" : "white");
        }
        ServerMessage notification = new Notification(message);
        connections.broadcast(gameId, notification, username);

        LoadGame loadGameMessage = new LoadGame(gameData.game());
        connections.sendsMessage(session, loadGameMessage);
    }

    private void leaveGame(Session session, String username, Leave command) throws IOException, DataAccessException {
        try {
            int gameId = command.getGameID();
            GameData gameData = gameDAO.getGame(gameId);

            if (gameData == null) {
                Error errorMessage = new Error("Error: invalid gameId");
                connections.sendsMessage(session, errorMessage);
                return;
            }

            connections.remove(gameId, username);
            ChessGame game = gameData.game();

            String whiteUsername = gameData.whiteUsername();
            String blackUsername = gameData.blackUsername();

            if (whiteUsername != null && whiteUsername.equals(username)) {
                whiteUsername = null;
            } else if (gameData.blackUsername() != null && blackUsername.equals(username)) {
                blackUsername = null;
            }

            GameData updatedGameData = new GameData(
                    gameData.gameID(),
                    whiteUsername,
                    blackUsername,
                    gameData.gameName(),
                    gameData.game()
            );

            gameDAO.updateGame(updatedGameData);

            if (game.isGameOver()){
                Error errorMessage = new Error("Error: game is over.");
                connections.sendsMessage(session, errorMessage);
                return;
            }

            var message = String.format("%s left the chess game.", username);
            Notification notification = new Notification(message);
            connections.broadcast(gameId, notification, username);
        } catch (Exception ex) {
        connections.sendsMessage(session, new Error("Error: " + ex.getMessage()));
        }
    }

    private void resign(Session session, String username, Resign command) throws IOException, DataAccessException {
        try {
            int gameId = command.getGameID();
            GameData gameData = gameDAO.getGame(gameId);

            if (gameData == null) {
                Error errorMessage = new Error("Error: invalid gameId");
                connections.sendsMessage(session, errorMessage);
                return;
            }

            if (isObserver) {
                Error errorMessage = new Error("Error: observer cannot resign");
                connections.sendsMessage(session, errorMessage);
                return;
            }

            ChessGame game = gameData.game();

            if (game.isGameOver()) {
                connections.sendsMessage(session, new Error("Error: Game is already over"));
                return;
            }

            game.setGameOver(true);

            if (game.isInCheckmate(ChessGame.TeamColor.WHITE) || game.isInCheckmate(ChessGame.TeamColor.BLACK) ||
                    game.isInStalemate(ChessGame.TeamColor.WHITE) || game.isInStalemate(ChessGame.TeamColor.BLACK)) {
                connections.sendsMessage(session, new Error("Error: Game is already over"));
                return;
            }

            if (!username.equals(gameData.whiteUsername()) && !username.equals(gameData.blackUsername())) {
                connections.sendsMessage(session, new Error("Error: Only players can resign"));
                return;
            }

            connections.remove(gameId, username);
            gameDAO.updateGame(gameData);

            var message = String.format("%s resigned from the chess game.", username);
            Notification notification = new Notification(message);
            connections.sendsMessage(session, notification);
            connections.broadcast(gameId, notification, null);
        } catch (Exception ex) {
            connections.sendsMessage(session, new Error("Error: " + ex.getMessage()));
        }
    }

    public void makeMove(Session session, String username, MakeMove command) throws ResponseException, IOException {
        try {
            int gameId = command.getGameID();
            GameData gameData = gameDAO.getGame(gameId);
            if (isObserver) {
                Error errorMessage = new Error("Error: observer cannot make move");
                connections.sendsMessage(session, errorMessage);
                return;
            }
            if (gameData == null) {
                Error errorMessage = new Error("Error: invalid gameId");
                connections.sendsMessage(session, errorMessage);
                return;
            }
            String whiteUsername = gameData.whiteUsername();
            String blackUsername = gameData.blackUsername();
            ChessGame.TeamColor playerColor;
            if (username.equals(whiteUsername)) {
                playerColor = ChessGame.TeamColor.WHITE;
            } else if (username.equals(blackUsername)) {
                playerColor = ChessGame.TeamColor.BLACK;
            } else {
                connections.sendsMessage(session, new Error("Error: Only players can make moves"));
                return;
            }
            if (gameData.game().isGameOver()) {
                connections.sendsMessage(session, new Error("Error: Game is already over"));
                return;
            }

            ChessMove move = command.getMove();

            if (move == null) {
                Error errorMessage = new Error("Error: invalid move");
                connections.sendsMessage(session, errorMessage);
                return;
            }

            if (playerColor != gameData.game().getTeamTurn()) {
                connections.sendsMessage(session, new Error("Error: Not your turn"));
                return;
            }

            gameData.game().makeMove(move);

            if (gameData.game().isInCheckmate(ChessGame.TeamColor.WHITE) || gameData.game().isInCheckmate(ChessGame.TeamColor.BLACK) ||
                    gameData.game().isInStalemate(ChessGame.TeamColor.WHITE) || gameData.game().isInStalemate(ChessGame.TeamColor.BLACK)) {
                gameData.game().setGameOver(true);
            }

            ChessGame.TeamColor opponentColor = gameData.game().getTeamTurn() == ChessGame.TeamColor.BLACK ? ChessGame.TeamColor.BLACK :
                    ChessGame.TeamColor.WHITE;

            String opponentName;
            if (gameData.game().getTeamTurn() == ChessGame.TeamColor.WHITE) {
                opponentName = gameData.blackUsername();
            } else {
                opponentName = gameData.whiteUsername();
            }

            boolean isInCheckmate = gameData.game().isInCheckmate(opponentColor);
            boolean isInCheck = gameData.game().isInCheck(opponentColor);
            boolean isInStalemate = gameData.game().isInStalemate(opponentColor);

            if (isInStalemate) {
                Notification checkmateNotification = new Notification("Players are in stalemate!");
                connections.broadcast(gameId, checkmateNotification, null);
                gameData.game().setGameOver(true);
            }

            else if (isInCheckmate) {
                Notification checkmateNotification = new Notification(String.format("Player %s is in checkmate!", opponentName));
                connections.broadcast(gameId, checkmateNotification, null);
                gameData.game().setGameOver(true);
            }

            else if (isInCheck) {
                Notification checkNotification = new Notification(String.format("Player %s is in check!", opponentName));
                connections.broadcast(gameId, checkNotification, null);
                gameData.game().setGameOver(true);
            }

            gameDAO.updateGame(gameData);

            var message = String.format("%s moved from: %c%d to %c%d", username,
                    (char)('a' + move.getStartPosition().getColumn() - 1), move.getStartPosition().getRow(),
                    (char)('a' + move.getEndPosition().getColumn() - 1), move.getEndPosition().getRow());

            Notification notification = new Notification(message);
            connections.broadcast(gameId, notification, username);

            LoadGame loadGameMessage = new LoadGame(gameData.game());
            connections.broadcast(gameId, loadGameMessage, null);

        } catch (Exception ex) {
            connections.sendsMessage(session, new Error("Error: " + ex.getMessage()));
        }
    }

    private String getUsername(String authToken) throws DataAccessException, UnauthorizedException {
        AuthData authData = authDAO.getAuthToken(authToken);
        if (authData == null) {
            throw new UnauthorizedException("Invalid auth token");
        }
        return authData.username();
    }
}