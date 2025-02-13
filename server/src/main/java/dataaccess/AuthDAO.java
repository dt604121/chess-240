package dataaccess;

import exception.DataAccessException;
import model.AuthData;

public interface AuthDAO{
    AuthData getAuthToken(String authToken) throws DataAccessException ;
    AuthDAO deleteAuth(AuthData) throws DataAccessException;
    void clearAuthDAO() throws DataAccessException;
    AuthDAO addAuthToken(AuthData authData);
}
