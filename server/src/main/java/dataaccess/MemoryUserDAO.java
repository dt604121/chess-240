package dataaccess;

import model.UserData;


import java.util.UUID;

public class MemoryUserDAO implements UserDAO {

    void clear(UserData u) throws DataAccessException {}

    // TODO: implement Data Access Methods
    @Override
    public UserData getUser(String username) {
        return null;
    }

    public UserData createUser(Object UserData){
        return null;
    }

    public static String createAuthToken(Object AuthData) {
        return UUID.randomUUID().toString();
    }

    public void clearUserDAO(){

    }
}
