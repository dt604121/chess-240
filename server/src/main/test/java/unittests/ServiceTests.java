package java.unittests;

import dataaccess.MemoryUserDAO;
import exception.DataAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.UserService;

public class ServiceTests {
    public class ServiceUnitTests2 {
        static final UserService service = new UserService(new MemoryUserDAO());
        @BeforeEach
        void clear() throws DataAccessException {
            service.clearUserDAOService();
        }
    }

    // Login
    @Test
    void loginTest() throws DataAccessException {
        // positive: empty -> assertTrue
        var loginResult = userService.loginService(loginRequest, memoryUserDAO, memoryAuthDAO);
        // negative: username is taken -> assertThrows
    }
}

}
