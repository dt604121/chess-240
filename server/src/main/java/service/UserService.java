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
    public RegisterResult registerService(RegisterRequest registerRequest) throws DataAccessException {
        return memoryUserDAO.register(registerRequest);
    }

    public LoginResult loginService(LoginRequest loginRequest, MemoryUserDAO memoryUserDAO, MemoryAuthDAO memoryAuthDAO) throws DataAccessException {
        return memoryUserDAO.login(loginRequest, memoryUserDAO, memoryAuthDAO);
    }

    public void logoutService(LogoutRequest logoutRequest, MemoryUserDAO memoryUserDAO, MemoryAuthDAO memoryAuthDAO) throws DataAccessException {
        memoryUserDAO.logout(logoutRequest, memoryUserDAO, memoryAuthDAO);
    }

    public void clearUserDAOService() throws DataAccessException {
        memoryUserDAO.clearUserDAO();
    }
}
