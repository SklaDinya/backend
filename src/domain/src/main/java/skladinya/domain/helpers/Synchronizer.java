package skladinya.domain.helpers;

public interface Synchronizer {

    default <T> T executeTransactionFunction(SynchronizedFunction<T> function) {
        return function.apply();
    }

    default void executeTransactionConsumer(SynchronizedConsumer consumer) {
        consumer.accept();
    }

    default <T> T executeSingleFunction(SynchronizedFunction<T> function) {
        return function.apply();
    }

    default void executeSingleConsumer(SynchronizedConsumer consumer) {
        consumer.accept();
    }
}
