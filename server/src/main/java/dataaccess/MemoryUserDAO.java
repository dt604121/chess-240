package dataaccess;

import model.*;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    // Key: Username Value: UserDataRecord
    final private HashMap<String, UserData> userDataTable = new HashMap<>();

    @Override
    public UserData getUser(String username) {
        if (userDataTable.containsKey(username)){
            return userDataTable.get(username);
        }
        return null;
    }

    @Override
    public UserData createUser(String username, String password, String email) {
        return new UserData(username, password, email);
    }

    @Override
    public void addUser(UserData userData){
        userDataTable.put(userData.username(), userData);
    }

    @Override
    public void clearUserDAO() {
        userDataTable.clear();
    }
}
