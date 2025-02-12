package dataaccess;

import static dataaccess.MemoryUserDAO.createAuthToken;
import model.AuthData;

public class MemoryAuthDAO implements AuthDAO {
    public AuthDAO getAuthToken(String authToken){
        createAuthToken(AuthData);
    }

    public AuthDAO deleteAuth(AuthData){
        return null;
    }

    public void clearAuthDAO(){
    }

}
