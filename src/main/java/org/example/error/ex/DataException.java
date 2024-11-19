package org.example.error.ex;

public class DataException extends RuntimeException {
    
    private final ErrorCode errorCode;
    
    public DataException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    
    public DataException(final ErrorCode errorCode, final String message) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public DataException(final ErrorCode errorCode, final String message, final Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
}
