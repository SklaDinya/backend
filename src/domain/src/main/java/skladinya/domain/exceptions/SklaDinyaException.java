package skladinya.domain.exceptions;

public final class SklaDinyaException extends RuntimeException {

    private final ExceptionType exceptionType;

    private SklaDinyaException(ExceptionType exceptionType, String message) {
        super(message);
        this.exceptionType = exceptionType;
    }

    private SklaDinyaException(Exception e) {
        super(e);
        this.exceptionType = ExceptionType.Wrap;
    }

    public ExceptionType getExceptionType() {
        return exceptionType;
    }

    public static SklaDinyaException wrap(Exception e) {
        return new SklaDinyaException(e);
    }

    public static SklaDinyaException validationError(String message) {
        return new SklaDinyaException(ExceptionType.ValidationError, message);
    }

    public static SklaDinyaException badCredentials(String message) {
        return new SklaDinyaException(ExceptionType.BadCredentials, message);
    }

    public static SklaDinyaException invalidAccess(String message) {
        return new SklaDinyaException(ExceptionType.InvalidAccess, message);
    }

    public static SklaDinyaException notFound(String message) {
        return new SklaDinyaException(ExceptionType.NotFound, message);
    }

    public static SklaDinyaException conflict(String message) {
        return new SklaDinyaException(ExceptionType.Conflict, message);
    }

    public static SklaDinyaException joke(String message) {
        return new SklaDinyaException(ExceptionType.DeveloperIsClown, message);
    }
}
