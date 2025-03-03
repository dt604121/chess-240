package dataaccess.sql;

import dataaccess.dao.AuthDAO;
import exception.DataAccessException;
import model.AuthData;

public class SQLAuthDAO implements AuthDAO {
    @Override
    public AuthData getAuthToken(String authToken) throws DataAccessException {
        return null;
    }

    // delete pet
    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        var statement = "DELETE FROM authToken WHERE authToken=?";
        SQLUserDAO.executeUpdate(statement, authToken);
    }

    @Override
    public void clearAuthDAO() throws DataAccessException {

    }

    @Override
    public void addAuthToken(AuthData authData) throws DataAccessException {

    }
}
