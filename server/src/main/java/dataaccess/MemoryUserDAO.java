package dataaccess;

import exception.DataAccessException;
import model.*;
import org.eclipse.jetty.server.Authentication;

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
    public UserData checkPassword(String password) throws DataAccessException {
        if (userDataTable.containsValue(password)){
            return userDataTable.get(password);
        }
        throw new DataAccessException("No password found.");
    }

    @Override
    public LoginResult login(LoginRequest loginRequest, MemoryUserDAO memoryUserDAO, MemoryAuthDAO memoryAuthDAO)
            throws DataAccessException {
        UserData username = getUser(loginRequest.username());
        UserData password = checkPassword(loginRequest.password());
        // How are we grabbing the authData?
        String authToken = memoryAuthDAO.createAuthToken(authData, memoryUserDAO, memoryAuthDAO);
        return new LoginResult(username, authToken);
    }

    @Override
    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
        return null;
    }
}
