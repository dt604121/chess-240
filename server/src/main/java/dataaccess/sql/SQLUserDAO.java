package dataaccess.sql;

import com.google.gson.Gson;
import dataaccess.DatabaseManager;
import dataaccess.dao.UserDAO;
import exception.DataAccessException;
import model.UserData;

import java.sql.SQLException;

import static java.sql.Types.NULL;

public class SQLUserDAO implements UserDAO {

    public SQLUserDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public UserData getUser(String username) throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "Select username, json FROM UserData WHERE username = ?";

            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        var json = rs.getString("json");
                        return new Gson().fromJson(json, UserData.class)
                    }
                } catch (SQLException e) {
                    throw new SQLException(e);
                }
            }
        }
    }

    @Override
    public UserData createUser(String username, String password, String email) throws DataAccessException {
        return null;
    }

    @Override
    public void clearUserDAO() throws DataAccessException {

    }

    // add pet?
    @Override
    public void addUser(UserData userData) throws DataAccessException {

    }

    public static int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    switch (param) {
                        case String p -> ps.setString(i + 1, p);
                        case Integer p -> ps.setInt(i + 1, p);
                        case null -> ps.setNull(i + 1, NULL);
                        default -> {
                        }
                    }
                }
                ps.executeUpdate();
                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException("could not execute update");
        }
    }

    private final String[] createStatements = {
            // UserData, GameData, AuthData
            """
            CREATE TABLE IF NOT EXISTS UserData (
              `username` varchar(256) NOT NULL PRIMARY KEY,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
            ) 
            
            CREATE TABLE IF NOT EXISTS GameData (
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
            ) 
            
            CREATE TABLE IF NOT EXISTS AuthData (
              `gameID` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
              `whiteUsername` varchar(256),
              `blackUsername` varchar(256),
              `gameName` varchar(256) NOT NULL,
              `game` TEXT NOT NULL,
              )
            """
    };

    private void configureDatabase() throws DataAccessException, SQLException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                } catch (SQLException ex) {
                    throw new DataAccessException("Couldn't configure the database. Error executing statement: \" +" +
                            "statement + \" | \" + e.getMessage()");
                }
            }
        }
    }
}
