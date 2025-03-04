package dataaccess.sql;

import dataaccess.DatabaseManager;
import dataaccess.dao.AuthDAO;
import exception.DataAccessException;
import model.AuthData;

import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO {
    @Override
    public AuthData getAuthToken(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, username FROM AuthData WHERE authToken=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new AuthData(
                                rs.getString("authToken"),
                                rs.getString("username")
                        );
                    }
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("issue getting the authToken");
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        // find and delete the row that matches the authToken given
        var statement = "DELETE * FROM AuthData WHERE authToken = ?";
        DatabaseManager.executeUpdate(statement, authToken);
    }

    @Override
    public void clearAuthDAO() throws DataAccessException {
        var statement = "TRUNCATE AuthData";
        DatabaseManager.executeUpdate(statement);
    }

    @Override
    public void addAuthToken(AuthData authData) throws DataAccessException {
        var statement = "INSERT INTO AuthData (authToken, username) VALUES (?, ?)";
        DatabaseManager.executeUpdate(statement, authData.authToken(), authData.username());
    }
}
