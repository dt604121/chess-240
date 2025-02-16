package dataaccess;

import exception.DataAccessException;
import model.AuthData;

public interface AuthDAO{
    AuthData getAuthToken(String authToken) throws DataAccessException ;
    void deleteAuth(AuthData authData) throws DataAccessException;
    void clearAuthDAO() throws DataAccessException;
    void addAuthToken(AuthData authData) throws DataAccessException;
}
