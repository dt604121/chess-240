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
    DatabaseManager databaseManager = new DatabaseManager();
    SQLGameDAO sqlGameDAO = new SQLGameDAO(databaseManager);
    SQLUserDAO sqlUserDAO = new SQLUserDAO(databaseManager);
    SQLAuthDAO sqlAuthDAO = new SQLAuthDAO(databaseManager);

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
        String validAuthToken = "1234";
        assertNotNull(sqlGameDAO.listGames(validAuthToken));
    }

    @Test
    void listGamesNegativeTest() {
        // testing null input
        assertThrows(DataAccessException.class, () -> sqlGameDAO.listGames(null));
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

        assertNull(sqlGameDAO.getGame(1234), "GameDAO should be empty after clearing");
    }

    @Test
    void updateGamePositiveTest() throws DataAccessException {
        GameData gameData = new GameData(1234, "whiteUsername", "blackUsername",
                "gameName", new ChessGame());
        sqlGameDAO.updateGame(gameData);

        assertEquals((gameData.gameID()), 1234);
    }

    @Test
    void updateGameNegativeTest() {
        // null data
        GameData gameData = new GameData(0, null, null,
                null, null);

        assertThrows(DataAccessException.class, () -> sqlGameDAO.updateGame(gameData));
    }

    @Test
    void addGamePositiveTest() throws DataAccessException {
        GameData gameData = new GameData(1234, "whiteUsername", "blackUsername",
                "gameName", new ChessGame());
        sqlGameDAO.addGame(gameData);

        assertEquals((gameData.gameID()), 1234);
    }

    @Test
    void addGameNegativeTest() {
        // null data
        GameData gameData = new GameData(0, null, null,
                null, null);

        assertThrows(DataAccessException.class, () -> sqlGameDAO.addGame(gameData));
    }
    // endregion

    // region User DAO Tests
    @Test
    void getUserPositiveTest() throws DataAccessException {
        UserData userData = new UserData("username", "password", "email.com");
        sqlUserDAO.addUser(userData);

        UserData retrievedUser = sqlUserDAO.getUser("username");
        assertNotNull(retrievedUser);
        assertEquals(userData.email(), retrievedUser.email());
        assertEquals(userData.password(), retrievedUser.password());
        assertEquals(userData.username(), retrievedUser.username());
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
    void createUserNegativeTest() {
        assertThrows(DataAccessException.class, () ->
                sqlUserDAO.createUser("Danica", null, null));
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
    void addUserNegativeTest() {
        UserData userData = new UserData(null, null, null);
        assertThrows(DataAccessException.class, () -> sqlUserDAO.addUser(userData));
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
        assertNull(sqlAuthDAO.getAuthToken("nonexistent_token"));
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
    void addAuthTokenNegativeTest() {
        AuthData authData = new AuthData(null, "username");
        assertThrows(DataAccessException.class, () -> sqlAuthDAO.addAuthToken(authData));
    }
    // endregion
}
