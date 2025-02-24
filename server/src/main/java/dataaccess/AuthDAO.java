package dataaccess;

import exception.DataAccessException;
import model.AuthData;

public interface AuthDAO{
    AuthData getAuthToken(String authToken) throws DataAccessException ;
    void deleteAuth(String authToken) throws DataAccessException;
    void clearAuthDAO() throws DataAccessException;
    void addAuthToken(AuthData authData) throws DataAccessException;
}
