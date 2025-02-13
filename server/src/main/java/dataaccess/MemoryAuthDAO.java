package dataaccess;

import exception.DataAccessException;
import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    final private HashMap<String, AuthData> authDataHashMap = new HashMap<>();

    @Override
    public AuthData getAuthToken(String authToken) throws DataAccessException {
        return null;
    }

    public AuthDAO deleteAuth(AuthData){
        return authDataHashMap.remove(AuthData);
    }

    public void clearAuthDAO(){
        authDataHashMap.clear();
    }

    @Override
    public String createAuthToken(Object AuthData, MemoryUserDAO memoryUserDAO, MemoryAuthDAO memoryAuthDAO) throws DataAccessException {
        return UUID.randomUUID().toString();
    }

}
