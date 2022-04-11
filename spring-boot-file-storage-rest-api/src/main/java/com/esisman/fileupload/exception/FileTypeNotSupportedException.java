package com.esisman.fileupload.exception;

public class FileTypeNotSupportedException extends RuntimeException {
    public FileTypeNotSupportedException(String message) {
        super(message);
    }
}
