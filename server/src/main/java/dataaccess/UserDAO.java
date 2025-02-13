package dataaccess;

import exception.DataAccessException;
import model.*;

public interface UserDAO {
    UserData getUser(String username) throws DataAccessException;
    UserData createUser(Object UserData) throws DataAccessException;
    void clearUserDAO() throws DataAccessException;
    void logout(LogoutRequest logoutRequest, MemoryUserDAO memoryUserDAO, MemoryAuthDAO memoryAuthDAO) throws DataAccessException;
    UserData checkPassword(String password) throws DataAccessException;
    LoginResult login(LoginRequest loginRequest, MemoryUserDAO memoryUserDAO, MemoryAuthDAO memoryAuthDAO) throws DataAccessException;
    RegisterResult register(RegisterRequest registerRequest) throws DataAccessException;
}
