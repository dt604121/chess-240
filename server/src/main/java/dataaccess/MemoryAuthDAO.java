package dataaccess;
import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {
    // Key: AuthToken Value: AuthDataRecord
    final private HashMap<String, AuthData> authDataHashMap = new HashMap<>();

    @Override
    public AuthData getAuthToken(String authToken) {
        return authDataHashMap.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) {
        authDataHashMap.remove(authToken);
    }
    @Override
    public void addAuthToken(AuthData authData) {
        authDataHashMap.put(authData.authToken(), authData);
    }

    @Override
    public void clearAuthDAO() {
        authDataHashMap.clear();
    }
}
