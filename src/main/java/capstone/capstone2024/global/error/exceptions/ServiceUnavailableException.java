package capstone.capstone2024.global.error.exceptions;

import capstone.capstone2024.global.error.ErrorCode;

public class ServiceUnavailableException extends BaseRuntimeException {
    public ServiceUnavailableException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
