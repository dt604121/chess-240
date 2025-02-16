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

    @Override
    public void deleteAuth(AuthData authData) throws DataAccessException{
        authDataHashMap.remove(authData.authToken(), authData);
    }
    @Override
    public void addAuthToken(AuthData authData) throws DataAccessException{
        authDataHashMap.put(authData.authToken(), authData);
    }

    @Override
    public void clearAuthDAO() throws DataAccessException{
        authDataHashMap.clear();
    }
}
