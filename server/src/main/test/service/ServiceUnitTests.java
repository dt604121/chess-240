package service;

import exception.DataAccessException;
import dataaccess.MemoryUserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ServiceUnitTests {
    static final UserService service = new UserService(new MemoryUserDAO());
    // TODO: implement tests
    @BeforeEach
    void clear() throws DataAccessException {
        service.clearUserDAO();
    }
        }
    // Login
    // make UserDAO and call login()
    // positive: empty
    // negative: username is taken
}
