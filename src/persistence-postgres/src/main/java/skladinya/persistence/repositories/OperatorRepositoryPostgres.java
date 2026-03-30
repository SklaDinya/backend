package skladinya.persistence.repositories;

import skladinya.domain.models.operator.Operator;
import skladinya.domain.models.operator.OperatorSearchOptions;
import skladinya.domain.repositories.OperatorRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class OperatorRepositoryPostgres implements OperatorRepository {
    @Override
    public Operator create(Operator operator) {
        return null;
    }

    @Override
    public Optional<Operator> getByOperatorId(UUID operatorId) {
        return Optional.empty();
    }

    @Override
    public Optional<Operator> getByUserId(UUID userId) {
        return Optional.empty();
    }

    @Override
    public List<Operator> getAllBySearchOptions(OperatorSearchOptions options) {
        return List.of();
    }

    @Override
    public Operator update(UUID operatorId, Operator operator) {
        return null;
    }

    @Override
    public void delete(UUID operatorId) {

    }
}
