package dataaccess.sql;

import com.google.gson.Gson;
import dataaccess.DatabaseManager;
import dataaccess.dao.AuthDAO;
import exception.DataAccessException;
import model.AuthData;

import java.sql.SQLException;

import static dataaccess.sql.SQLUserDAO.executeUpdate;

public class SQLAuthDAO implements AuthDAO {
    @Override
    public AuthData getAuthToken(String authToken) throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, json FROM AuthData WHERE authToken=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
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
    public void deleteAuth(String authToken) throws DataAccessException {
        var statement = "DELETE FROM authToken WHERE authToken = ?";
        executeUpdate(statement, authToken);
    }

    @Override
    public void clearAuthDAO() throws DataAccessException {
        var statement = "TRUNCATE AuthData";
        executeUpdate(statement);
    }

    @Override
    public void addAuthToken(AuthData authData) throws DataAccessException {
        var statement = "INSERT INTO AuthData (authToken, username) VALUES (?, ?)";
        executeUpdate(statement, authData.authToken(), authData.username());
    }
}
