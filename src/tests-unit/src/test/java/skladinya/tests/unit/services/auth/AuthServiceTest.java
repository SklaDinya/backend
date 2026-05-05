package skladinya.tests.unit.services.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skladinya.domain.exceptions.SklaDinyaException;
import skladinya.domain.models.user.UserCreate;
import skladinya.domain.repositories.UserRepository;
import skladinya.domain.services.AuthService;
import skladinya.domain.services.UserService;
import skladinya.services.auth.AuthServiceImpl;
import skladinya.tests.helper.builder.UserBuilder;
import skladinya.tests.helper.factory.JwtFactory;
import skladinya.tests.helper.factory.UserCreateFactory;
import skladinya.tests.helper.synchronizer.SynchronizerTest;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        var synchronizer = new SynchronizerTest();
        authService = new AuthServiceImpl(userRepository, userService, synchronizer);
    }

    @Test
    void givenValidUsername_whenLogin_thenReturnJwt() {
        var user = UserBuilder.builder().build();
        var login = user.username();
        var password = user.password();
        var token = JwtFactory.create();
        given(userRepository.getByUsername(login)).willReturn(Optional.of(user));
        given(userService.generateToken(user)).willReturn(token);

        var result = authService.login(login, password);

        assertEquals(token, result);
        verify(userRepository, never()).getByEmail(any());
    }

    @Test
    void givenValidEmail_whenLogin_thenReturnJwt() {
        var user = UserBuilder.builder().build();
        var login = user.email();
        var password = user.password();
        var token = JwtFactory.create();
        given(userRepository.getByUsername(login)).willReturn(Optional.empty());
        given(userRepository.getByEmail(login)).willReturn(Optional.of(user));
        given(userService.generateToken(user)).willReturn(token);

        var result = authService.login(login, password);

        assertEquals(token, result);
    }

    @Test
    void givenInvalidLogin_whenLogin_thenThrowsException() {
        var user = UserBuilder.builder().build();
        var login = user.email();
        var password = user.password();
        given(userRepository.getByUsername(login)).willReturn(Optional.empty());
        given(userRepository.getByEmail(login)).willReturn(Optional.empty());

        assertThrows(SklaDinyaException.class, () -> authService.login(login, password));

        verify(userService, never()).generateToken(any());
    }

    @Test
    void givenInvalidPassword_whenLogin_thenThrowsException() {
        var user = UserBuilder.builder().build();
        var login = user.email();
        var password = UUID.randomUUID().toString();
        given(userRepository.getByUsername(login)).willReturn(Optional.of(user));

        assertThrows(SklaDinyaException.class, () -> authService.login(login, password));

        verify(userRepository, never()).getByEmail(any());
        verify(userService, never()).generateToken(any());
    }

    @Test
    void givenValidData_whenRegister_thenReturnJwt() {
        var user = UserBuilder.builder().build();
        var form = UserCreateFactory.create(user);
        var token = JwtFactory.create();
        given(userService.create(form)).willReturn(user);
        given(userService.generateToken(user)).willReturn(token);

        var result = authService.register(form);

        assertEquals(token, result);
    }

    @Test
    void givenInvalidData_whenRegister_thenThrowException() {
        var form = UserCreateFactory.empty();
        willThrow(SklaDinyaException.conflict("")).given(userService).create(form);

        assertThrows(SklaDinyaException.class, () -> authService.register(form));

        verify(userService, never()).generateToken(any());
    }
}
