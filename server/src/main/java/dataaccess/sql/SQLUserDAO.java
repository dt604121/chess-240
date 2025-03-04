package dataaccess.sql;

import dataaccess.DatabaseManager;
import dataaccess.dao.UserDAO;
import exception.DataAccessException;
import model.UserData;

import java.sql.SQLException;

import static java.sql.Types.NULL;

public class SQLUserDAO implements UserDAO {

    public SQLUserDAO() throws DataAccessException, SQLException {
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
                            return new UserData(
                                    rs.getString("username"),
                                    rs.getString("password"),
                                    rs.getString("email")
                            );
                    }
                } catch (SQLException e) {
                    throw new SQLException(e);
                }
                return null;
            }
        }
    }

    @Override
    public UserData createUser(String username, String password, String email) throws DataAccessException {
        var statement = "INSERT INTO UserData (username, password, email) VALUES (?, ?, ?, ?)";
        var userData = new UserData(username, password, email);
        executeUpdate(statement, username, password, email);
        return userData;
    }

    @Override
    public void clearUserDAO() throws DataAccessException {
        var statement = "TRUNCATE UserData";
        executeUpdate(statement);
    }

    @Override
    public void addUser(UserData userData) throws DataAccessException {
        var statement = "INSERT INTO UserData (username, password, email) VALUES (?, ?, ?, ?)";
        executeUpdate(statement, userData.username(), userData.password(), userData.email());
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
              `username` VARCHAR(256) NOT NULL PRIMARY KEY,
              `password` VARCHAR(256) NOT NULL,
              `email` VARCHAR(256) NOT NULL,
            ) 
            
            CREATE TABLE IF NOT EXISTS GameData (
              `authToken` VARCHAR(256) NOT NULL,
              `username` VARCHAR(256) NOT NULL,
            ) 
            
            CREATE TABLE IF NOT EXISTS AuthData (
              `gameID` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
              `whiteUsername` VARCHAR(256),
              `blackUsername` VARCHAR(256),
              `gameName` VARCHAR(256) NOT NULL,
              `game` TEXT NOT NULL
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
