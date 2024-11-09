package py.com.cotip.application.config.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import py.com.cotip.application.config.model.CotipResponse;

import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    // ::: CotipException handler

    @ExceptionHandler(CotipException.class)
    public ResponseEntity<Map<String, Object>> handleCotipException(CotipException ex) {
        return ResponseEntity.status(ex.getHttpStatus()).body(CotipResponse.responseError(ex));
    }

    // ::: Generic exception handler

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String errorCode = "UNKNOWN_ERROR";
        String errorMessage = "An unexpected error occurred";

        if (ex instanceof NoSuchElementException || ex instanceof EntityNotFoundException) {
            status = HttpStatus.NOT_FOUND;
            errorCode = "NOT_FOUND";
            errorMessage = "The requested resource was not found";
        }

        CotipException genericException = new CotipException(
                status.value(),
                errorCode,
                errorMessage,
                null,
                false,
                ex
        );
        return ResponseEntity.status(status).body(CotipResponse.responseError(genericException));
    }
}
