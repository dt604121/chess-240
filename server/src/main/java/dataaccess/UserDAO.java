package dataaccess;

import exception.DataAccessException;
import model.*;

public interface UserDAO {
    UserData getUser(String username) throws DataAccessException;
    UserData createUser(Object UserData) throws DataAccessException;
    void clearUserDAO() throws DataAccessException;
    RegisterResult register(RegisterRequest registerRequest) throws DataAccessException;
}