package py.com.cotip.insfrastructure.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import py.com.cotip.insfrastructure.config.model.CotipResponse;
import py.com.cotip.domain.exception.CotipException;

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
