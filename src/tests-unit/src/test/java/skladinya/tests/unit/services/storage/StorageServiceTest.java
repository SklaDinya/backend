package skladinya.tests.unit.services.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skladinya.domain.exceptions.SklaDinyaException;
import skladinya.domain.models.storage.*;
import skladinya.domain.models.user.UserRole;
import skladinya.domain.repositories.StorageRepository;
import skladinya.domain.services.EmailService;
import skladinya.domain.services.OperatorService;
import skladinya.domain.services.StorageService;
import skladinya.services.storage.StorageServiceImpl;
import skladinya.tests.helper.builder.OperatorBuilder;
import skladinya.tests.helper.builder.StorageBuilder;
import skladinya.tests.helper.builder.UserBuilder;
import skladinya.tests.helper.builder.UserUpdateBuilder;
import skladinya.tests.helper.factory.StorageCreateFactory;
import skladinya.tests.helper.factory.StorageSearchFactory;
import skladinya.tests.helper.factory.StorageUpdateFactory;
import skladinya.tests.helper.factory.UserUpdateFactory;
import skladinya.tests.helper.synchronizer.SynchronizerTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class StorageServiceTest {

    @Mock
    private StorageRepository storageRepository;

    @Mock
    private OperatorService operatorService;

    @Mock
    private EmailService emailService;

    private StorageService storageService;

    @BeforeEach
    void setUp() {
        var synchronizer = new SynchronizerTest();
        storageService = new StorageServiceImpl(storageRepository, operatorService, emailService, synchronizer);
    }

    @Test
    void givenValidData_whenCreate_thenSendSuccessEmail() {
        var user = UserBuilder.builder().role(UserRole.StorageOperator).build();
        var storage = StorageBuilder.builder().status(StorageStatus.Created).build();
        var operator = OperatorBuilder.builder().user(user).storageId(storage.storageId()).build();
        var storageCreate = StorageCreateFactory.create(storage, operator);
        given(storageRepository.create(any())).willReturn(storage);
        given(operatorService.create(storage.storageId(), storageCreate.operatorCreate())).willReturn(operator);

        storageService.create(storageCreate);

        verify(emailService).sendStorageCreated(user.email(), storage);
    }

    @Test
    void givenExistedUsername_whenCreate_thenThrowException() {
        var user = UserBuilder.builder().role(UserRole.StorageOperator).build();
        var storage = StorageBuilder.builder().status(StorageStatus.Created).build();
        var operator = OperatorBuilder.builder().user(user).storageId(storage.storageId()).build();
        var storageCreate = StorageCreateFactory.create(storage, operator);
        given(storageRepository.create(any())).willReturn(storage);
        willThrow(SklaDinyaException.conflict("")).given(operatorService)
                .create(storage.storageId(), storageCreate.operatorCreate());

        assertThrows(SklaDinyaException.class, () -> storageService.create(storageCreate));

        verify(emailService, never()).sendStorageCreated(any(), any());
    }

    @Test
    void givenValidData_whenGetByStorageId_thenReturnStorage() {
        var storage = StorageBuilder.builder().status(StorageStatus.Active).build();
        given(storageRepository.getByStorageId(storage.storageId())).willReturn(Optional.of(storage));

        var result = storageService.getByStorageId(storage.storageId());

        assertEquals(storage, result);
    }

    @Test
    void givenInvalidStorageId_whenGetByStorageId_thenThrowException() {
        var storageId = UUID.randomUUID();
        given(storageRepository.getByStorageId(storageId)).willReturn(Optional.empty());

        assertThrows(SklaDinyaException.class, () -> storageService.getByStorageId(storageId));
    }

    @Test
    void givenCreatedStorage_whenGetByStorageId_thenThrowException() {
        var storage = StorageBuilder.builder().status(StorageStatus.Created).build();
        given(storageRepository.getByStorageId(storage.storageId())).willReturn(Optional.of(storage));

        assertThrows(SklaDinyaException.class, () -> storageService.getByStorageId(storage.storageId()));
    }

    @Test
    void givenValidData_whenGetAllBySearchOptions_thenReturnStorages() {
        var storage1 = StorageBuilder.builder().build();
        var storage2 = StorageBuilder.builder().build();
        var list = List.of(storage1, storage2);
        var options = StorageSearchFactory.create();
        given(storageRepository.getAllBySearchOptions(options)).willReturn(list);

        var result = storageService.getAllBySearchOptions(options);

        assertEquals(list.size(), result.size());
        for (var i = 0; i < list.size(); ++i) {
            assertEquals(list.get(i), result.get(i));
        }
    }

    @Test
    void givenNoStorages_whenGetAllBySearchOptions_thenReturnEmptyList() {
        var options = StorageSearchFactory.create();
        given(storageRepository.getAllBySearchOptions(options)).willReturn(List.of());

        var result = storageService.getAllBySearchOptions(options);

        assertTrue(result.isEmpty());
    }

    @Test
    void givenValidData_whenUpdate_thenReturnUpdated() {
        var storage = StorageBuilder.builder().build();
        var update = new StorageUpdate("one", "two", "three");
        var updated = StorageUpdateFactory.create(storage, update);
        given(storageRepository.getByStorageId(storage.storageId())).willReturn(Optional.of(storage));
        given(storageRepository.update(eq(storage.storageId()), any())).willReturn(updated);

        var result = storageService.update(storage.storageId(), update);

        assertEquals(updated, result);
    }

    @Test
    void givenInvalidStorageId_whenUpdate_thenThrowException() {
        var storageId = UUID.randomUUID();
        var update = new StorageUpdate("one", "two", "three");
        given(storageRepository.getByStorageId(storageId)).willReturn(Optional.empty());

        assertThrows(SklaDinyaException.class, () -> storageService.update(storageId, update));

        verify(storageRepository, never()).update(any(), any());
    }

    @Test
    void givenValidData_whenApprove_thenReturnStorage() {
        var user = UserBuilder.builder().role(UserRole.StorageOperator).banned(true).build();
        var storage = StorageBuilder.builder().status(StorageStatus.Created).build();
        var operator = OperatorBuilder.builder().user(user).storageId(storage.storageId()).build();
        var updatedStorage = StorageUpdateFactory.create(storage, new StorageUpdate(StorageStatus.Active));
        var updatedUser = UserUpdateFactory.create(user, UserUpdateBuilder.builder().banned(false).build());
        var updatedOperator = OperatorBuilder.builder()
                .operatorId(operator.operatorId())
                .storageId(storage.storageId())
                .user(updatedUser)
                .build();
        given(storageRepository.getByStorageId(storage.storageId())).willReturn(Optional.of(storage));
        given(storageRepository.update(eq(storage.storageId()), any())).willReturn(updatedStorage);
        given(operatorService.getAllBySearchOptions(eq(storage.storageId()), any())).willReturn(List.of(operator));
        given(operatorService.update(eq(storage.storageId()), eq(operator.operatorId()), any()))
                .willReturn(updatedOperator);

        var result = storageService.approve(storage.storageId());

        assertEquals(updatedStorage, result);
        verify(emailService).sendStorageApproved(updatedUser.email(), updatedStorage);
    }

    @Test
    void givenActiveStorage_whenApprove_thenThrowException() {
        var storage = StorageBuilder.builder().status(StorageStatus.Active).build();
        given(storageRepository.getByStorageId(storage.storageId())).willReturn(Optional.of(storage));

        assertThrows(SklaDinyaException.class, () -> storageService.approve(storage.storageId()));

        verify(storageRepository, never()).update(any(), any());
        verify(operatorService, never()).getAllBySearchOptions(any(), any());
        verify(operatorService, never()).update(any(), any(), any());
        verify(emailService, never()).sendStorageApproved(any(), any());
    }

//    void reject(UUID storageId);

    @Test
    void givenValidData_whenReject_thenSendRejectEmail() {
        var user = UserBuilder.builder().role(UserRole.StorageOperator).banned(true).build();
        var storage = StorageBuilder.builder().status(StorageStatus.Created).build();
        var operator = OperatorBuilder.builder().user(user).storageId(storage.storageId()).build();
        given(storageRepository.getByStorageId(storage.storageId())).willReturn(Optional.of(storage));
        given(operatorService.getAllBySearchOptions(eq(storage.storageId()), any())).willReturn(List.of(operator));

        storageService.reject(storage.storageId());

        verify(storageRepository).delete(storage.storageId());
        verify(operatorService).delete(storage.storageId(), operator.operatorId());
        verify(emailService).sendStorageRejected(user.email(), storage);
    }

    @Test
    void givenActiveStorage_whenReject_thenThrowException() {
        var storage = StorageBuilder.builder().status(StorageStatus.Active).build();
        given(storageRepository.getByStorageId(storage.storageId())).willReturn(Optional.of(storage));

        assertThrows(SklaDinyaException.class, () -> storageService.reject(storage.storageId()));

        verify(storageRepository, never()).delete(any());
        verify(operatorService, never()).getAllBySearchOptions(any(), any());
        verify(operatorService, never()).delete(any(), any());
        verify(emailService, never()).sendStorageRejected(any(), any());
    }
}
