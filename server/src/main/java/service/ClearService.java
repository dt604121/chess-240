package service;

import dataaccess.dao.AuthDAO;
import dataaccess.dao.UserDAO;
import dataaccess.dao.GameDAO;
import exception.DataAccessException;

public class ClearService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public ClearService(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO){
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public void clearService() throws DataAccessException {
        userDAO.clearUserDAO();
        authDAO.clearAuthDAO();
        gameDAO.clearGameDAO();
    }
}
