package org.wisner.weather.network;

public class DownloadException extends Exception {
    public DownloadException() {
    }

    public DownloadException(Throwable cause) {
        super(cause);
    }

    public DownloadException(String message) {
        super(message);
    }

    public DownloadException(String message, Throwable cause) {
        super(message, cause);
    }
}
