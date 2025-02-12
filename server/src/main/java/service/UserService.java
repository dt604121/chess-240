package service;

import dataaccess.UserDAO;
import model.*;
import server.ResponseException;

public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO){
        this.userDAO = userDAO;
    }
    public RegisterResult register(RegisterRequest registerRequest) throws ResponseException {
        return userDAO.register(registerRequest);
    }

    public LoginResult login(LoginRequest loginRequest) throws ResponseException {
        return userDAO.login(loginRequest);
    }

    public void logout(LogoutRequest logoutRequest) throws ResponseException {
        userDAO.logout(logoutRequest);
    }

    public void clearUserDAO() throws ResponseException {
        userDAO.clearUserDAO();
    }
}
