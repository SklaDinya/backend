package skladinya.persistence.helpers;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import skladinya.domain.helpers.SynchronizedConsumer;
import skladinya.domain.helpers.SynchronizedFunction;
import skladinya.domain.helpers.Synchronizer;

public class PostgresSynchronizer implements Synchronizer {

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public <T> T executeTransactionFunction(SynchronizedFunction<T> function) {
        return Synchronizer.super.executeTransactionFunction(function);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void executeTransactionConsumer(SynchronizedConsumer consumer) {
        Synchronizer.super.executeTransactionConsumer(consumer);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public <T> T executeSingleFunction(SynchronizedFunction<T> function) {
        return Synchronizer.super.executeSingleFunction(function);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void executeSingleConsumer(SynchronizedConsumer consumer) {
        Synchronizer.super.executeSingleConsumer(consumer);
    }
}
