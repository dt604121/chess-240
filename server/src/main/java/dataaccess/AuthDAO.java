package dataaccess;

import model.AuthData;
import server.ResponseException;

public interface AuthDAO{
    getAuthToken(String authToken) throws ResponseException ;

    deleteAuth(AuthData) throws ResponseException;

    void clearAuthDAO() throws ResponseException;
}
