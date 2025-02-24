package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import exception.DataAccessException;
import exception.UnauthorizedException;
import model.*;

import java.util.Objects;
import java.util.UUID;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }
    public RegisterResult registerService(RegisterRequest registerRequest) throws DataAccessException,
            AlreadyTakenException, BadRequestException {
        // null field(s)
        if (registerRequest.password() == null | registerRequest.username() == null | registerRequest.email() == null){
            throw new BadRequestException("unauthorized");
        }
        // username exists already
        UserData existingUser = userDAO.getUser(registerRequest.username());
        if (existingUser != null) {
            throw new AlreadyTakenException("Username already taken");
        }
        // new user
        UserData userData = userDAO.createUser(registerRequest.username(), registerRequest.password(),
                registerRequest.email());
        userDAO.addUser(userData);
        AuthData authData = createAndSaveAuthToken(userData.username());
        return new RegisterResult(userData.username(), authData.authToken());
    }

    public AuthData createAndSaveAuthToken(String username) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, username);
        authDAO.addAuthToken(authData);
        return authData;
    }

    public LoginResult loginService(LoginRequest loginRequest) throws DataAccessException, UnauthorizedException {
        UserData userData = userDAO.getUser(loginRequest.username());
        // check password
        if (userData == null){
            throw new UnauthorizedException("unauthorized");
        }

        if (Objects.equals(loginRequest.password(), userData.password())){
            AuthData authData = createAndSaveAuthToken(userData.username());
            return new LoginResult(userData.username(), authData.authToken());
        }
        throw new UnauthorizedException("unauthorized");
    }

    public void logoutService(String authToken) throws DataAccessException, UnauthorizedException {
        if (authDAO.getAuthToken(authToken) != null){
            authDAO.deleteAuth(authToken);
        }
        else {
            throw new UnauthorizedException("unauthorized");
        }
    }
}
