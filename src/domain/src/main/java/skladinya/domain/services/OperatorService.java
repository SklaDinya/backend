package skladinya.domain.services;

import skladinya.domain.models.operator.Operator;
import skladinya.domain.models.operator.OperatorCreate;
import skladinya.domain.models.operator.OperatorSearchOptions;
import skladinya.domain.models.operator.OperatorUpdate;

import java.util.List;
import java.util.UUID;

public interface OperatorService {

    Operator create(OperatorCreate createForm);

    Operator getByOperatorId(UUID operatorId);

    List<Operator> getAllBySearchOptions(OperatorSearchOptions options);

    Operator update(OperatorUpdate updateForm);
}
