package capstone.capstone2024.global.error.exceptions;

import capstone.capstone2024.global.error.ErrorCode;

public class BadRequestException extends BaseRuntimeException {
    public BadRequestException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}