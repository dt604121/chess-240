package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import exception.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import exception.UnauthorizedException;
import model.*;

import javax.xml.crypto.Data;
import java.util.Objects;
import java.util.UUID;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }
    public RegisterResult registerService(RegisterRequest registerRequest) throws DataAccessException {
        return userDAO.register(registerRequest);
    }

    public AuthData createAndSaveAuthToken(String username) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, username);
        authDAO.addAuthToken(authData);
        return authData;
    }

    public LoginResult login(LoginRequest loginRequest)
            throws DataAccessException, UnauthorizedException {
        UserData userData = userDAO.getUser(loginRequest.username());
        // check password
        if (Objects.equals(loginRequest.password(), userData.password())){
            AuthData authData = createAndSaveAuthToken(userData.username());
            return new LoginResult(userData.username(), authData.authToken());
        }
        else {
            throw new UnauthorizedException("unauthorized");
        }
    }

    public LoginResult loginService(LoginRequest loginRequest, MemoryUserDAO memoryUserDAO, MemoryAuthDAO memoryAuthDAO)
            throws DataAccessException, UnauthorizedException {
        return login(loginRequest);
    }

    public void logout(LogoutRequest logoutRequest) throws DataAccessException {
        userDAO.logout(logoutRequest);
    }

    public void clearUserDAOService() throws DataAccessException {
        userDAO.clearUserDAO();
    }

    public void clearAuthDAOService() throws DataAccessException {
        authDAO.clearAuthDAO();
    }
}
