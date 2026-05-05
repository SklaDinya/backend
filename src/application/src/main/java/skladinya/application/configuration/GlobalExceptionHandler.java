package skladinya.application.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import skladinya.domain.exceptions.ExceptionType;
import skladinya.domain.exceptions.SklaDinyaException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(SklaDinyaException.class)
    public ResponseEntity<String> handleSkladinyaException(SklaDinyaException ex) {
        logger.warn("Exception type: {}", ex.getExceptionType());
        logger.warn("Exception body: {}", ex.getMessage());
        logger.warn("Exception: ", ex);
        return (switch (ex.getExceptionType()) {
            case ExceptionType.ValidationError -> ResponseEntity.badRequest();
            case ExceptionType.BadCredentials -> ResponseEntity.status(HttpStatus.UNAUTHORIZED);
            case ExceptionType.InvalidAccess -> ResponseEntity.status(HttpStatus.FORBIDDEN);
            case ExceptionType.NotFound -> ResponseEntity.notFound();
            case ExceptionType.Conflict -> ResponseEntity.status(HttpStatus.CONFLICT);
            case ExceptionType.TeapotMock -> ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT);
            case ExceptionType.Wrap -> ResponseEntity.internalServerError();
        }).build();
    }
}
