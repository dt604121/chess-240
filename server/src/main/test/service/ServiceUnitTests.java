package service;

import dataaccess.UserDAO;
import exception.DataAccessException;
import dataaccess.MemoryUserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ServiceUnitTests {
    static final UserService service = new UserService(new MemoryUserDAO());
    // TODO: implement tests
    @BeforeEach
    void clear() throws DataAccessException {
        service.clearUserDAOService();
    }
        }
        
    // Login
    @Test
    void loginTest() throws DataAccessException {
        // positive: empty -> assertTrue
        // negative: username is taken -> assertThrows
    }
}
