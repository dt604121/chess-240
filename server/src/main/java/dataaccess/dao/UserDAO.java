package dataaccess.dao;

import exception.DataAccessException;
import model.*;

public interface UserDAO {
    UserData getUser(String username) throws DataAccessException;
    UserData createUser(String username, String password, String email) throws DataAccessException;
    void clearUserDAO() throws DataAccessException;
    void addUser(UserData userData) throws DataAccessException;
}