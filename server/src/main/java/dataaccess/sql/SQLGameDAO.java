package dataaccess.sql;

import com.google.gson.Gson;
import dataaccess.DatabaseManager;
import dataaccess.dao.GameDAO;
import exception.DataAccessException;
import model.AuthData;
import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import static dataaccess.sql.SQLUserDAO.executeUpdate;

public class SQLGameDAO implements GameDAO {
    // list pets
    @Override
    public Collection<GameData> listGames(Object listGamesRequest) throws DataAccessException {
        var result = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gamdID, json FROM GameData";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        var gameID = rs.getInt("gameID");
                        var json = rs.getString("json");
                        var game = new Gson().fromJson(json, GameData.class)
                        result.add(game);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return result;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, json FROM GamdeData WHERE gameID=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        var json = rs.getString("json");
                        return new Gson().fromJson(json, AuthData.class);
                    }
                } catch (SQLException e) {
                    throw new SQLException(e);
                }
                return null;
            }
        }
    }

    @Override
    public void clearGameDAO() throws DataAccessException {
        var statement = "TRUNCATE GameData";
        executeUpdate(statement);
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, json FROM GamdeData WHERE gameID=?";
            try (var preparedStatement = conn.prepareStatement("UPDATE GameData SET gameID=? WHERE gameID=?")) {
                preparedStatement.setInt(1, gameID);
                preparedStatement.executeUpdate(statement);
            }
        }
    }

    @Override
    public void addGame(GameData gameData) {
        var statement = "INSERT INTO GameData (gameID, whiteUsername, blackUsername, gameName, game)";
        executeUpdate(statement, gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(),
                gameData.gameName(), gameData.game());
    }
}
