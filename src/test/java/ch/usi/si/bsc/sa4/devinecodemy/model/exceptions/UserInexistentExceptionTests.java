package ch.usi.si.bsc.sa4.devinecodemy.model.exceptions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@DisplayName("The UserInexistentException")
public class UserInexistentExceptionTests {

    UserInexistentException userInexistentException;

    @BeforeEach
    void setup() {
        userInexistentException = new UserInexistentException();
    }

    @Test
    @DisplayName("should be created after calling the constructor")
    public void testCreate() {
        assertNotNull(userInexistentException,
                "object is null after calling the constructor");
        assertNotNull(userInexistentException.getMessage(),
                "message is null after calling the constructor");
    }


    @Test
    @DisplayName("should be created after using user-id as a parameter")
    public void testCreateWithNumber() {
        userInexistentException = new UserInexistentException("an id");
        assertNotNull(userInexistentException,
                "object is null after calling the constructor");
        assertNotNull(userInexistentException.getMessage(),
                "message is null after calling the constructor");
    }
}
