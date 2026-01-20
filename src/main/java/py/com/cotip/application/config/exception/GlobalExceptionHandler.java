package py.com.cotip.application.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import py.com.cotip.application.config.model.CotipResponse;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CotipException.class)
    public ResponseEntity<Map<String, Object>> handleCotipException(CotipException ex) {
        return CotipResponse.responseError(ex.getHttpStatus(), CotipResponse.responseError(ex));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAll(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CotipResponse.responseError(ex));
    }
}