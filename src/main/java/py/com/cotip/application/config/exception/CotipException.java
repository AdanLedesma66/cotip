package py.com.cotip.application.config.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
public class CotipException extends RuntimeException {

    // ::: vars

    private final int httpStatus;
    private final String code;
    private final String description;
    private final String text;
    private final boolean isUserMessage;
    @Setter
    private Map<String, ?> extra;

    // ::: constructor

    public CotipException(int httpStatus, String code, String description, String text, boolean isUserMessage, Throwable cause) {
        super(description, cause);
        this.httpStatus = httpStatus;
        this.code = code;
        this.description = description;
        this.text = text;
        this.isUserMessage = isUserMessage;
    }

    public CotipException(int httpStatus, String code, String description, String text, boolean isUserMessage) {
        super(description);
        this.httpStatus = httpStatus;
        this.code = code;
        this.description = description;
        this.text = text;
        this.isUserMessage = isUserMessage;
    }

    public CotipException(int httpStatus, String code, String description, Throwable cause) {
        this(httpStatus, code, description, description, false, cause);
    }

    public CotipException(int httpStatus, String code, String description) {
        this(httpStatus, code, description, description, false);
    }
}