package dataaccess.sql;

import com.google.gson.Gson;
import dataaccess.DatabaseManager;
import dataaccess.dao.GameDAO;
import exception.DataAccessException;
import exception.SQLException;
import model.AuthData;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

public class SQLGameDAO implements GameDAO {
    @Override
    public Collection<GameData> listGames(Object listGamesRequest) throws DataAccessException {
        var result = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            // How are we getting the authToken from listGamesRequest?
            // TODO: look into this...
            var statement = "SELECT gamdID, whiteusername, blackusername, gameName, game, json FROM GameData WHERE authToken = ? ";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        return new GameData(
                                rs.getInt("gameID"),
                                rs.getString("whiteusername"),
                                rs.getString("blackusername"),
                                rs.getString("gameName"),
                                rs.getObject("game")
                        );
                        return null;
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
            var statement = "SELECT gameID, whiteusername, blackusername, gameName, game, json FROM GamdeData WHERE gameID=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new GameData(
                                rs.getString("whiteusername"),
                                rs.getString("blackusername"),
                                rs.getString("gameName"),
                                rs.getObject("game"),
                                rs.getString("json")
                        );
                    }
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("issue getting the game");
        }
    }

    @Override
    public void clearGameDAO() throws DataAccessException {
        var statement = "TRUNCATE GameData";
        DatabaseManager.executeUpdate(statement);
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
    public void addGame(GameData gameData) throws DataAccessException  {
        var statement = "INSERT INTO GameData (gameID, whiteUsername, blackUsername, gameName, game)";
        DatabaseManager.executeUpdate(statement, gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(),
                gameData.gameName(), gameData.game());
    }
}
