package dataaccess;

import chess.ChessGame;
import dataaccess.sql.SQLUserDAO;
import dataaccess.sql.SQLGameDAO;
import dataaccess.sql.SQLAuthDAO;
import exception.DataAccessException;
import exception.SQLException;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.*;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseUnitTests {
    SQLGameDAO sqlGameDAO = new SQLGameDAO();
    SQLUserDAO sqlUserDAO = new SQLUserDAO();
    SQLAuthDAO sqlAuthDAO = new SQLAuthDAO();

    public DatabaseUnitTests() throws DataAccessException {
    }

    // TODO: tests implemented correctly (based on pets?)

    @BeforeEach
    void setUP() throws DataAccessException {
        // Clear the DAO's
        sqlGameDAO.clearGameDAO();
        sqlAuthDAO.clearAuthDAO();
        sqlUserDAO.clearUserDAO();
    }

    //region Game DAO Tests
    @Test
    void listGamesPositiveTest() throws DataAccessException {
        GameData gameData = new GameData(1234, "whiteUsername", "blackUsername",
                "gameName", new ChessGame());

        assertNotNull(sqlGameDAO.listGames(gameData));
    }

    @Test
    void listGamesNegativeTest() throws DataAccessException {
        // testing null input
        GameData gameData = new GameData(0, null, null,
                null, null);

        assert(sqlGameDAO.listGames(gameData).isEmpty());
    }

    @Test
    void getGamePositiveTest() throws DataAccessException {
        GameData gameData = new GameData(1234, "whiteUsername", "blackUsername",
                "gameName", new ChessGame());
        sqlGameDAO.addGame(gameData);
        assertNotNull(sqlGameDAO.getGame(1234));
    }

    @Test
    void getGameNegativeTest() throws DataAccessException {
        // mismatching gameID
        GameData gameData = new GameData(1234, "whiteUsername", "blackUsername",
                "gameName", new ChessGame());
        sqlGameDAO.addGame(gameData);
        assertNull(sqlGameDAO.getGame(123));
    }

    @Test
    void clearGameDAOPositiveTest() throws DataAccessException {
        GameData gameData = new GameData(1234, "whiteUsername", "blackUsername",
                "gameName", new ChessGame());

        sqlGameDAO.addGame(gameData);
        sqlGameDAO.clearGameDAO();

        assertNull(sqlGameDAO.getGame(1234), "GameDAO should be emtpy after clearing");
    }

    @Test
    void updateGamePositiveTest() throws DataAccessException {
        GameData gameData = new GameData(1234, "whiteUsername", "blackUsername",
                "gameName", new ChessGame());
        sqlGameDAO.updateGame(gameData);

        assertEquals((gameData.gameID()), 1234);
    }

    @Test
    void updateGameNegativeTest() throws DataAccessException {
        // null data
        GameData gameData = new GameData(0, null, null,
                null, null);

        sqlGameDAO.updateGame(gameData);

        assertNotEquals((gameData.gameID()), 0);
    }

    @Test
    void addGamePositiveTest() throws DataAccessException {
        GameData gameData = new GameData(1234, "whiteUsername", "blackUsername",
                "gameName", new ChessGame());
        sqlGameDAO.addGame(gameData);

        assertEquals((gameData.gameID()), 1234);
    }

    @Test
    void addGameNegativeTest() throws DataAccessException {
        // null data
        GameData gameData = new GameData(0, null, null,
                null, null);

        sqlGameDAO.addGame(gameData);

        assertNotEquals((gameData.gameID()), 0);
    }
    // endregion

    // region User DAO Tests
    @Test
    void getUserPositiveTest() throws DataAccessException, SQLException {
        UserData userData = new UserData("username", "password", "email.com");

        assertNotNull(sqlUserDAO.getUser("username"));
        assertEquals(userData.username(), "username");
    }

    @Test
    void getUserNegativeTest() throws DataAccessException {
        UserData userData = new UserData("username", "password", "email.com");

        assertNull(sqlUserDAO.getUser("Danica"));
        assertNotEquals(userData.username(), "Danica");
    }

    @Test
    void createUserPositiveTest() throws DataAccessException {
        sqlUserDAO.createUser("username", "password", "email.com");
        assertNotNull(sqlUserDAO.getUser("username"));
    }

    @Test
    void createUserNegativeTest() throws DataAccessException {
        assertNull(sqlUserDAO.createUser(null, null, null));
    }

    @Test
    void clearUserDAOPositiveTest() throws DataAccessException {
        UserData userData = new UserData("clearUser", "clear", "clearUser@email");
        sqlUserDAO.addUser(userData);
        sqlUserDAO.clearUserDAO();

        assertNull(sqlUserDAO.getUser("clearUser"), "UserDAO should be empty after clear.");
    }

    @Test
    void addUserPositiveTest() throws DataAccessException {
        UserData userData = new UserData("username", "password", "email.com");

        sqlUserDAO.addUser(userData);
        assertEquals(userData.username(), "username");
        assertEquals(userData.password(), "password");
        assertEquals(userData.email(), "email.com");
    }

    @Test
    void addUserNegativeTest() throws DataAccessException {
        UserData userData = new UserData(null, null, null);

        sqlUserDAO.addUser(userData);
        assertNull(userData.username(), userData.email());
    } // endregion

    // region Auth DAO Tests
    @Test
    void getAuthTokenPositiveTest() throws DataAccessException {
        AuthData authData = new AuthData("1234", "username");
        sqlAuthDAO.addAuthToken(authData);
        assertNotNull(sqlAuthDAO.getAuthToken("1234"));
    }
    @Test
    void getAuthTokenNegativeTest() throws DataAccessException {
        AuthData authData = new AuthData(null, "username");
        sqlAuthDAO.addAuthToken(authData);
        assertNotNull(sqlAuthDAO.getAuthToken(null));
    }

    @Test
    void deleteAuthTokenPositiveTest() throws DataAccessException {
        AuthData authData = new AuthData("1234", "username");
        sqlAuthDAO.addAuthToken(authData);
        sqlAuthDAO.deleteAuth("1234");
        assertNull(sqlAuthDAO.getAuthToken("1234"));
    }

    @Test
    void deleteAuthTokenNegativeTest() throws DataAccessException {
        AuthData authData = new AuthData(null, "username");
        sqlAuthDAO.addAuthToken(authData);
        sqlAuthDAO.deleteAuth(null);
        assertNull(sqlAuthDAO.getAuthToken(null));
    }

    @Test
    void clearAuthDAOPositiveTest() throws DataAccessException {
        AuthData authData = new AuthData("1234", "username");
        sqlAuthDAO.addAuthToken(authData);
        sqlAuthDAO.clearAuthDAO();

        // throws error if the data in the DAO still exists
        assertNull(sqlAuthDAO.getAuthToken("1234"),
                "AuthDAO should be empty after clear.");
    }

    @Test
    void addAuthTokenPositiveTest() throws DataAccessException {
        AuthData authData = new AuthData("1234", "username");
        sqlAuthDAO.addAuthToken(authData);
        assertEquals(authData.authToken(), "1234");
    }

    @Test
    void addAuthTokenNegativeTest() throws DataAccessException {
        AuthData authData = new AuthData(null, "username");
        sqlAuthDAO.addAuthToken(authData);
        assertNull(authData.authToken());
    }
    // endregion
}
