package dataaccess;

import exception.DataAccessException;
import model.AuthData;

public interface AuthDAO{
    AuthData getAuthToken(String authToken) throws DataAccessException ;
    AuthDAO deleteAuth(AuthData) throws DataAccessException;
    void clearAuthDAO() throws DataAccessException;
    String createAuthToken(Object AuthData, MemoryUserDAO memoryUserDAO, MemoryAuthDAO memoryAuthDAO) throws DataAccessException;
}
