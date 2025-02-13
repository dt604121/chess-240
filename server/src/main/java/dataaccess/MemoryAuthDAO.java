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
//
//    public AuthDAO deleteAuth(AuthData authData){
//        return authDataHashMap.remove(authData);
//    }

    public AuthDAO addAuthToken(AuthData authData){
        return authDataHashMap.put(authData);
    }

    public void clearAuthDAO(){
        authDataHashMap.clear();
    }

}
