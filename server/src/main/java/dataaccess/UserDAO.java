package dataaccess;

import model.UserData;
import server.ResponseException;

public interface UserDAO {
    UserData getUser(String username) throws ResponseException;
    UserData createUser(Object UserData) throws ResponseException;
    String createAuthToken(Object AuthData) throws ResponseException;
    void clearUserDAO() throws ResponseException;
}
