package dataaccess;

import exception.DataAccessException;
import model.*;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    // Key: Username Value: UserDataRecord
    final private HashMap<String, UserData> userDataTable = new HashMap<>();

    void clear(UserData u) throws DataAccessException {}

    @Override
    public UserData getUser(String username) throws DataAccessException{
        if (userDataTable.containsValue(username)){
            return userDataTable.get(username);
        }
        throw new DataAccessException("No user found.");
    }

    @Override
    public UserData createUser(Object UserData) throws DataAccessException {
        return null;
    }

    @Override
    public void clearUserDAO() throws DataAccessException {
        return;
    }

    @Override
    public void logout(LogoutRequest logoutRequest, MemoryUserDAO memoryUserDAO, MemoryAuthDAO memoryAuthDAO) throws DataAccessException {

    }

    @Override
    public LoginResult login(LoginRequest loginRequest, MemoryUserDAO memoryUserDAO, MemoryAuthDAO memoryAuthDAO)
            throws DataAccessException {
        memoryAuthDAO.createAuthToken(loginRequest, memoryUserDAO, memoryAuthDAO);
    }

    @Override
    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
        return null;
    }
}
