package dataaccess;

import model.*;
import org.eclipse.jetty.util.log.Log;
import server.ResponseException;

public interface UserDAO {
    UserData getUser(String username) throws ResponseException;
    UserData createUser(Object UserData) throws ResponseException;
    String createAuthToken(Object AuthData) throws ResponseException;
    void clearUserDAO() throws ResponseException;
    void logout(LogoutRequest logoutRequest) throws ResponseException;
    LoginResult login(LoginRequest loginRequest) throws ResponseException;
    RegisterResult register(RegisterRequest registerRequest) throws ResponseException;
}
