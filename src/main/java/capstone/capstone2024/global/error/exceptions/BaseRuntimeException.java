package capstone.capstone2024.global.error.exceptions;

import capstone.capstone2024.global.error.ErrorCode;
import lombok.Getter;

@Getter
public abstract class BaseRuntimeException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String message;

    public BaseRuntimeException(final ErrorCode errorCode, final String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public BaseRuntimeException(final ErrorCode errorCode) {
        this(errorCode, null);
    }
}
