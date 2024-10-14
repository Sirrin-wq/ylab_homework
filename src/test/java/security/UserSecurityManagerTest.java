package security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.ylab.habittracker.entity.User;
import ru.ylab.habittracker.security.UserSecurityManager;
import ru.ylab.habittracker.service.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserSecurityManagerTest {
    private UserService userService;
    private UserSecurityManager userSecurityManager;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        userSecurityManager = new UserSecurityManager(userService);
    }

    @Test
    @DisplayName("Should register a user successfully")
    void testRegisterUser() {
        String name = "Test User";
        String email = "test@example.com";
        String password = "password";

        userSecurityManager.registerUser(name, email, password);

        verify(userService).addUser(name, email, password);
    }

    @Test
    @DisplayName("Should login a user successfully with correct credentials")
    void testLoginUserSuccess() {
        String email = "test@example.com";
        String password = "password";
        User user = new User("Test User", email, password);
        when(userService.getByEmail(email)).thenReturn(user);

        User loggedInUser = userSecurityManager.loginUser(email, password);

        assertEquals(user, loggedInUser);
        assertEquals(user, UserSecurityManager.currentUser);
    }
}

