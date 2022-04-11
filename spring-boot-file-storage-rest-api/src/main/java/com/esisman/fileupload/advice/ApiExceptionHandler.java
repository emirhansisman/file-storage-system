package com.esisman.fileupload.advice;

import com.esisman.fileupload.exception.FileConflictException;
import com.esisman.fileupload.exception.FileSizeLimitExceedException;
import com.esisman.fileupload.exception.FileTypeNotSupportedException;
import com.esisman.fileupload.exception.NoContentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDate;

@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(FileSizeLimitExceedException.class)
    public ResponseEntity<ApiExceptionResponse> handleFileSizeLimitExceedException(ApiExceptionResponse exception) {
        return new ResponseEntity<>(apiExceptionResponseBuilder(HttpStatus.BAD_REQUEST, exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileTypeNotSupportedException.class)
    public ResponseEntity<ApiExceptionResponse> handleFileTypeNotSupportedException(ApiExceptionResponse exception) {
        return new ResponseEntity<>(apiExceptionResponseBuilder(HttpStatus.UNPROCESSABLE_ENTITY, exception.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(FileConflictException.class)
    public ResponseEntity<ApiExceptionResponse> handleFileConflictException(ApiExceptionResponse exception) {
        return new ResponseEntity<>(apiExceptionResponseBuilder(HttpStatus.CONFLICT, exception.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NoContentException.class)
    public ResponseEntity<ApiExceptionResponse> handleNoContentException(ApiExceptionResponse exception) {
        return new ResponseEntity<>(apiExceptionResponseBuilder(HttpStatus.NO_CONTENT, exception.getMessage()), HttpStatus.NO_CONTENT);
    }

    private ApiExceptionResponse apiExceptionResponseBuilder(HttpStatus httpStatus, String cause) {
        return ApiExceptionResponse.builder()
                .httpStatus(httpStatus)
                .message(cause)
                .timestamp(LocalDate.now())
                .build();
    }
}
