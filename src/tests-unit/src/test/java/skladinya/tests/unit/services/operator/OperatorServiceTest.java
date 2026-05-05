package skladinya.tests.unit.services.operator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skladinya.domain.exceptions.SklaDinyaException;
import skladinya.domain.models.operator.OperatorCreate;
import skladinya.domain.models.operator.OperatorSearchOptions;
import skladinya.domain.models.operator.OperatorUpdate;
import skladinya.domain.models.user.UserRole;
import skladinya.domain.repositories.OperatorRepository;
import skladinya.domain.services.OperatorService;
import skladinya.domain.services.UserService;
import skladinya.services.operator.OperatorServiceImpl;
import skladinya.tests.helper.builder.OperatorBuilder;
import skladinya.tests.helper.builder.UserBuilder;
import skladinya.tests.helper.synchronizer.SynchronizerTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class OperatorServiceTest {

    @Mock
    private OperatorRepository operatorRepository;

    @Mock
    private UserService userService;

    private OperatorService operatorService;

    @BeforeEach
    void setUp() {
        var synchronizer = new SynchronizerTest();
        operatorService = new OperatorServiceImpl(operatorRepository, userService, synchronizer);
    }

    @Test
    void givenValidData_whenCreate_thenReturnOperator() {
        var user = UserBuilder.builder().role(UserRole.StorageOperator).build();
        var operator = OperatorBuilder.builder().user(user).build();
        given(userService.create(any())).willReturn(user);
        given(operatorRepository.create(any())).willReturn(operator);
        var form = new OperatorCreate(
                user.username(),
                user.password(),
                user.name(),
                user.email(),
                operator.role()
        );

        var result = operatorService.create(operator.storageId(), form, user.banned());

        assertEquals(operator, result);
    }

    @Test
    void givenExistsUsername_whenCreate_thenThrowException() {
        var user = UserBuilder.builder().role(UserRole.StorageOperator).build();
        var operator = OperatorBuilder.builder().user(user).build();
        willThrow(SklaDinyaException.conflict("")).given(userService).create(any());
        var form = new OperatorCreate(
                user.username(),
                user.password(),
                user.name(),
                user.email(),
                operator.role()
        );

        assertThrows(SklaDinyaException.class, () -> operatorService.create(operator.storageId(), form, user.banned()));

        verify(operatorRepository, never()).create(any());
    }

    @Test
    void givenValidData_whenGetByOperatorId_thenReturnOperator() {
        var user = UserBuilder.builder().role(UserRole.StorageOperator).build();
        var operator = OperatorBuilder.builder().user(user).build();
        given(operatorRepository.getByOperatorId(operator.operatorId())).willReturn(Optional.of(operator));

        var result = operatorService.getByOperatorId(operator.storageId(), operator.operatorId());

        assertEquals(operator, result);
    }

    @Test
    void givenInvalidOperatorId_whenGetByOperatorId_thenThrowException() {
        var storageId = UUID.randomUUID();
        var operatorId = UUID.randomUUID();
        given(operatorRepository.getByOperatorId(operatorId)).willReturn(Optional.empty());

        assertThrows(SklaDinyaException.class, () -> operatorService.getByOperatorId(storageId, operatorId));
    }

    @Test
    void givenInvalidStorageId_whenGetByOperatorId_thenThrowException() {
        var user = UserBuilder.builder().role(UserRole.StorageOperator).build();
        var operator = OperatorBuilder.builder().user(user).build();
        var storageId = UUID.randomUUID();
        given(operatorRepository.getByOperatorId(operator.operatorId())).willReturn(Optional.of(operator));

        assertThrows(SklaDinyaException.class, () -> operatorService.getByOperatorId(storageId, operator.operatorId()));
    }

    @Test
    void givenValidData_whenGetByUserId_thenReturnOperator() {
        var user = UserBuilder.builder().role(UserRole.StorageOperator).build();
        var operator = OperatorBuilder.builder().user(user).build();
        given(operatorRepository.getByUserId(user.userId())).willReturn(Optional.of(operator));

        var result = operatorService.getByUserId(operator.storageId(), user.userId());

        assertEquals(operator, result);
    }

    @Test
    void givenInvalidOperatorId_whenGetByUserId_thenThrowException() {
        var storageId = UUID.randomUUID();
        var userId = UUID.randomUUID();
        given(operatorRepository.getByUserId(userId)).willReturn(Optional.empty());

        assertThrows(SklaDinyaException.class, () -> operatorService.getByUserId(storageId, userId));
    }

    @Test
    void givenInvalidStorageId_whenGetByUserId_thenThrowException() {
        var user = UserBuilder.builder().role(UserRole.StorageOperator).build();
        var operator = OperatorBuilder.builder().user(user).build();
        var storageId = UUID.randomUUID();
        given(operatorRepository.getByUserId(user.userId())).willReturn(Optional.of(operator));

        assertThrows(SklaDinyaException.class, () -> operatorService.getByUserId(storageId, user.userId()));
    }

//    List<Operator> getAllBySearchOptions(UUID storageId, OperatorSearchOptions options);

    @Test
    void givenValidData_whenGetAllBySearchOptions_thenReturnOperators() {
        var storageId = UUID.randomUUID();
        var operator1 = OperatorBuilder.builder().storageId(storageId).build();
        var operator2 = OperatorBuilder.builder().storageId(storageId).build();
        var list = List.of(operator1, operator2);
        var options = new OperatorSearchOptions(null, null, null, null, 50, 0);
        given(operatorRepository.getAllBySearchOptions(storageId, options)).willReturn(list);

        var result = operatorService.getAllBySearchOptions(storageId, options);

        assertEquals(list.size(), result.size());
        for (var i = 0; i < list.size(); ++i) {
            assertEquals(list.get(i), result.get(i));
        }
    }

    @Test
    void givenNoOperators_whenGetAllBySearchOptions_thenReturnEmptyList() {
        var storageId = UUID.randomUUID();
        var options = new OperatorSearchOptions(null, null, null, null, 50, 0);
        given(operatorRepository.getAllBySearchOptions(storageId, options)).willReturn(List.of());

        var result = operatorService.getAllBySearchOptions(storageId, options);

        assertTrue(result.isEmpty());
    }

    @Test
    void givenValidData_whenUpdate_thenReturnOperator() {
        var user = UserBuilder.builder().role(UserRole.StorageOperator).build();
        var operator = OperatorBuilder.builder().user(user).build();
        var updateForm = new OperatorUpdate(
                "a",
                "b",
                "c",
                "d@e.f",
                null,
                null
        );
        var updatedUser = UserBuilder.builder()
                .userId(user.userId())
                .username(updateForm.username())
                .password(updateForm.password())
                .name(updateForm.name())
                .email(updateForm.email())
                .createdAt(user.createdAt())
                .banned(user.banned())
                .build();
        var updatedOperator = OperatorBuilder.builder()
                .operatorId(operator.operatorId())
                .user(updatedUser)
                .storageId(operator.storageId())
                .role(operator.role())
                .build();
        given(operatorRepository.getByOperatorId(operator.operatorId())).willReturn(Optional.of(operator));
        given(operatorRepository.update(eq(operator.operatorId()), any())).willReturn(updatedOperator);
        given(userService.update(eq(user.userId()), any())).willReturn(updatedUser);

        var result = operatorService.update(operator.storageId(), operator.operatorId(), updateForm);

        assertEquals(updatedOperator, result);
    }

    @Test
    void givenInvalidStorageId_whenUpdate_thenThrowException() {
        var user = UserBuilder.builder().role(UserRole.StorageOperator).build();
        var operator = OperatorBuilder.builder().user(user).build();
        var updateForm = new OperatorUpdate(
                "a",
                "b",
                "c",
                "d@e.f",
                null,
                null
        );
        given(operatorRepository.getByOperatorId(operator.operatorId())).willReturn(Optional.of(operator));

        assertThrows(SklaDinyaException.class, () ->
                operatorService.update(UUID.randomUUID(), operator.operatorId(), updateForm));

        verify(operatorRepository, never()).update(any(), any());
        verify(userService, never()).update(any(), any());
    }

    @Test
    void givenValidData_whenDelete_thenReturnNothing() {
        var user = UserBuilder.builder().role(UserRole.StorageOperator).build();
        var operator = OperatorBuilder.builder().user(user).build();
        given(operatorRepository.getByOperatorId(operator.operatorId())).willReturn(Optional.of(operator));

        operatorService.delete(operator.storageId(), operator.operatorId());

        verify(operatorRepository).delete(operator.operatorId());
        verify(userService).delete(user.userId());
    }

    @Test
    void givenInvalidOperatorId_whenDelete_thenThrowException() {
        var storageId = UUID.randomUUID();
        var operatorId = UUID.randomUUID();
        given(operatorRepository.getByOperatorId(operatorId)).willReturn(Optional.empty());

        assertThrows(SklaDinyaException.class, () -> operatorService.delete(storageId, operatorId));

        verify(operatorRepository, never()).delete(any());
        verify(userService, never()).delete(any());
    }

    @Test
    void givenInvalidStorageId_whenDelete_thenThrowException() {
        var user = UserBuilder.builder().role(UserRole.StorageOperator).build();
        var operator = OperatorBuilder.builder().user(user).build();
        var storageId = UUID.randomUUID();
        given(operatorRepository.getByOperatorId(operator.operatorId())).willReturn(Optional.of(operator));

        assertThrows(SklaDinyaException.class, () -> operatorService.delete(storageId, operator.operatorId()));

        verify(operatorRepository, never()).delete(any());
        verify(userService, never()).delete(any());
    }
}
