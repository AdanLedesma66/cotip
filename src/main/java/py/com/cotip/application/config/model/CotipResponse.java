package py.com.cotip.application.config.model;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import py.com.cotip.application.config.exception.CotipException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
@Setter
@Slf4j
public class CotipResponse<T> {

    // ::: data

    private T data;

    // ::: response
    public static <T> CotipResponse<T> of(T data) {
        CotipResponse<T> response = new CotipResponse<>();
        response.setData(data);
        return response;
    }

    public static <T> CotipResponse<T> of(Optional<T> data) {
        CotipResponse<T> response = new CotipResponse<>();
        data.ifPresent(response::setData);
        return response;
    }

    public static <T> CotipResponse<T> of(Optional<T> data, HttpServletResponse response) {
        if (!data.isPresent()) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return null;
        } else {
            CotipResponse<T> res = new CotipResponse<>();
            res.setData(data.get());
            return res;
        }
    }

    public static Object empty() {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // ::: response error

    public static Map<String, Object> responseError(CotipException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("code", ex.getCode());
        error.put("description", ex.getDescription());
        error.put("extra", ex.getExtra());
        error.put("text", ex.getText());
        error.put("isUserMessage", ex.isUserMessage());

        Map<String, Object> body = new HashMap<>();
        body.put("failure", error);
        body.put("status", ex.getHttpStatus());

        return body;
    }

    public static Map<String, Object> responseError(Throwable t) {
        if (t instanceof CotipException ex) {
            return responseError(ex);
        }

        Map<String, Object> error = new HashMap<>();
        error.put("code", "UNKNOWN_ERROR");
        error.put("description", "An unexpected error occurred");
        error.put("extra", null);
        error.put("text", t.getMessage());

        Map<String, Object> body = new HashMap<>();
        body.put("failure", error);
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

        return body;
    }

    // ::: Overloaded response error for custom HTTP status codes
    public static ResponseEntity<Map<String, Object>> responseError(int httpStatus, Map<String, Object> errorBody) {
        return ResponseEntity.status(httpStatus).body(errorBody);
    }
}