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
        return null;
    }

    @Override
    public UserData createUser(String username, String password, String email) throws DataAccessException {
        return new UserData(username, password, email);
    }

    public void addUser(UserData userData) throws DataAccessException{
        userDataTable.put(userData.username(), userData);
    }

    @Override
    public void clearUserDAO() throws DataAccessException{
        userDataTable.clear();
    }
}
