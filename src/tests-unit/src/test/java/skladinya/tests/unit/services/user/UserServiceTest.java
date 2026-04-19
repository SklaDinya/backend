package skladinya.tests.unit.services.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skladinya.domain.exceptions.SklaDinyaException;
import skladinya.domain.models.user.UserRole;
import skladinya.domain.models.user.UserUpdate;
import skladinya.domain.repositories.OperatorRepository;
import skladinya.domain.repositories.UserRepository;
import skladinya.domain.services.JwtService;
import skladinya.domain.services.UserService;
import skladinya.services.user.UserServiceImpl;
import skladinya.tests.helper.builder.OperatorBuilder;
import skladinya.tests.helper.builder.UserBuilder;
import skladinya.tests.helper.builder.UserUpdateBuilder;
import skladinya.tests.helper.factory.JwtFactory;
import skladinya.tests.helper.factory.UserCreateFactory;
import skladinya.tests.helper.factory.UserSearchFactory;
import skladinya.tests.helper.factory.UserUpdateFactory;
import skladinya.tests.helper.synchronizer.SynchronizerTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OperatorRepository operatorRepository;

    @Mock
    private JwtService jwtService;

    private UserService userService;

    @BeforeEach
    void setUp() {
        var synchronizer = new SynchronizerTest();
        userService = new UserServiceImpl(userRepository, operatorRepository, jwtService, synchronizer);
    }

    @Test
    void givenValidData_whenCreate_thenReturnUser() {
        var user = UserBuilder.builder().build();
        var form = UserCreateFactory.create(user);
        given(userRepository.getByUsername(user.username())).willReturn(Optional.empty());
        given(userRepository.getByEmail(user.email())).willReturn(Optional.empty());
        given(userRepository.create(any())).willReturn(user);

        var result = userService.create(form);

        assertEquals(user, result);
    }

    @Test
    void givenExistsUsername_whenCreate_thenThrowException() {
        var user = UserBuilder.builder().build();
        var form = UserCreateFactory.create(user);
        var found = UserBuilder.builder().username(user.username()).build();
        given(userRepository.getByUsername(user.username())).willReturn(Optional.of(found));

        assertThrows(SklaDinyaException.class, () -> userService.create(form));

        verify(userRepository, never()).getByEmail(any());
        verify(userRepository, never()).create(any());
    }

    @Test
    void givenExistsEmail_whenCreate_thenThrowException() {
        var user = UserBuilder.builder().build();
        var form = UserCreateFactory.create(user);
        var found = UserBuilder.builder().email(user.email()).build();
        given(userRepository.getByUsername(user.username())).willReturn(Optional.empty());
        given(userRepository.getByEmail(user.email())).willReturn(Optional.of(found));

        assertThrows(SklaDinyaException.class, () -> userService.create(form));

        verify(userRepository, never()).create(any());
    }

    @Test
    void givenValidData_whenGetByUserId_thenReturnUser() {
        var user = UserBuilder.builder().build();
        given(userRepository.getByUserId(user.userId())).willReturn(Optional.of(user));

        var result = userService.getByUserId(user.userId());

        assertEquals(user, result);
    }

    @Test
    void givenInvalidUserId_whenGetByUserId_thenThrowException() {
        var userId = UUID.randomUUID();
        given(userRepository.getByUserId(userId)).willReturn(Optional.empty());

        assertThrows(SklaDinyaException.class, () -> userService.getByUserId(userId));
    }

    @Test
    void givenValidData_whenGetAllBySearchOptions_thenReturnUsers() {
        var user1 = UserBuilder.builder().build();
        var user2 = UserBuilder.builder().build();
        var users = List.of(user1, user2);
        var options = UserSearchFactory.create();
        given(userRepository.getAllBySearchOptions(options)).willReturn(users);

        var found = userService.getAllBySearchOptions(options);

        assertEquals(users.size(), found.size());
        for (var i = 0; i < users.size(); ++i) {
            assertEquals(users.get(i), found.get(i));
        }
    }

    @Test
    void givenNotMatchedData_whenGetAllBySearchOptions_thenReturnEmptyList() {
        var options = UserSearchFactory.create();
        given(userRepository.getAllBySearchOptions(options)).willReturn(List.of());

        var found = userService.getAllBySearchOptions(options);

        assertEquals(0, found.size());
    }

    @Test
    void givenValidData_whenUpdate_thenReturnUser() {
        var user = UserBuilder.builder().build();
        var form = UserUpdateBuilder.builder().random().build();
        var updated = UserUpdateFactory.create(user, form);
        given(userRepository.getByUserId(user.userId())).willReturn(Optional.of(user));
        given(userRepository.getByUsername(form.username())).willReturn(Optional.empty());
        given(userRepository.getByEmail(form.email())).willReturn(Optional.empty());
        given(userRepository.update(eq(user.userId()), any())).willReturn(updated);

        var result = userService.update(user.userId(), form);

        assertEquals(updated, result);
    }

    @Test
    void givenEmpty_whenUpdate_thenReturnUser() {
        var user = UserBuilder.builder().build();
        var form = UserUpdateBuilder.builder().build();
        var updated = UserUpdateFactory.create(user, form);
        given(userRepository.getByUserId(user.userId())).willReturn(Optional.of(user));
        given(userRepository.update(eq(user.userId()), any())).willReturn(updated);

        var result = userService.update(user.userId(), form);

        assertEquals(updated, result);
        verify(userRepository, never()).getByUsername(any());
        verify(userRepository, never()).getByEmail(any());
    }

    @Test
    void givenExistentUsername_whenUpdate_thenThrowException() {
        var user = UserBuilder.builder().build();
        var form = UserUpdateBuilder.builder().random().build();
        var found = UserBuilder.builder().username(form.username()).build();
        given(userRepository.getByUserId(user.userId())).willReturn(Optional.of(user));
        given(userRepository.getByUsername(form.username())).willReturn(Optional.of(found));

        assertThrows(SklaDinyaException.class, () -> userService.update(user.userId(), form));

        verify(userRepository, never()).getByEmail(any());
        verify(userRepository, never()).update(any(), any());
    }

    @Test
    void givenExistentEmail_whenUpdate_thenThrowException() {
        var user = UserBuilder.builder().build();
        var form = UserUpdateBuilder.builder().random().build();
        var found = UserBuilder.builder().email(form.email()).build();
        given(userRepository.getByUserId(user.userId())).willReturn(Optional.of(user));
        given(userRepository.getByUsername(form.username())).willReturn(Optional.empty());
        given(userRepository.getByEmail(form.email())).willReturn(Optional.of(found));

        assertThrows(SklaDinyaException.class, () -> userService.update(user.userId(), form));

        verify(userRepository, never()).update(any(), any());
    }

    @Test
    void givenValidData_whenUpdateSelf_thenReturnUser() {
        var user = UserBuilder.builder().build();
        var form = UserUpdateBuilder.builder().random().buildSelf(user.password());
        var updated = UserUpdateFactory.create(user, new UserUpdate(form));
        var token = JwtFactory.create();
        given(userRepository.getByUserId(user.userId())).willReturn(Optional.of(user));
        given(userRepository.getByUsername(form.username())).willReturn(Optional.empty());
        given(userRepository.getByEmail(form.email())).willReturn(Optional.empty());
        given(userRepository.update(eq(user.userId()), any())).willReturn(updated);
        given(jwtService.create(user.userId(), user.role())).willReturn(token);

        var result = userService.updateSelf(user.userId(), form);

        assertEquals(token, result);
    }

    @Test
    void givenEmpty_whenUpdateSelf_thenReturnUser() {
        var user = UserBuilder.builder().build();
        var form = UserUpdateBuilder.builder().buildSelf(null);
        var updated = UserUpdateFactory.create(user, new UserUpdate(form));
        var token = JwtFactory.create();
        given(userRepository.getByUserId(user.userId())).willReturn(Optional.of(user));
        given(userRepository.update(eq(user.userId()), any())).willReturn(updated);
        given(jwtService.create(user.userId(), user.role())).willReturn(token);

        var result = userService.updateSelf(user.userId(), form);

        assertEquals(token, result);
        verify(userRepository, never()).getByUsername(any());
        verify(userRepository, never()).getByEmail(any());
    }

    @Test
    void givenExistentUsername_whenUpdateSelf_thenThrowException() {
        var user = UserBuilder.builder().build();
        var form = UserUpdateBuilder.builder().random().buildSelf(user.password());
        var found = UserBuilder.builder().username(form.username()).build();
        given(userRepository.getByUserId(user.userId())).willReturn(Optional.of(user));
        given(userRepository.getByUsername(form.username())).willReturn(Optional.of(found));

        assertThrows(SklaDinyaException.class, () -> userService.updateSelf(user.userId(), form));

        verify(userRepository, never()).getByEmail(any());
        verify(userRepository, never()).update(any(), any());
        verify(jwtService, never()).create(any(), any());
    }

    @Test
    void givenNoPassword_whenUpdateSelf_thenThrowException() {
        var user = UserBuilder.builder().build();
        var form = UserUpdateBuilder.builder().random().buildSelf(null);
        given(userRepository.getByUserId(user.userId())).willReturn(Optional.of(user));
        given(userRepository.getByUsername(form.username())).willReturn(Optional.empty());

        assertThrows(SklaDinyaException.class, () -> userService.updateSelf(user.userId(), form));

        verify(userRepository, never()).getByEmail(any());
        verify(userRepository, never()).update(any(), any());
        verify(jwtService, never()).create(any(), any());
    }

    @Test
    void givenInvalidPassword_whenUpdateSelf_thenThrowException() {
        var user = UserBuilder.builder().build();
        var form = UserUpdateBuilder.builder().random().buildSelf(UUID.randomUUID().toString());
        given(userRepository.getByUserId(user.userId())).willReturn(Optional.of(user));
        given(userRepository.getByUsername(form.username())).willReturn(Optional.empty());

        assertThrows(SklaDinyaException.class, () -> userService.updateSelf(user.userId(), form));

        verify(userRepository, never()).getByEmail(any());
        verify(userRepository, never()).update(any(), any());
        verify(jwtService, never()).create(any(), any());
    }

    @Test
    void givenExistentEmail_whenUpdateSelf_thenThrowException() {
        var user = UserBuilder.builder().build();
        var form = UserUpdateBuilder.builder().random().buildSelf(user.password());
        var found = UserBuilder.builder().email(form.email()).build();
        given(userRepository.getByUserId(user.userId())).willReturn(Optional.of(user));
        given(userRepository.getByUsername(form.username())).willReturn(Optional.empty());
        given(userRepository.getByEmail(form.email())).willReturn(Optional.of(found));

        assertThrows(SklaDinyaException.class, () -> userService.updateSelf(user.userId(), form));

        verify(userRepository, never()).update(any(), any());
        verify(jwtService, never()).create(any(), any());
    }

    @Test
    void givenValidData_whenGenerateToken_thenReturnToken() {
        var user = UserBuilder.builder().build();
        var token = JwtFactory.create();
        given(jwtService.create(user.userId(), user.role())).willReturn(token);

        var result = userService.generateToken(user);

        assertEquals(token, result);
        verify(jwtService).update(user.userId());
    }

    @Test
    void givenOperator_whenGenerateToken_thenReturnToken() {
        var user = UserBuilder.builder().role(UserRole.StorageOperator).build();
        var operator = OperatorBuilder.builder().user(user).build();
        var token = JwtFactory.create();
        given(operatorRepository.getByUserId(user.userId())).willReturn(Optional.of(operator));
        given(jwtService.createStorageOperator(user.userId(), operator.storageId(), operator.role())).willReturn(token);

        var result = userService.generateToken(user);

        assertEquals(token, result);
        verify(jwtService).update(user.userId());
    }

    @Test
    void givenNoOperator_whenGenerateToken_thenReturnToken() {
        var user = UserBuilder.builder().role(UserRole.StorageOperator).build();
        given(operatorRepository.getByUserId(user.userId())).willReturn(Optional.empty());

        assertThrows(SklaDinyaException.class, () -> userService.generateToken(user));

        verify(jwtService, never()).update(any());
        verify(jwtService, never()).createStorageOperator(any(), any(), any());
    }
}
