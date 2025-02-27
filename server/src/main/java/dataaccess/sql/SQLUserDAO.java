package dataaccess.sql;

import dataaccess.dao.UserDAO;
import exception.DataAccessException;
import model.UserData;

public class SQLUserDAO implements UserDAO {
    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public UserData createUser(String username, String password, String email) throws DataAccessException {
        return null;
    }

    @Override
    public void clearUserDAO() throws DataAccessException {

    }

    @Override
    public void addUser(UserData userData) throws DataAccessException {

    }
}
