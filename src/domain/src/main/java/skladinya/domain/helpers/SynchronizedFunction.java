package skladinya.domain.helpers;

@FunctionalInterface
public interface SynchronizedFunction<T> {

    T apply();
}
