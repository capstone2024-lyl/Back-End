package capstone.capstone2024.global.error.exceptions;

import capstone.capstone2024.global.error.ErrorCode;

public class InternalServerException extends BaseRuntimeException {
    public InternalServerException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}