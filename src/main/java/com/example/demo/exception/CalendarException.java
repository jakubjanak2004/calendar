package com.example.demo.exception;

public class CalendarException extends RuntimeException {
    public CalendarException() {
    }

    public CalendarException(String message) {
        super(message);
    }

    public CalendarException(String message, Throwable cause) {
        super(message, cause);
    }

    public CalendarException(Throwable cause) {
        super(cause);
    }
}
