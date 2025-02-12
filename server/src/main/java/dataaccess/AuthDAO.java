package dataaccess;

import exception.DataAccessException;
import model.AuthData;

public interface AuthDAO{
    AuthData getAuthToken(String authToken) throws ResponseException ;

    AuthData deleteAuth(AuthData) throws ResponseException;

    void clearAuthDAO() throws ResponseException;
    String createAuthToken(Object AuthData, MemoryUserDAO memoryUserDAO, MemoryAuthDAO memoryAuthDAO) throws DataAccessException;
}
