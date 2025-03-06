package dataaccess.sql;

import dataaccess.DatabaseManager;
import dataaccess.dao.UserDAO;
import exception.DataAccessException;
import model.UserData;

import java.sql.SQLException;

@SuppressWarnings("SqlNoDataSourceInspection")
public class SQLUserDAO implements UserDAO {
    private final DatabaseManager databaseManager;

    public SQLUserDAO(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "Select username, password, email FROM UserData WHERE username = ?";
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
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("issue with getting the user");
        }
        return null;
    }

    @Override
    public UserData createUser(String username, String password, String email) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "INSERT INTO UserData (username, password, email) VALUES (?, ?, ?)";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, email);
                preparedStatement.executeUpdate();
                return new UserData(username, password, email);
            }
        } catch (SQLException e){
            throw new DataAccessException("couldn't create a user");
        }
    }

    @Override
    public void clearUserDAO() throws DataAccessException {
        var statement = "TRUNCATE UserData";
        DatabaseManager.executeUpdate(statement);
    }

    @Override
    public void addUser(UserData userData) throws DataAccessException {
        var statement = "INSERT INTO UserData (username, password, email) VALUES (?, ?, ?)";
        DatabaseManager.executeUpdate(statement, userData.username(), userData.password(), userData.email());
    }
}
