package com.netflix.kayenta.metrics;

public class FailedToGenerateQueryException extends RuntimeException {
    public FailedToGenerateQueryException(String message, Throwable cause) {
        super(message, cause);
    }
}
