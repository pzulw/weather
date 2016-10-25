package org.wisner.weather.parse;

public class ParseException extends Exception {
    public ParseException() {
    }

    public ParseException(Throwable cause) {
        super(cause);
    }

    public ParseException(String message) {
        super(message);
    }

    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
