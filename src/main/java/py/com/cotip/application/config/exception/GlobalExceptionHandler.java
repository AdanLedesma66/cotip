package py.com.cotip.application.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import py.com.cotip.application.config.model.CotipResponse;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // ::: global exception

    @ExceptionHandler(CotipException.class)
    public ResponseEntity<Map<String, Object>> handleCotipException(CotipException ex) {
        return ResponseEntity.status(ex.getHttpStatus()).body(CotipResponse.responseError(ex));
    }

    // ::: generic exception

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        CotipException genericException = new CotipException(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "UNKNOWN_ERROR",
                "An unexpected error occurred",
                null,
                false,
                ex
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(CotipResponse.responseError(genericException));
    }
}