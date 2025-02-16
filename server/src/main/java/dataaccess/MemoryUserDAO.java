package dataaccess;

import exception.DataAccessException;
import model.*;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    // Key: Username Value: UserDataRecord
    final private HashMap<String, UserData> userDataTable = new HashMap<>();

    @Override
    public UserData getUser(String username) throws DataAccessException{
        if (userDataTable.containsKey(username)){
            return userDataTable.get(username);
        }
        throw new DataAccessException("No user found.");
    }

    @Override
    public UserData createUser(Object UserData) throws DataAccessException {
        return null;
    }

    public void addUser(UserData userData){
        userDataTable.put(userData.username(), userData);
    }

    @Override
    public void clearUserDAO() throws DataAccessException {
        userDataTable.clear();
    }

    @Override
    public void logout(LogoutRequest logoutRequest) throws DataAccessException {

    }

    @Override
    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
        return null;
    }
}
