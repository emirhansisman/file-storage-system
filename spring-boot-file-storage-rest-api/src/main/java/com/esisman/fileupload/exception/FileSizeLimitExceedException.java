package com.esisman.fileupload.exception;

public class FileSizeLimitExceedException extends RuntimeException {
    public FileSizeLimitExceedException(String message) {
        super(message);
    }
}
