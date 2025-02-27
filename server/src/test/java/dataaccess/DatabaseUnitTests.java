package dataaccess;

import dataaccess.sql.SQLUserDAO;
import dataaccess.sql.SQLGameDAO;
import dataaccess.sql.SQLAuthDAO;
import exception.DataAccessException;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.*;
import org.junit.jupiter.api.BeforeEach;

import java.util.Collection;

public class DatabaseUnitTests {
    SQLGameDAO sqlGameDAO = new SQLGameDAO();
    SQLUserDAO sqlUserDAO = new SQLUserDAO();
    SQLAuthDAO sqlAuthDAO = new SQLAuthDAO();

    @BeforeEach
    void setUP() throws DataAccessException {
        // Clear the DAO's
        sqlGameDAO.clearGameDAO();
        sqlAuthDAO.clearAuthDAO();
        sqlUserDAO.clearUserDAO();
        // TODO: add psuedo data here for testing
        // assert that it's not null as well...
    }

    //region Game DAO Tests
    @Test
    void listGamesPositiveTest() throws DataAccessException {

    }

    void listGamesNegativeTest() throws DataAccessException {

    }

    void getGamePositiveTest() throws DataAccessException {

    }

    void getGameNegativeTest() throws DataAccessException {

    }

    void clearGameDAOPositiveTest() throws DataAccessException {

    }

    void updateGamePositiveTest() throws DataAccessException {

    }

    void updateGameNegativeTest() throws DataAccessException {

    }

    void addGamePositiveTest() {

    }

    void addGameNegativeTest()  {

    }
    // endregion

    // region User DAO Tests
    void getUserPositiveTest() throws DataAccessException {

    }

    void getUserNegativeTest() throws DataAccessException {

    }

    void createUserPositiveTest() throws DataAccessException {

    }

    void createUserNegativeTest() throws DataAccessException {

    }

    void clearUserDAOPositiveTest() throws DataAccessException {

    }

    void addUserPositiveTest() throws DataAccessException {

    }

    void addUserNegativeTest() throws DataAccessException {

    } // endregion

    // region Auth DAO Tests
    void getAuthTokenPositiveTest() throws DataAccessException {

    }

    void getAuthTokenNegativeTest() throws DataAccessException {

    }

    void deleteAuthTokenPositiveTest() throws DataAccessException {

    }

    void deleteAuthTokenNegativeTest() throws DataAccessException {

    }

    void clearAuthDAOPositiveTest() throws DataAccessException {

    }

    void addAuthTokenPositiveTest() throws DataAccessException {

    }

    void addAuthTokenNegativeTest() throws DataAccessException {

    }
    // endregion
}
