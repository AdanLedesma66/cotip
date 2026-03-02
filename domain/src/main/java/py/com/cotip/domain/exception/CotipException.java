package py.com.cotip.domain.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
public class CotipException extends RuntimeException {

    // ::: vars

    private final int httpStatus;
    private final String code;
    private final String description;
    private final boolean isUserMessage;
    @Setter
    private Map<String, ?> extra;

    // ::: constructor

    public CotipException(int httpStatus, String code, String description, boolean isUserMessage, Throwable cause) {
        super(description, cause);
        this.httpStatus = httpStatus;
        this.code = code;
        this.description = description;
        this.isUserMessage = isUserMessage;
    }

    public CotipException(int httpStatus, String code, String description, boolean isUserMessage) {
        super(description);
        this.httpStatus = httpStatus;
        this.code = code;
        this.description = description;
        this.isUserMessage = isUserMessage;
    }

    public CotipException(int httpStatus, String code, String description, String description1, boolean b, Throwable cause, int httpStatus1, String code1, String description2, boolean isUserMessage) {
        this.httpStatus = httpStatus1;
        this.code = code1;
        this.description = description2;
        this.isUserMessage = isUserMessage;
    }
}
