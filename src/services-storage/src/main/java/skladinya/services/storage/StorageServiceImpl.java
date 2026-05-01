package skladinya.services.storage;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import skladinya.domain.exceptions.SklaDinyaException;
import skladinya.domain.helpers.Synchronizer;
import skladinya.domain.models.operator.Operator;
import skladinya.domain.models.operator.OperatorSearchOptions;
import skladinya.domain.models.operator.OperatorUpdate;
import skladinya.domain.models.storage.*;
import skladinya.domain.repositories.StorageRepository;
import skladinya.domain.services.EmailService;
import skladinya.domain.services.OperatorService;
import skladinya.domain.services.StorageService;

import java.util.List;
import java.util.UUID;

@Service
@RequestScope
public class StorageServiceImpl implements StorageService {

    private final StorageRepository storageRepository;

    private final OperatorService operatorService;

    private final EmailService emailService;

    private final Synchronizer synchronizer;

    public StorageServiceImpl(
            StorageRepository storageRepository,
            OperatorService operatorService,
            EmailService emailService,
            Synchronizer synchronizer
    ) {
        this.storageRepository = storageRepository;
        this.operatorService = operatorService;
        this.emailService = emailService;
        this.synchronizer = synchronizer;
    }

    @Override
    public void create(StorageCreate createForm) {
        synchronizer.executeTransactionConsumer(() -> {
            var created = new Storage(createForm.storageName(), createForm.address(), createForm.description());
            var storage = storageRepository.create(created);
            var operator = operatorService.create(storage.storageId(), createForm.operatorCreate());
            emailService.sendStorageCreated(operator.user().email(), storage);
        });
    }

    @Override
    public Storage getByStorageId(UUID storageId) {
        return synchronizer.executeSingleFunction(() ->
                storageRepository.getByStorageId(storageId).orElseThrow(() ->
                        SklaDinyaException.notFound(String.format("Storage %s not found", storageId))));
    }

    @Override
    public List<Storage> getAllBySearchOptions(StorageSearchOptions options) {
        return synchronizer.executeSingleFunction(() -> storageRepository.getAllBySearchOptions(options));
    }

    @Override
    public Storage update(UUID storageId, StorageUpdate updateForm) {
        return synchronizer.executeTransactionFunction(() -> {
            var storage = getByStorageId(storageId);
            var updated = new Storage(
                    storageId,
                    updateForm.name() == null ? storage.name() : updateForm.name(),
                    updateForm.address() == null ? storage.address() : updateForm.address(),
                    updateForm.description() == null ? storage.description() : updateForm.description(),
                    updateForm.status() == null ? storage.status() : updateForm.status(),
                    storage.createdAt()
            );
            return storageRepository.update(storageId, updated);
        });
    }

    @Override
    public Storage approve(UUID storageId) {
        return synchronizer.executeTransactionFunction(() -> {
            var storageUpdate = new StorageUpdate(StorageStatus.Active);
            var storage = update(storageId, storageUpdate);
            var bannedOperator = getCreatedOperator(storageId);
            var operatorUpdate = new OperatorUpdate(false);
            var operator = operatorService.update(storageId, bannedOperator.operatorId(), operatorUpdate);
            emailService.sendStorageApproved(operator.user().email(), storage);
            return storage;
        });
    }

    @Override
    public void reject(UUID storageId) {
        synchronizer.executeTransactionConsumer(() -> {
            var storage = getCreatedStorage(storageId);
            var operator = getCreatedOperator(storageId);
            operatorService.delete(storageId, operator.operatorId());
            storageRepository.delete(storageId);
            emailService.sendStorageRejected(operator.user().email(), storage);
        });
    }

    private Storage getCreatedStorage(UUID storageId) {
        var storage = getByStorageId(storageId);
        if (!StorageStatus.Created.equals(storage.status())) {
            throw SklaDinyaException.validationError(String.format("Storage %s already approved", storageId));
        }
        return storage;
    }

    private Operator getCreatedOperator(UUID storageId) {
        var options = new OperatorSearchOptions(2, 0);
        var operators = operatorService.getAllBySearchOptions(storageId, options);
        if (operators.size() != 1) {
            throw SklaDinyaException.wrap(new RuntimeException("Invalid operator count"));
        }
        return operators.getFirst();
    }
}
