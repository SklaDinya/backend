package skladinya.services.operator;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import skladinya.domain.exceptions.SklaDinyaException;
import skladinya.domain.helpers.Synchronizer;
import skladinya.domain.models.operator.Operator;
import skladinya.domain.models.operator.OperatorCreate;
import skladinya.domain.models.operator.OperatorSearchOptions;
import skladinya.domain.models.operator.OperatorUpdate;
import skladinya.domain.models.user.UserCreate;
import skladinya.domain.models.user.UserRole;
import skladinya.domain.models.user.UserUpdate;
import skladinya.domain.repositories.OperatorRepository;
import skladinya.domain.services.OperatorService;
import skladinya.domain.services.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

@Service
@RequestScope
public class OperatorServiceImpl implements OperatorService {

    private final OperatorRepository operatorRepository;

    private final UserService userService;

    private final Synchronizer synchronizer;

    public OperatorServiceImpl(
            OperatorRepository operatorRepository,
            UserService userService,
            Synchronizer synchronizer
    ) {
        this.operatorRepository = operatorRepository;
        this.userService = userService;
        this.synchronizer = synchronizer;
    }

    @Override
    public Operator create(UUID storageId, OperatorCreate createForm, boolean banned) {
        return synchronizer.executeTransactionFunction(() -> {
            var userCreate = new UserCreate(
                    createForm.username(),
                    createForm.password(),
                    createForm.name(),
                    createForm.email(),
                    UserRole.StorageOperator,
                    banned
            );
            var user = userService.create(userCreate);
            var operator = new Operator(user, storageId, createForm.role());
            return operatorRepository.create(operator);
        });
    }

    @Override
    public Operator getByOperatorId(UUID storageId, UUID operatorId) {
        return synchronizer.executeSingleFunction(() ->
                get(storageId, () -> operatorRepository.getByOperatorId(operatorId)));
    }

    @Override
    public Operator getByUserId(UUID storageId, UUID userId) {
        return synchronizer.executeSingleFunction(() ->
                get(storageId, () -> operatorRepository.getByUserId(userId)));
    }

    @Override
    public List<Operator> getAllBySearchOptions(UUID storageId, OperatorSearchOptions options) {
        return synchronizer.executeSingleFunction(() -> operatorRepository.getAllBySearchOptions(storageId, options));
    }

    @Override
    public Operator update(UUID storageId, UUID operatorId, OperatorUpdate updateForm) {
        return synchronizer.executeTransactionFunction(() -> {
            var operator = getByOperatorId(storageId, operatorId);
            var userUpdate = new UserUpdate(
                    updateForm.username(),
                    updateForm.password(),
                    updateForm.name(),
                    updateForm.email(),
                    null,
                    updateForm.banned()
            );
            var user = userService.update(operator.userId(), userUpdate);
            var updated = new Operator(
                    operatorId,
                    user.userId(),
                    user,
                    storageId,
                    updateForm.role() == null ? operator.role() : updateForm.role()
            );
            return operatorRepository.update(operatorId, updated);
        });
    }

    @Override
    public void delete(UUID storageId, UUID operatorId) {
        synchronizer.executeTransactionConsumer(() -> {
            var operator = getByOperatorId(storageId, operatorId);
            operatorRepository.delete(operatorId);
            userService.delete(operator.userId());
        });
    }

    private Operator get(UUID storageId, Supplier<Optional<Operator>> supplier) {
        var operator = supplier.get().orElseThrow(() -> SklaDinyaException.notFound("Operator not found"));
        if (!storageId.equals(operator.storageId())) {
            throw SklaDinyaException.notFound("Operator not found");
        }
        return operator;
    }
}
