package service;

import exception.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.*;

public class UserService {
    private final MemoryUserDAO memoryUserDAO;

    public UserService(MemoryUserDAO memoryUserDAO){
        this.memoryUserDAO = memoryUserDAO;
    }
    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
        return memoryUserDAO.register(registerRequest);
    }

    public LoginResult loginService(LoginRequest loginRequest, MemoryUserDAO memoryUserDAO, MemoryAuthDAO memoryAuthDAO) throws DataAccessException {
        return memoryUserDAO.login(loginRequest, memoryUserDAO, memoryAuthDAO);
    }

    public void logout(LogoutRequest logoutRequest) throws DataAccessException {
        memoryUserDAO.logout(logoutRequest);
    }

    public void clearUserDAO() throws DataAccessException {
        memoryUserDAO.clearUserDAO();
    }
}
