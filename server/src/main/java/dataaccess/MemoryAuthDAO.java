package dataaccess;

import exception.DataAccessException;
import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {
    // Key: AuthToken Value: AuthDataRecord
    final private HashMap<String, AuthData> authDataHashMap = new HashMap<>();

    @Override
    public AuthData getAuthToken(String authToken) throws DataAccessException {
        return authDataHashMap.get(authToken);
    }

    public void deleteAuth(AuthData authData){
        authDataHashMap.remove(authData.authToken(), authData);
    }

    public void addAuthToken(AuthData authData){
        authDataHashMap.put(authData.authToken(), authData);
    }

    public void clearAuthDAO(){
        authDataHashMap.clear();
    }
}
