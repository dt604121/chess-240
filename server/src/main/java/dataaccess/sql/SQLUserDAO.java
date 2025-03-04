package dataaccess.sql;

import dataaccess.DatabaseManager;
import dataaccess.dao.UserDAO;
import exception.DataAccessException;
import model.UserData;

import java.sql.SQLException;

public class SQLUserDAO implements UserDAO {

   private DatabaseManager configureDB() {
       DatabaseManager database = new DatabaseManager();
       return database.ConfigureDAODatabases();
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
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("issue with getting the user");
        }
    }


    @Override
    public UserData createUser(String username, String password, String email) throws DataAccessException {
        var statement = "INSERT INTO UserData (username, password, email) VALUES (?, ?, ?, ?)";
        var userData = new UserData(username, password, email);
        DatabaseManager.executeUpdate(statement, username, password, email);
        return userData;
    }

    @Override
    public void clearUserDAO() throws DataAccessException {
        var statement = "TRUNCATE UserData";
        DatabaseManager.executeUpdate(statement);
    }

    @Override
    public void addUser(UserData userData) throws DataAccessException {
        var statement = "INSERT INTO UserData (username, password, email) VALUES (?, ?, ?, ?)";
        DatabaseManager.executeUpdate(statement, userData.username(), userData.password(), userData.email());
    }
}
