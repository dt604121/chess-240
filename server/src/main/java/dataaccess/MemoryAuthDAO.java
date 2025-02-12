package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    final private HashMap<String, AuthData> authDataHashMap = new HashMap<>();
    public AuthDAO deleteAuth(AuthData){
        return authDataHashMap.remove(AuthData);
    }

    @Override
    public String createAuthToken(Object AuthData) {
        return UUID.randomUUID().toString();
    }

    public void clearAuthDAO(){
        authDataHashMap.clear();
    }

}
