package skladinya.domain.services;

import skladinya.domain.models.operator.Operator;
import skladinya.domain.models.operator.OperatorCreate;
import skladinya.domain.models.operator.OperatorSearchOptions;
import skladinya.domain.models.operator.OperatorUpdate;

import java.util.List;
import java.util.UUID;

public interface OperatorService {

    Operator create(UUID storageId, OperatorCreate createForm);

    Operator getByOperatorId(UUID storageId, UUID operatorId);

    Operator getByUserId(UUID storageId, UUID userId);

    List<Operator> getAllBySearchOptions(UUID storageId, OperatorSearchOptions options);

    Operator update(UUID storageId, UUID operatorId, OperatorUpdate updateForm);

    void delete(UUID storageId, UUID operatorId);
}
