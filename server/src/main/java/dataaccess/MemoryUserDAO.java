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
    public UserData createUser(Object UserData) throws DataAccessException {
        return null;
    }

    public void addUser(UserData userData){
        userDataTable.put(userData.username(), userData);
    }

    @Override
    public void clearUserDAO(){
        userDataTable.clear();
    }

    @Override
    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
        return null;
    }
}
