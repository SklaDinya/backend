package skladinya.domain.repositories;

import skladinya.domain.models.operator.Operator;
import skladinya.domain.models.operator.OperatorSearchOptions;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OperatorRepository {

    Operator create(Operator operator);

    Optional<Operator> getByOperatorId(UUID operatorId);

    Optional<Operator> getByUserId(UUID userId);

    List<Operator> getAllBySearchOptions(UUID storageId, OperatorSearchOptions options);

    Operator update(UUID operatorId, Operator operator);

    void delete(UUID operatorId);
}
