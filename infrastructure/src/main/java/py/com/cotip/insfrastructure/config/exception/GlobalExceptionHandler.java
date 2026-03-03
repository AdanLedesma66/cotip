package py.com.cotip.insfrastructure.config.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import py.com.cotip.domain.commons.CotipError;
import py.com.cotip.domain.exception.CotipException;
import py.com.cotip.insfrastructure.config.model.CotipResponse;

import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CotipException.class)
    public ResponseEntity<Map<String, Object>> handleCotipException(CotipException ex) {
        HttpHeaders headers = new HttpHeaders();
        if (ex.getExtra() != null) {
            Object retryAfterSeconds = ex.getExtra().get("retryAfterSeconds");
            if (retryAfterSeconds != null) {
                headers.set("Retry-After", String.valueOf(retryAfterSeconds));
            }
        }

        return new ResponseEntity<>(
                CotipResponse.responseError(ex),
                headers,
                HttpStatus.valueOf(ex.getHttpStatus())
        );
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class,
            ConstraintViolationException.class, HandlerMethodValidationException.class})
    public ResponseEntity<Map<String, Object>> handleValidationException(Exception ex) {
        CotipException validationException = new CotipException(
                HttpStatus.BAD_REQUEST.value(),
                CotipError.REQUEST_VALIDATION_ERROR.getCode(),
                extractValidationMessage(ex),
                true
        );

        return handleCotipException(validationException);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAll(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CotipResponse.responseError(ex));
    }

    private String extractValidationMessage(Exception ex) {
        if (ex instanceof MethodArgumentNotValidException methodArgumentException) {
            return methodArgumentException.getBindingResult()
                    .getAllErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse("Request validation error");
        }

        if (ex instanceof BindException bindException) {
            return bindException.getBindingResult()
                    .getAllErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse("Request validation error");
        }

        if (ex instanceof ConstraintViolationException constraintViolationException) {
            return constraintViolationException.getConstraintViolations()
                    .stream()
                    .map(violation -> violation.getMessage())
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse("Request validation error");
        }

        if (ex instanceof HandlerMethodValidationException handlerMethodValidationException) {
            return handlerMethodValidationException.getAllErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse("Request validation error");
        }

        return "Request validation error";
    }
}
