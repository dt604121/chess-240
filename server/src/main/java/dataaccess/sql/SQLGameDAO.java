package dataaccess.sql;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DatabaseManager;
import dataaccess.dao.GameDAO;
import exception.DataAccessException;
import java.sql.SQLException;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class SQLGameDAO implements GameDAO {
    private final DatabaseManager databaseManager;

    public SQLGameDAO(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }
    @Override
    public Collection<GameData> listGames(Object listGamesRequest) throws DataAccessException {
        var result = new ArrayList<GameData>();
        String authToken = (String) listGamesRequest;
        if (listGamesRequest == null || authToken.isBlank()) {
            throw new DataAccessException("Error: authToken cannot be null or empty");
        }
        try (var conn = DatabaseManager.getConnection()) {
            // How are we getting the authToken from listGamesRequest?
            var statement = "SELECT gameID, whiteusername, blackusername, gameName, game FROM GameData " +
                    "WHERE ? IN (SELECT authToken FROM AuthData);";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        var gameID = rs.getInt("gameID");
                        var whiteusername = rs.getString("whiteusername");
                        var blackusername = rs.getString("blackusername");
                        var gameName = rs.getString("gameName");
                        var json = rs.getString("game");
                        var game = new Gson().fromJson(json, ChessGame.class);
                        result.add(new GameData(gameID, whiteusername, blackusername, gameName, game));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return result;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteusername, blackusername, gameName, game FROM GameData WHERE gameID=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                                var json = rs.getString("game");
                                var game = new Gson().fromJson(json, ChessGame.class);
                                return new GameData(
                                rs.getInt("gameID"),
                                rs.getString("whiteusername"),
                                rs.getString("blackusername"),
                                rs.getString("gameName"),
                                game
                                );
                    }
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void clearGameDAO() throws DataAccessException {
        var statement = "TRUNCATE GameData";
        DatabaseManager.executeUpdate(statement);
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {
        if (Objects.isNull(gameData) || Objects.isNull(gameData.whiteUsername()) ||
                Objects.isNull(gameData.blackUsername()) || Objects.isNull(gameData.gameName()) ||
                Objects.isNull(gameData.game())) {
            throw new DataAccessException("Error: username, password, and email cannot be null");
        }
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("UPDATE GameData SET whiteusername=?," +
                    "blackusername=?, gameName=?, game=? WHERE gameID=?")) {
                var json = new Gson().toJson(gameData.game());
                preparedStatement.setString(1, gameData.whiteUsername());
                preparedStatement.setString(2, gameData.blackUsername());
                preparedStatement.setString(3, gameData.gameName());
                preparedStatement.setString(4, json);
                preparedStatement.setInt(5, gameData.gameID());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void addGame(GameData gameData) throws DataAccessException  {
        if (Objects.isNull(gameData) || Objects.isNull(gameData.whiteUsername()) ||
                Objects.isNull(gameData.blackUsername()) || Objects.isNull(gameData.gameName()) ||
                Objects.isNull(gameData.game())) {
            throw new DataAccessException("Error: username, password, and email cannot be null");
        }
        var statement = "INSERT INTO GameData (gameID, whiteUsername, blackUsername, gameName, game)" +
                "VALUES (?, ?, ?, ?, ?)";
        var json = new Gson().toJson(gameData);
        DatabaseManager.executeUpdate(statement, gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(),
                gameData.gameName(), json);
    }
}
