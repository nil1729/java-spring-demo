package xyz.nilanjan.spring.exception.demo.exception;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class ApiException {
    private final String message;
    private final Throwable throwable; // remove throwable on the production
    private final HttpStatus httpStatus;
    private final ZonedDateTime timestamp;

    public ApiException(
            String message,
            Throwable throwable, // remove throwable on the production
            HttpStatus httpStatus,
            ZonedDateTime timestamp
    ) {
        this.message = message;
        this.throwable = throwable; // remove throwable on the production
        this.httpStatus = httpStatus;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public Throwable getThrowable() { // remove throwable on the production
        return throwable;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }
}
